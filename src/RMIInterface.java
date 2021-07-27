import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface RMIInterface extends Remote {
    public void sayHello() throws RemoteException;
    Boolean register(User user) throws java.rmi.RemoteException;
    public ArrayList<Department> getDepartmentList() throws java.rmi.RemoteException;
    public void addDepartment(Department dep) throws java.rmi.RemoteException;
    public ArrayList<User> getUserList() throws java.rmi.RemoteException;

    //void criarEleicao(Eleicao eleicao) throws java.rmi.RemoteException;
    //ArrayList<ListaCandidata> getListaCandidatos()throws java.rmi.RemoteException;
    //void addCandidatos(ListaCandidata candidato) throws java.rmi.RemoteException;

    ArrayList<Election> getElectionsList()throws java.rmi.RemoteException;
    void addElection(Election election) throws java.rmi.RemoteException;
    void removeElection(int i) throws java.rmi.RemoteException;

    public ArrayList<MulticastServer> getVotingTables() throws java.rmi.RemoteException;
    public void addTable(MulticastServer table) throws java.rmi.RemoteException;
    public void removeTable(int i) throws java.rmi.RemoteException;
    public boolean manageVotingTable(Department department, int option) throws java.rmi.RemoteException;

    public User indentifyVoter(String idCardNumber) throws java.rmi.RemoteException;
    public ArrayList<Election> identifyElections(User voter, Department dep) throws java.rmi.RemoteException;

    public Election chooseElection(User voter, Department dep, int i) throws java.rmi.RemoteException;
    public void electorVote(Election election, Vote vote) throws java.rmi.RemoteException;
    public void RemoveUser(int i) throws java.rmi.RemoteException;

}

