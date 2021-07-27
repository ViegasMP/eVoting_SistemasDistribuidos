import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.rmi.RemoteException;

import static java.lang.System.exit;

public class AdminConsole implements Serializable {
    RMIInterface rmi;

    private AdminConsole(RMIInterface rmi) {
        super();
        this.rmi = rmi;
        menu();
    }

    //ponto1
    public void register() {
        String name;
        String phoneNumber;
        String address;
        String password;
        Department Department = null;
        String idCardNumber;
        String expirationDate;

        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        try {
            User newUser = null;
            System.out.println("Name: ");
            name = reader.readLine();
            System.out.println("ID card number: ");
            idCardNumber = reader.readLine();
            System.out.println("ID card expiration date: ");
            expirationDate = reader.readLine();
            System.out.println("Password: ");
            password = reader.readLine();
            System.out.println("Phone Number: ");
            phoneNumber = reader.readLine();
            System.out.println("Address: ");
            address = reader.readLine();
            System.out.println("Department: ");
            Department = chooseDepartment(rmi);

            System.out.println("1 - Student\n2 - Teacher\n3 - Employee ");
            String option = reader.readLine();
            boolean invalid = true;
            while (invalid) {
                if (option.equals("1")) {
                    newUser = new Student(name, phoneNumber, address, password, Department, idCardNumber, expirationDate);
                    invalid = false;
                } else if (option.equals("2")) {
                    newUser = new Teacher(name, phoneNumber, address, password, Department, idCardNumber, expirationDate);
                    invalid = false;
                } else if (option.equals("3")) {
                    newUser = new Employee(name, phoneNumber, address, password, Department, idCardNumber, expirationDate);
                    invalid = false;
                }
            }

            while (true) {
                try {
                    rmi.register(newUser);
                    break;
                } catch (RemoteException ignored) {
                    run();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Department chooseDepartment(RMIInterface rmi) {
        try {
            System.out.println("List of existing departments:");
            ArrayList<Department> depList = rmi.getDepartmentList();
            int i = 0;
            for (Department dep : depList) {
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
            } while (optionInt <= 0 || optionInt > depList.size());
            //System.out.println(listaDep.get(opcaoint-1).getNome());
            return depList.get(optionInt - 1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // função auxiliar para criar os departamentos
    private void createDep(String name) {
        try {
            InputStreamReader input = new InputStreamReader(System.in);
            BufferedReader reader = new BufferedReader(input);
            ArrayList<Department> departments;
            while (true) {
                departments = rmi.getDepartmentList();
                break;
            }
            Department dep = new Department(name, new ArrayList<>());
            while (true) {
                rmi.addDepartment(dep);
                break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static int verifyNum(String num){

        int size = num.length();
        if (size == 0)
            return 0;
        char c;
        int count = 0;
        for(int i = 0; i < size; i++){
            c = num.charAt(i);
            if (Character.isDigit(c))
                count++;
        }
        if (count == size)
            return 1;
        else
            return 0;
    }

    private static int validDate(int dd, int mm, int yy, int hour, int min) {
        int bissextile;
        if (yy % 4 == 0)
            bissextile = 1;
        else
            bissextile = 0;
        if (yy == -1) {
            System.out.println("Invalid Date. Try Again.\n--------------\n\n");
            return 0;
        }

        if(dd > 31 || dd < 1 || mm > 12 || mm < 1) {
            System.out.println("Invalid Date. Try Again.\n--------------\n\n");
            return 0;
        }
        else if(mm == 2 && dd < 30 && bissextile==1 && (hour >= 0 && hour <= 23) && (min >= 0 && min <= 59) || mm == 2 && dd < 29 && bissextile==1) {
            return 1;
        } else if((mm == 4 || mm == 6 || mm == 9 || mm == 11) && dd < 31 && (hour >= 0 && hour <= 23) && (min >= 0 && min <= 59)) {
            return 1;
        } else if(dd < 31 && mm != 2 && (hour >= 0 && hour <= 23) && (min >= 0 && min <= 59)) {
            return 1;
        } else {
            System.out.println("Invalid Date. Try Again.\n--------------\n\n");
            return 0;
        }
    }

    // pedir uma data
    private static Calendar askDate(){
        int count;
        int day, year, month, hour, min;
        String pday, pyear, pmonth, phour, pmin;
        do{
            count = 0;
            System.out.println("Day:");
            Scanner sc = new Scanner(System.in);
            pday = sc.nextLine();
            if (verifyNum(pday)==1){
                day=Integer.parseInt(pday);
                count++;
            }
            else
                day=-1;
            System.out.println("Month:");
            sc = new Scanner(System.in);
            pmonth= sc.nextLine();
            if (verifyNum(pmonth)==1){
                month=Integer.parseInt(pmonth);
                count++;
            }
            else
                month=-1;
            System.out.println("Year:");
            sc = new Scanner(System.in);
            pyear= sc.nextLine();
            if (verifyNum(pyear)==1){
                year=Integer.parseInt(pyear);
                count++;
            }
            else
                year=-1;
            System.out.println("Hour:");
            sc = new Scanner(System.in);
            phour= sc.nextLine();
            if (verifyNum(phour)==1){
                hour=Integer.parseInt(phour);
                count++;
            }
            else
                hour=-1;
            System.out.println("Minutes:");
            sc = new Scanner(System.in);
            pmin= sc.nextLine();
            if (verifyNum(pmin)==1){
                min=Integer.parseInt(pmin);
                count++;
            }
            else
                min=-1;
        }while(validDate(day,month,year,hour,min)!=1 || count!=5);
        Calendar date = Calendar.getInstance();
        date.clear();
        date.set(year,month-1,day,hour,min);
        return date;
    }

    private void altInfo() throws RemoteException {
        try {
            User user = chooseUser();
            if (user != null) {
                for (int i = 0; i < rmi.getUserList().size(); i++) {
                    if (user.getName().equals(rmi.getUserList().get(i).getName())) {
                        rmi.RemoveUser(i);
                    }
                }
                register();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private User chooseUser(){
        System.out.println("User List: ");
        ArrayList<User> userList;
        while (true){
            try{
                userList=rmi.getUserList();
                break;
            } catch (RemoteException ignored) {
                run();
            }
        }
        if (userList.isEmpty()) {
            System.out.println("\tNo users created\n you can register users in option 1\n");
            return null;
        }
        for(int i=0;i<userList.size();i++){
            System.out.println((i+1)+" - "+userList.get(i).getIdCardNumber());
        }
        Scanner sc = new Scanner(System.in);
        String option;
        option=sc.nextLine();
        int optionInt;
        do{
            try {
                optionInt= Integer.parseInt(option);
            } catch (NumberFormatException e) {
                optionInt= 0;
            }
        }while(optionInt<=0 || optionInt>userList.size());

        System.out.println(userList.get(optionInt-1).getName());

        return userList.get(optionInt-1);

    }

    public void createElection() throws RemoteException {
        //for(int i=0; i < rmi.getListaEleicoes().size();i++)
        //    System.out.println(rmi.getListaEleicoes().get(i).getTitulo());
        try {
            System.out.println("\tCreate Election\nType of election:\n1 - Student Council Election\n2 - General Counsel Election");
            Scanner sc = new Scanner(System.in);
            String option;
            int optionInt;
            do {
                System.out.println("Indicate the type of election:");
                option = sc.nextLine();
                try {
                    optionInt = Integer.parseInt(option);
                } catch (NumberFormatException e) {
                    optionInt = 0;
                }
            } while (optionInt != 1 && optionInt != 2);
            switch (optionInt) {
                case 1:
                    Election studentCouncil = createStudentCouncil();
                    while (true) {
                        try {
                            rmi.addElection(studentCouncil);
                            break;
                        } catch (RemoteException ignored) {
                            run();
                        }
                    }
                    break;
                case 2:
                    Election generalCounsel = createGeneralCounsel();
                    while (true) {
                        try {
                            rmi.addElection(generalCounsel);
                            break;
                        } catch (RemoteException ignored) {
                            run();
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        //for(int i=0; i < rmi.getListaEleicoes().size();i++)
        //    System.out.println(rmi.getListaEleicoes().get(i).getTitulo());
    }

    private void altElection() throws RemoteException {
        //for(int i=0; i < rmi.getListaEleicoes().size();i++)
        //    System.out.println(rmi.getListaEleicoes().get(i).getTitulo());
        try {
            Election election = chooseElection();
            if (election != null) {
                for (int i = 0; i < rmi.getElectionsList().size(); i++) {
                    if (election.getTitle().equals(rmi.getElectionsList().get(i).getTitle()) && election.getDescription().equals(rmi.getElectionsList().get(i).getDescription())) {
                        rmi.removeElection(i);
                    }
                }
                Scanner sc = new Scanner(System.in);
                System.out.println("Title");
                String title = sc.nextLine();
                System.out.println("Description");
                String description = sc.nextLine();
                System.out.println("Elections start date");
                Calendar start = askDate();
                System.out.println("Elections end date");
                Calendar end = askDate();

                if (!election.verifyVoting() && election.verifyVotingExpired()) {
                    election.setTitle(title);
                    election.setDescription(description);
                    election.setStart(start);
                    election.setEnd(end);
                    while (true) {
                        rmi.addElection(election);
                        break;
                    }
                }
                else{
                    System.out.println("Invalid date.");
                }

            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    private Election chooseElection(){
        System.out.println("Election List:");
        ArrayList<Election> electionList;
        int i=0;
        while (true) {
            try {
                electionList = rmi.getElectionsList();
                break;
            } catch (RemoteException ignored) {
                run();
            }
        }
        if (electionList.isEmpty()){
            System.out.println("\tThere are no elections currently\n you can create elections in option 2\n");
            return null;
        } else {
            for (Election election: electionList){

                if (!election.verifyVoting() && election.verifyVotingExpired()) {
                    i++;
                    System.out.println(i + " - " + election.getTitle());
                }
            }
        }

        Scanner sc = new Scanner(System.in);
        String opcao;
        opcao=sc.nextLine();
        int opcaoint;
        do{
            try {
                opcaoint= Integer.parseInt(opcao);
            } catch (NumberFormatException e) {
                opcaoint= 0;
            }
        }while(opcaoint<=0 || opcaoint>electionList.size());

        return electionList.get(opcaoint-1);
    }

    private StudentCouncil createStudentCouncil() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Name: ");
        String title = sc.nextLine();
        System.out.println("Description: ");
        String description = sc.nextLine();
        System.out.println("Elections start date");
        Calendar start = askDate();
        System.out.println("Elections end date");
        Calendar end = askDate();
        Department dep = chooseDepartment(rmi);
        ArrayList<CandidateList> list = new ArrayList<>();
        StudentCouncil election = new StudentCouncil(title, description, start, end, dep, list);
        if (!election.verifyVoting() && election.verifyVotingExpired()) {
            return election;
        }
        else{
            System.out.println("Invalid date.");
            return null;
        }
    }

    private GeneralCounsel createGeneralCounsel() {
        GeneralCounsel election;
        Scanner sc = new Scanner(System.in);
        System.out.println("Create a title:");
        String title = sc.nextLine();
        System.out.println("Create a description:");
        String description = sc.nextLine();
        System.out.println("Elections start date:");
        Calendar start = askDate();
        System.out.println("Elections end date:");
        Calendar end = askDate();
        ArrayList<CandidateList> studentCandidates = new ArrayList<>();
        ArrayList<CandidateList> employeeCandidates = new ArrayList<>();
        ArrayList<CandidateList> teacherCandidates = new ArrayList<>();

        ArrayList<User> listP;
        while (true) {
            try {
                listP = rmi.getUserList();
                break;
            } catch (RemoteException ignored) {
                run();
            }
        }
        election = new GeneralCounsel(title, description, start, end, studentCandidates, teacherCandidates, employeeCandidates, listP);
        if (!election.verifyVoting() && election.verifyVotingExpired()) {
            return election;
        }
        else{
            System.out.println("Invalid date.");
            return null;
        }
    }

    private void manageCandidateLists() throws RemoteException {
        Election election=chooseElection();
        ArrayList<Election> elections;
        while (true) {
            try {
                elections = rmi.getElectionsList();
                break;
            } catch (RemoteException ignored) {
                run();
            }
        }
        for (int i=0;i<elections.size();i++)
            if ((election.getTitle().toUpperCase()).equals(rmi.getElectionsList().get(i).getTitle().toUpperCase()) && (election.getDescription().toUpperCase()).equals(rmi.getElectionsList().get(i).getDescription().toUpperCase()))
                rmi.removeElection(i);

        if (election != null) {
            election.editCandidates();
            while (true) {
                try {
                    rmi.addElection(election);
                    break;
                } catch (RemoteException ignored) {
                    run();
                }
            }
        }
    }

    private void manageVotingTables(){
        Scanner sc = new Scanner(System.in);
        String option;
        int optionInt;
        do {
            System.out.println("\tManage Voting Tables\n1 - Create voting table\n2 - Delete voting table\n3 - Associate election to voting table");
            option = sc.nextLine();
            try {
                optionInt = Integer.parseInt(option);
            } catch (NumberFormatException e) {
                optionInt = 0;
            }
        } while (optionInt!=1 && optionInt!=2 && optionInt!=3);

        switch (optionInt){
            case 1:
                createVotingTable();
                break;
            case 2:
                deleteVotingTable();
                break;
            case 3:
                associateVotingTable();
                break;
        }
    }

    private void createVotingTable() {
        try {
            Department dep = chooseDepartment(rmi);
            ArrayList<MulticastServer> tables;
            while (true)
                try {
                    tables = rmi.getVotingTables();
                    break;
                } catch (RemoteException ignored) {
                    run();
                }
            for (MulticastServer table : tables){
                if((table.getDepartment().getName()).equals(dep.getName())){
                    System.out.println("This department already has a voting table.");
                    return;
                }
            }
            MulticastServer newTable = new MulticastServer(dep);
            while (true)
                try {
                    rmi.addTable(newTable);
                    break;
                } catch (RemoteException ignored) {
                    run();
                }
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteVotingTable(){
        try{
            ArrayList<MulticastServer> tables;
            while (true)
                try {
                    tables = rmi.getVotingTables();
                    break;
                } catch (RemoteException ignored) {
                    run();
                }
            if (tables.isEmpty()){
                System.out.println("\n*There are no voting tables*");
                return;
            }
            int i = 0;
            for(MulticastServer table: tables)
                System.out.println((++i)+" - "+table.getDepartment().getName());
            String option;
            Scanner sc = new Scanner(System.in);
            int optionInt;
            do {
                System.out.println("Choose a voting table to delete");
                option = sc.nextLine();
                try {
                    optionInt = Integer.parseInt(option);
                } catch (NumberFormatException e) {
                    optionInt = -1;
                }
            } while (optionInt<1 && optionInt> tables.size());
            MulticastServer chosenTable = tables.get(optionInt-1);
            for(Election election: chosenTable.getElectionsList()){
                System.out.println("Voting table with elections. Impossible to delete.");
            }
            rmi.removeTable(optionInt-1);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void associateVotingTable() {
        try {
            Election ele= chooseElection();
            if (ele==null)
                return;
            int i=0;
            ArrayList<MulticastServer> tables;
            while (true)
                try {
                    tables = rmi.getVotingTables();
                    break;
                } catch (RemoteException ignored) {
                    run();
                }
            System.out.println("Voting table list:");
            for(MulticastServer table: tables)
                System.out.println((++i)+" - "+table.getDepartment().getName());
            if (i==0){
                System.out.println("There are no voting tables");
                return;
            }
            String option;
            Scanner sc= new Scanner(System.in);
            int optionInt;
            do {
                System.out.println("Choose a voting table");
                option = sc.nextLine();
                try {
                    optionInt = Integer.parseInt(option);
                } catch (NumberFormatException e) {
                    optionInt = -1;
                }
            } while (optionInt<1 && optionInt>tables.size());
            MulticastServer chosenTable = tables.get(optionInt-1);
            for (Election election : chosenTable.getElectionsList() ){
                if(election.getTitle().equals(ele.getTitle())&&election.getDescription().equals(ele.getDescription())){
                    System.out.println("This voting table already has this election");
                    return;
                }
            }
            while (true){
                try {
                    rmi.removeTable(optionInt-1);
                    break;
                } catch (RemoteException ignored) {
                    run();
                }
            }
            chosenTable.addElection(ele);
            while (true) {
                try {
                    rmi.addTable(chosenTable);
                    break;
                } catch (RemoteException ignored) {
                    run();
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void votingPlace() throws  IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\tVoter voting places\n");
        System.out.print("ID card number: ");
        String idCardNumber = reader.readLine();
        ArrayList<Election> elections = new ArrayList<>();

        while (true) {
            try {
                elections = rmi.getElectionsList();
                break;
            } catch (RemoteException ignored) {
                run();
            }
        }
        for (Election aux : elections)
            aux.votingPlace(idCardNumber);
    }

    private void tableState(){
        try {
            ArrayList<MulticastServer> tables;
            while (true)
            {
                tables = rmi.getVotingTables();
                break;
            }
            for (MulticastServer table : tables)
                System.out.println(table.getDepartment().getName() +"("+ table.getTableState()+")");

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


    private void realTimeElections() {
        int i = 0;
        ArrayList<MulticastServer> mesasVotos;
        while (true)
            try {
                mesasVotos = rmi.getVotingTables();
                break;
            } catch (RemoteException ignored) {
                run();
            }

        if (!mesasVotos.isEmpty()) {
            System.out.println("\tOpen voting tables:");
            for (MulticastServer mesa : mesasVotos) {
                if (mesa.getTableState())
                    System.out.println("[" + (++i) + "]" + mesa.getDepartment().getName());
            }
            if (i!=0) {
                System.out.print("-");
                Scanner scanner = new Scanner(System.in);
                MulticastServer mesaEscolhida = mesasVotos.get(scanner.nextInt() - 1);

                System.out.println("\tActive Elections: ");
                for (Election eleicao : mesaEscolhida.getElectionsList()) {
                    if (eleicao.verifyVoting()) {
                        System.out.println(eleicao.getTitle());
                        eleicao.currentNumberOfVotes();
                        System.out.println();
                    }
                }
            }else
                System.out.println("No active voting tables");
        }else
            System.out.println("No voting tables");
    }

    private void consultOldElections() {
        try {
            ArrayList<Election> electionList;
            while (true) {
                try {
                    electionList = rmi.getElectionsList();
                    break;
                } catch (RemoteException ignored) {
                    run();
                }
            }
            ArrayList<Election> auxElectionList = new ArrayList<>();
            int i = 0;
            if (electionList.isEmpty()) {
                System.out.println("No elections registered");
                return;
            }
            for (Election ele : electionList) {
                if (!ele.verifyVotingExpired())
                    auxElectionList.add(ele);
            }
            System.out.println("Lista de eleições passadas:");
            for (Election election : auxElectionList)
                System.out.println((++i) + " - " + election.getTitle());

            String opt;
            if (auxElectionList.isEmpty()) {
                System.out.println("No expired elections");
                return;
            }
            Scanner sc = new Scanner(System.in);
            int optint;
            do {
                System.out.println("choose election");
                opt = sc.nextLine();
                try {
                    optint = Integer.parseInt(opt);
                } catch (NumberFormatException e) {
                    optint = -1;
                }
            } while (optint < 1 && optint > auxElectionList.size());
            auxElectionList.get(optint - 1).print();


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private void run() {
        int sleep_time = 1000;
        while (true) {
            try {
                this.rmi = (RMIInterface) Naming.lookup("rmi://localhost:7500/eVoting");
                this.rmi.sayHello();
                return;
            } catch (NotBoundException | RemoteException | MalformedURLException ignored) {
                try {
                    Thread.sleep(sleep_time);
                    sleep_time *= 2;
                    if (sleep_time > 16000) {
                        System.out.println("\n\t*RMI Server Error*");
                        exit(0);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void menu() {
        System.out.println("Choose an option:\n1.Register user\n2.Create election\n3.Manage candidate lists of an election");
        System.out.println("4.Manage voting tables\n9.Alter Election\n10.Voting Place\n11.Voting Table State\n12.Consult elections in real time\n15.Alter Info");
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
        } while (optionInt <= 0 && optionInt > 10);
        try {
            switch (optionInt) {
                case 0:

                //adicionar departamentos ao ficheiro de objetos departamentos.ser
                createDep("DEI");
                createDep("DEEC");
                createDep("DEC");
                createDep("DEM");
                createDep("DEQ");
                createDep("DCV");
                createDep("DCT");
                System.out.println( rmi.getDepartmentList().get(0).getName());
                    break;
                case 1:
                    register();
                    break;
                case 2:
                    createElection();
                    break;
                case 3:
                    manageCandidateLists();
                    break;
                case 4:
                    manageVotingTables();
                    break;
                case 9:
                    altElection();
                    break;
                case 10:
                    votingPlace();
                    break;
                case 11:
                    tableState();
                    break;
                case 12:
                    realTimeElections();
                    break;
                case 14:
                    consultOldElections();
                    break;
                case 15:
                    altInfo();
                    break;

            }
            menu();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String args[]) {
        try {
            RMIInterface rmi = (RMIInterface) Naming.lookup("rmi://localhost:7500/eVoting");
            rmi.sayHello();
            AdminConsole adminConsole = new AdminConsole(rmi);
        } catch (Exception e) {
            System.out.println("Exception in main: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
