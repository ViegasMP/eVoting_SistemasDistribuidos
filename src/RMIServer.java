import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class RMIServer extends UnicastRemoteObject implements RMIInterface{
    static private int PORT = 5001;
    static String MULTICAST_ADDRESS = "224.3.2.1";
    private MulticastSocket dSocket;
    private static ArrayList<String> multicastServers = new ArrayList<>();
    private static DatagramSocket nSocket;
    private static boolean run=true;

    /**
     * Lista de Eleições do sistema
     */
    private ArrayList<Election> electionsList;
    /**
     * Lista de departamentos do sistema
     */
    private ArrayList<Department> departmentsList;
    /**
     * Lista de Pessoas do sistema
     */
    private ArrayList<User> usersList;
    /**
     * Lista de mesas de voto do sistema
     */
    private ArrayList<MulticastServer> votingTablesList;

    public RMIServer() throws RemoteException, SocketException {
        super();
        this.electionsList = new ArrayList<>();
        this.departmentsList = new ArrayList<>();
        this.usersList = new ArrayList<>();
        this.votingTablesList = new ArrayList<>();
        readFiles();
        try{
            dSocket =  new MulticastSocket(5000);
        }
        catch(IOException b){
            System.out.println("Error creating socket");
        }
    }

    synchronized public ArrayList<Department> getDepartmentList() {
        return departmentsList;
    }

    synchronized public void electorVote(Election election, Vote vote) {
        for (User user: this.usersList)
            if (user.getIdCardNumber().equals(vote.getVoter().getIdCardNumber()))
                for (Election auxElection : this.electionsList) {
                    if (auxElection.electionEquals(election)) {
                        auxElection.removeVoter(vote);
                        auxElection.addVote(vote);
                    }
                }
        saveFiles(3);
    }

    @Override
    public void addDepartment(Department dep) throws RemoteException {
        this.departmentsList.add(dep);
        saveFiles(2);
    }

    public ArrayList<User> getUserList() {
        return usersList;
    }


    @Override
    public void addElection(Election election) throws RemoteException {
        this.electionsList.add(election);
        saveFiles(3);
    }

    synchronized public void removeElection(int i){
        this.electionsList.remove(i);
        saveFiles(3);
    }

    synchronized public ArrayList<Election> getElectionsList()  {
        return electionsList;
    }

    synchronized public ArrayList<MulticastServer> getVotingTables(){
        return votingTablesList;
    }

    synchronized public void addTable(MulticastServer table){
        votingTablesList.add(table);
        saveFiles(4);
        saveFiles(2);
    }

    synchronized public void removeTable(int i){
        this.votingTablesList.remove(i);
        saveFiles(4);
    }

    synchronized public boolean manageVotingTable(Department department, int option){
        //abrir mesa de voto
        for (MulticastServer aux : votingTablesList)
            //abrir mesa
            if (option == 1) {
                if ((department.getName().toUpperCase()).equals(aux.getDepartment().getName().toUpperCase())) {
                    aux.setTableState(true);
                    saveFiles(2);
                    saveFiles(4);
                    return true;
                }
            }
            //fechar mesa de voto
            else if (option == 2) {
                if (department.getName().equals(aux.getDepartment().getName()) && aux.getTableState()) {
                    aux.setTableState(false);
                    saveFiles(2);
                    saveFiles(4);
                    return true;
                }
            }
        return false;
    }

    synchronized public User indentifyVoter(String idCardNumber) {
        for (User user: this.usersList)
            if (user.getIdCardNumber().equals(idCardNumber))
                return user;
        return null;
    }

    synchronized public ArrayList<Election> identifyElections(User voter, Department dep){
        ArrayList<Election> elections = new ArrayList<>();
        for (MulticastServer tableaux : this.votingTablesList) {
            if ((tableaux.getDepartment().getName().toUpperCase()).equals(dep.getName().toUpperCase())) {
                for (Election aux : tableaux.getElectionsList()) {
                    for (Election election : electionsList) {
                        if (election.getTitle().equals(aux.getTitle())) {
                            elections.add(election);
                        }
                    }
                }
            }
        }
        return elections;
    }

    synchronized public Election chooseElection(User voter, Department dep, int i){
        for (MulticastServer tablesaux : this.votingTablesList)
            if (tablesaux.getDepartment().getName().equals(dep.getName()))
                for (Election aux: tablesaux.getElectionsList())
                    return aux;
        return null;
    }

    synchronized public void RemoveUser(int i) throws RemoteException{
        this.usersList.remove(i);
        saveFiles(1);
    }

    public ArrayList<MulticastServer> getMesasVotos() {
        return votingTablesList;
    }


    @Override
    synchronized public Boolean register(User user) throws RemoteException{

        for (User aux: this.usersList) {
            //print da lista de pessoas ja registadas
            System.out.println(aux.getName());
            //pessoa já registada
            if (user.getIdCardNumber().equals(aux.getIdCardNumber()))
                return false;
        }
        for (Department dep : departmentsList) {
            if ((user.getDepartment().getName()).equals(dep.getName())) {
                //user.addUserToList(usersList, dep);
                usersList.add(user);
                saveFiles(1);
                return true;
            }
        }
        return false;
    }

    public static void main(String args[]) throws RemoteException, SocketException {
        RMIInterface server=new RMIServer();
        try {
            Registry r = LocateRegistry.createRegistry(7500);
            System.out.println(LocateRegistry.getRegistry(7500));
            r.rebind("eVoting", server);
            //meter as mesas de voto vazias
            //for (MulticastServer m: r.mesasVoto)
                //m.setEstadoMesa(false);
            System.out.println("RMIServer ready.");

        } catch (RemoteException e) {
            //backup server
            boolean programFails = true;
            while (programFails) {
                programFails = false;
                try {
                    Thread.sleep(5000);
                    LocateRegistry.createRegistry(7500).rebind("eVoting",server);
                    run=false;
                    System.out.println("Connected! Server Backup assumed");
                    run=true;

                } catch (RemoteException | InterruptedException b) {
                    System.out.println("Main RMI Server working... Waiting for failures");
                    programFails = true;
                }
            }
        }
    }

    @Override
    public void sayHello() throws RemoteException {
    }

    /**
     * carregar os dados dos ficheiros para as classes
     */
    public void readFiles() {
        FileInputStream streamIn;
        ObjectInputStream objectinputstream = null;
        try {
            //streamIn = new FileInputStream("/Users/ViegasMP/SD/meta_1/pessoas.ser");
            //streamIn = new FileInputStream("/Users/anita/Desktop/SD/meta_1/pessoas.ser");
            streamIn = new FileInputStream("C:\\Users\\HP\\Documents\\AIJASUSALGUEMMEAJUDE\\pessoas.ser");
            objectinputstream = new ObjectInputStream(streamIn);
            ArrayList<User> users = (ArrayList<User>) objectinputstream.readObject();
            this.usersList =users;
            //streamIn = new FileInputStream("/Users/ViegasMP/SD/meta_1/departamentos.ser");
            //streamIn = new FileInputStream("/Users/anita/Desktop/SD/meta_1/departamentos.ser");
            streamIn = new FileInputStream("C:\\Users\\HP\\Documents\\AIJASUSALGUEMMEAJUDE\\departamentos.ser");
            objectinputstream = new ObjectInputStream(streamIn);
            ArrayList<Department> deps = (ArrayList<Department>) objectinputstream.readObject();
            this.departmentsList =deps;
            //streamIn = new FileInputStream("/Users/ViegasMP/SD/meta_1/eleicoes.ser");
            //streamIn = new FileInputStream("/Users/anita/Desktop/SD/meta_1/eleicoes.ser");
            streamIn = new FileInputStream("C:\\Users\\HP\\Documents\\AIJASUSALGUEMMEAJUDE\\eleicoes.ser");
            objectinputstream = new ObjectInputStream(streamIn);
            ArrayList<Election> elections = (ArrayList<Election>) objectinputstream.readObject();
            this.electionsList =elections;
            //streamIn = new FileInputStream("/Users/ViegasMP/SD/meta_1/mesas.ser");
            streamIn = new FileInputStream("C:\\Users\\HP\\Documents\\AIJASUSALGUEMMEAJUDE\\mesas.ser");
            objectinputstream = new ObjectInputStream(streamIn);
            ArrayList<MulticastServer> mesas = (ArrayList<MulticastServer>) objectinputstream.readObject();
            this.votingTablesList= mesas;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (objectinputstream != null) {
                try {
                    objectinputstream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * guardar os dados do sistema para ficheiros
     */
    public void saveFiles(int flag){
        ObjectOutputStream oos=null;
        FileOutputStream fout;
        try{
            if (flag == 1){
                //fout = new FileOutputStream("/Users/ViegasMP/SD/meta_1/pessoas.ser");
                //fout = new FileOutputStream("/Users/anita/Desktop/SD/meta_1/pessoas.ser");
                fout = new FileOutputStream("C:\\Users\\HP\\Documents\\AIJASUSALGUEMMEAJUDE\\pessoas.ser");
                oos = new ObjectOutputStream(fout);
                oos.writeObject(usersList);
            } else if (flag == 2){
                //fout = new FileOutputStream("/Users/ViegasMP/SD/meta_1/departamentos.ser");
                //fout = new FileOutputStream("/Users/anita/Desktop/SD/meta_1/departamentos.ser");
                fout = new FileOutputStream("C:\\Users\\HP\\Documents\\AIJASUSALGUEMMEAJUDE\\departamentos.ser");
                oos = new ObjectOutputStream(fout);
                oos.writeObject(departmentsList);
            } else if (flag == 3){
                //fout = new FileOutputStream("/Users/ViegasMP/SD/meta_1/eleicoes.ser");
                //fout = new FileOutputStream("/Users/anita/Desktop/SD/meta_1/eleicoes.ser");
                fout = new FileOutputStream("C:\\Users\\HP\\Documents\\AIJASUSALGUEMMEAJUDE\\eleicoes.ser");
                oos = new ObjectOutputStream(fout);
                oos.writeObject(electionsList);
            } else if (flag == 4) {
                //fout = new FileOutputStream("/Users/ViegasMP/SD/meta_1/mesas.ser");
                fout = new FileOutputStream("C:\\Users\\HP\\Documents\\AIJASUSALGUEMMEAJUDE\\mesas.ser");
                oos = new ObjectOutputStream(fout);
                oos.writeObject(votingTablesList);
            }
        }catch (IOException e) {
            System.out.println("IOEXCEPTION");
        }finally {
            if(oos  != null ){
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

