import java.net.MulticastSocket;
import java.net.*;
import java.io.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.*;
import static java.lang.System.exit;

/**
 * Multicast Server – Existe um Multicast Server por cada mesa de voto
 * que gere localmente os terminais de voto que lhe estão associados.
 * Permite aos membros da mesa realizar a funcionalidade 6 e realiza
 * automaticamente a funcionalidade 5.
 *
 * 5) Gerir terminais de voto: Cada mesa de voto pode ter diversos
 * terminais de voto associados. Tal como se descreve mais à frente,
 * a comunicação e gestão destes terminais será feita inteiramente por
 * Multicast. Esta gestão deverá ser automática, bastando configurar o
 * grupo de Multicast para que as máquinas se descubram e identifiquem.
 * Uma alternativa é utilizar dois grupos de multicast distintos:
 *      um para descoberta de máquinas
 *              MulticastServer
 *      e outro para comunicação de votos dos terminais para o servidor da mesa.
 *              MulticastServerReceive
 * 6) Identificar eleitor na mesa de voto. O primeiro passo para que uma
 * pessoa possa votar consiste em pesquisar por qualquer campo por forma
 * a identificar esse eleitor. Ao identificar um eleitor, um dos terminais
 * de voto fica desbloqueado para que essa pessoa possa ir votar secretamente
 * (um terminal que esteja livre deve ser escolhido usando as funcionalidades
 * do Multicast). Deve poder-se listar e escolher uma eleição, caso haja mais
 * do que uma eleição em simultâneo. O terminal de voto volta a ficar bloqueado
 * após 120 segundos sem uso.
 */

public class MulticastServer extends Thread implements Serializable{
    private static String MULTICAST_ADDRESS = "224.3.2.1";
    private static int PORT = 5001;
    private static long SLEEP_TIME = 10000;
    public int id;

    private Department department;
    private ArrayList<Election> electionList;
    private Boolean tableState;

    public MulticastServer(Department department) {
        super("Server " + (long) (Math.random() * 1000));
        this.department = department;
        this.electionList = new ArrayList<>();
        this.tableState = false;
    }

    public MulticastServer() {
        super("Server " + (long) (Math.random() * 1000));
    }

    public void setTableState(Boolean tableState) {
        this.tableState = tableState;
    }

