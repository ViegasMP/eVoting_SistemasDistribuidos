import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.util.Scanner;

/**
 * Voting Terminal – São os clientes Multicast que estão associados a cada mesa de voto,
 * e que permitem realizar as funcionalidades 7 e 8.
 * 7) Autenticação de eleitor no terminal de voto: Após a identificação, um eleitor dirige-se
 * ao terminal de voto que tiver sido desbloqueado e apresenta o seu user- name/password para
 * obter acesso ao "boletim de voto", ou seja, à enumeração das listas candidatas à eleição
 * em que se irá votar.
 * 8) Votar: Cada eleitor pode votar no máximo uma vez por eleição, escolhendo no terminal de
 * voto uma das seguintes opções: uma das listas candidatas, voto em branco ou voto nulo. É
 * fundamental que cada voto seja secreto, que cada eleitor vote apenas uma vez nas eleições
 * em que estiver autorizado, e que todos os votos sejam contados corretamente no final.
 * Qualquer pessoa pode votar em qualquer mesa, mesmo que seja noutro departamento que não o seu.
 *
 * Recomenda-se que o terminal de voto utilize dois grupos de Multicast distintos:
 * um para descobrir e ser descoberto pelo servidor (por exemplo, quando o servidor precisa de um terminal livre)
 *          MulticastClient
 * e outro grupo apenas para comunicar a intenção de voto do eleitor.
 *          MulticastVoting
 */
public class VotingTerminal extends Thread {
    private String MULTICAST_ADDRESS = "224.3.2.3";
    private int PORT_R = 5000;
    private int PORT_S = 5005;
    private long SLEEP_TIME = 10000;


    public static void main(String[] args) {
        VotingTerminal client = new VotingTerminal();
        client.start();
    }

    public void run() {
        MulticastSocket socket_send=null, socket_receive=null;
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        byte[] buffer_receive = new byte[256];
        byte[] buffer_send;
        DatagramPacket packet_receive, packet_send = null;
        String text = null;
        String message = null;

        System.out.println(this.getName() + " running...");
        System.out.println("\tWelcome to the voting terminal");
        try {
            socket_receive = new MulticastSocket(PORT_R);//binding socket
            socket_send = new MulticastSocket();
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket_receive.joinGroup(group);
            socket_send.joinGroup(group);
            while (true) {
                // type | login ; userID | 123 ; password | 123
                System.out.println("login:");
                //pede login
                try {
                    text = reader.readLine();
                } catch (Exception ignored) {
                }

                //envia login
                buffer_send = text.getBytes();
                packet_send = new DatagramPacket(buffer_send, buffer_send.length, group, PORT_S);
                socket_send.send(packet_send);

                //recebe validacao se o login foi concluido
                packet_receive = new DatagramPacket(buffer_receive, buffer_receive.length);
                socket_receive.receive(packet_receive);
                message = new String(packet_receive.getData(), 0, packet_receive.getLength());
                System.out.println(message);

                String[] result = message.split(" ; ");
                String[] type = result[0].split(" | ");
                String logged = result[1].split(" | ")[2];

                //System.out.println("type = "+ type[2]);
                //System.out.println("logged = "+logged);
                //se o status de login for on
                if (type[2].equals("status") && logged.equals("on")) {
                    //recebo boletim de votos
                    packet_receive = new DatagramPacket(buffer_receive, buffer_receive.length);
                    socket_receive.receive(packet_receive);
                    message = new String(packet_receive.getData(), 0, packet_receive.getLength());
                    System.out.println(message);

                    System.out.println("type | vote ; option | *");

                    text = null;
                    try {
                        text = reader.readLine();
                    } catch (Exception ignored) {
                    }

                    buffer_send = text.getBytes();
                    packet_send = new DatagramPacket(buffer_send, buffer_send.length, group, PORT_S);
                    socket_send.send(packet_send);

                    break;
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket_receive.close();
            socket_send.close();
        }
    }
}