    public void addElection(Election e) {
        this.electionList.add(e);
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public ArrayList<Election> getElectionsList() {
        return electionList;
    }

    public Boolean getTableState() {
        return tableState;
    }

    private static Department escolheDepartamento(RMIInterface rmi) {
        try {
            System.out.println("Lista de departamentos existentes:");
            ArrayList<Department> listaDep = rmi.getDepartmentList();
            int i = 0;
            for (Department dep : listaDep) {
                i++;
                System.out.println(i + " - " + dep.getName());
            }
            Scanner sc = new Scanner(System.in);
            String option;
            option = sc.nextLine();
            int optionInt;
            do {
                try {
                    optionInt = Integer.parseInt(option);
                } catch (NumberFormatException e) {
                    optionInt = 0;
                }
            } while (optionInt <= 0 || optionInt > listaDep.size());
            //System.out.println(listaDep.get(opcaoint-1).getNome());
            return listaDep.get(optionInt - 1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        Department department;
        Scanner scanner = new Scanner(System.in);
        boolean verifyTable = false;
        MulticastSocket socket=null;
        int number =0;
        try {
            //ligar ao RMI
            RMIInterface rmi = (RMIInterface) Naming.lookup("rmi://localhost:7500/eVoting");
            socket = new MulticastSocket(PORT);
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);

            department=escolheDepartamento(rmi);
            verifyTable = rmi.manageVotingTable(department, 1);

            if(!verifyTable) {
                System.out.println("Non existing table or already closed");
                socket.close();
                exit(0);
            }

            while (true) {

                System.out.println("1 - Identify voter\n2 - Close voting table");
                String choice = scanner.next();

                switch (choice) {
                    // identificar eleitor e eleicao
                    case "1":
                        User voter;
                        System.out.print("Voter's id card number: ");
                        String numCC = scanner.next();
                        voter = rmi.indentifyVoter(numCC); // ver se o eleitor existe, se sim, retorna a pessoa

                        if (voter != null) {
                            System.out.println("Voter "+voter.getName()+" identified\n\nList of available elections:");
                            ArrayList<Election> elections;
                            // eleicoes disponiveis para o leitor votar
                            elections = rmi.identifyElections(voter, department);

                            if (!elections.isEmpty()) {
                                int i = 0;
                                for (Election aux : elections)
                                    System.out.println((++i) + " - " + aux.getTitle());
                                int option = scanner.nextInt();
                                Election election;
                                election = rmi.chooseElection(voter, department, option);

                                if (election != null) { // eleicao identificada pelo o eleitor
                                    MulticastServerReceive serverReceive = new MulticastServerReceive(rmi, election, department, voter);
                                    serverReceive.start();
                                }

                            } else {
                                System.out.println("No elections available to vote in.\n");
                            }
                        }
                        break;
                    // fechar a atual mesa de voto (estado da mesa = false)
                    case "2":
                        verifyTable = rmi.manageVotingTable(department, 2);
                        System.out.println("Voting table closed.");
                        exit(0);
                        break;
                }
            }
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Socket Exception");
        } catch (NotBoundException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }

    /**
    *Grupo Multicast para a comunicação de votos dos terminais para o servidor da mesa de votos
    **/
    static class MulticastServerReceive extends Thread {
        private String MULTICAST_ADDRESS = "224.3.2.3";
        private int PORT_R = 5005;
        private int PORT_S = 5000;
        private Election election; //identificador de eleicao
        private Department department; //local da eleicao
        private User voter; //eleitor
        private ArrayList<CandidateList> candidateLists; //lista dos candidatos
        private RMIInterface rmi;

        MulticastServerReceive(RMIInterface rmi, Election election, Department department, User voter) {
            this.election = election;
            this.department = department;
            this.rmi = rmi;
            this.voter = voter;
            this.candidateLists = null;

        }

        public void run() {
            byte[] buffer_send;
            byte[] buffer_receive;
            DatagramPacket packet_receive = null, packet_send = null;
            String text = null;
            //while (true) {
                MulticastSocket socket_send = null, socket_receive = null;
                try {
                    socket_send = new MulticastSocket();
                    socket_receive = new MulticastSocket(PORT_R);
                    InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
                    socket_send.joinGroup(group);
                    socket_receive.joinGroup(group);

                    while (true) {

                        //receber autenticacao
                        buffer_receive = new byte[1000];
                        packet_receive = new DatagramPacket(buffer_receive, buffer_receive.length);
                        socket_receive.receive(packet_receive);
                        String message = new String(packet_receive.getData(), 0, packet_receive.getLength());
                        System.out.println(message);
                        String[] result = message.split(" ; ");
                        String[] type = result[0].split(" | ");
                        //System.out.println(type[2]);

                        //tipo login
                        if (type[2].equals("login")) {
                            String userID = result[1].split(" | ")[2];
                            String password = result[2].split(" | ")[2];

                            //password validada
                            if (this.voter.getPassword().equals(password)) {

                                //resposta de login validado
                                text = "type | status ; logged | on ; msg | Welcome to eVoting";
                                System.out.println(text);
                                buffer_send = text.getBytes();
                                packet_send = new DatagramPacket(buffer_send, buffer_send.length, group, PORT_S);
                                socket_send.send(packet_send);

                                //cria boletim de votos
                                this.candidateLists = this.election.getCandidateList(voter);
                                StringBuilder ballor = new StringBuilder("");
                                int i = 0;
                                if (this.candidateLists != null) {
                                    for (CandidateList aux : this.candidateLists)
                                        ballor.append(aux.getName()).append(" | ").append(++i).append(" ; ");
                                }
                                System.out.println(ballor.toString());

                                //envia boletim
                                buffer_send = ballor.toString().getBytes();
                                packet_send = new DatagramPacket(buffer_send, buffer_send.length, group, PORT_S);
                                socket_send.send(packet_send);


                            } else {
                                text = "type | status ; logged | off ; msg | Wrong credentials";
                                buffer_send = text.getBytes();
                                packet_send = new DatagramPacket(buffer_send, buffer_send.length, group, PORT_S);
                                socket_send.send(packet_send);
                            }

                        }
                        else if (type[2].equals("vote")) {
                            int option = Integer.parseInt( result[1].split(" | ")[2]);
                            System.out.println(option);
                            Vote vote = new Vote(this.voter, this.candidateLists.get(option), this.department);
                            rmi.electorVote(this.election, vote);
                            break;

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}