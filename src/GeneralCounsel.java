import java.io.Serializable;
import java.util.*;

public class GeneralCounsel extends Election implements Serializable{
    private ArrayList<Vote> studentVoteList;
    private ArrayList<Vote> teacherVoteList;
    private ArrayList<Vote> employeeVoteList;
    private ArrayList<CandidateList> studentCandidateList;
    private ArrayList<CandidateList> teacherCandidateList;
    private ArrayList<CandidateList> employeeCandidateList;
    private ArrayList<Student> studentList;
    private ArrayList<Teacher> teacherList;
    private ArrayList<Employee> employeeList;



    public GeneralCounsel(String title, String description, Calendar start, Calendar end, ArrayList<CandidateList> studentCandidateList, ArrayList<CandidateList> teacherCandidateList, ArrayList<CandidateList> employeeCandidateList, ArrayList<User> votersList) {
        super(title, description, start, end);
        this.studentCandidateList = studentCandidateList;
        this.teacherCandidateList = teacherCandidateList;
        this.employeeCandidateList = employeeCandidateList;
        this.studentList = new ArrayList<>();
        this.teacherList = new ArrayList<>();
        this.employeeList = new ArrayList<>();
        for (User user : votersList)
            user.addGeneralVoter(this);
        this.studentVoteList = new ArrayList<>();
        this.teacherVoteList = new ArrayList<>();
        this.employeeVoteList = new ArrayList<>();
        this.studentCandidateList.add(new CandidateList("Null Vote"));
        this.studentCandidateList.add(new CandidateList("Blank Vote"));
        this.teacherCandidateList.add(new CandidateList("Null Vote"));
        this.teacherCandidateList.add(new CandidateList("Blank Vote"));
        this.employeeCandidateList.add(new CandidateList("Null Vote"));
        this.employeeCandidateList.add(new CandidateList("Blank Vote"));
    }

    public void removeVoter(Vote vote) {
        vote.getVoter().removeGeneralVoter(this, vote);
    }

    public void addVote(Vote vote) {
        vote.getVoter().addGeneralVote(this, vote);
    }

    void addStudentVote(Vote vote) {
        this.studentVoteList.add(vote);
    }

    void addTeacherVote(Vote vote) {
        this.teacherVoteList.add(vote);
    }

    void addEmployeeVote(Vote vote) {
        this.employeeVoteList.add(vote);
    }

    void removeStudentVoter(Vote vote) {
        this.studentVoteList.add(vote);
    }

    void removeTeacherVoter(Vote vote) {
        this.teacherVoteList.add(vote);
    }

    void removeEmployeeVoter(Vote vote) {
        this.employeeVoteList.add(vote);
    }

    void addStudent(Student student){
        this.studentList.add(student);
    }

    void addTeacher(Teacher teacher){
        this.teacherList.add(teacher);
    }

    void addEmployee(Employee employee){
        this.employeeList.add(employee);
    }

    ArrayList<CandidateList> getStudentCandidateList() {
        return studentCandidateList;
    }

    ArrayList<CandidateList> getTeacherCandidateList() {
        return teacherCandidateList;
    }

    ArrayList<CandidateList> getEmployeeCandidateList() {
        return employeeCandidateList;
    }

    @Override
    public ArrayList<User> getVotersList(){
        ArrayList<User> list = new ArrayList<>();
        list.addAll(this.studentList);
        list.addAll(this.teacherList);
        list.addAll(this.employeeList);
        return list;
    }

    @Override
    public ArrayList<CandidateList> getCandidateList(User user) {
        return user.getGeneralCandidateList(this);
    }

    private void editEmployeeCandidates(){
        System.out.print("[1]Add List\n[2]Remove List");
        Scanner sc = new Scanner(System.in);
        String op = sc.nextLine();
        if (op.equals("1")){
            System.out.println("Enter a name for the list:");
            String name = sc.nextLine();
            CandidateList newList = new CandidateList(name);
            this.employeeCandidateList.add(newList);
        }
        else if (op.equals("2")){
            if(this.employeeCandidateList.isEmpty())
                return;
            System.out.println("Choose a list to remove:");
            int i = 1;
            for (CandidateList dep: this.employeeCandidateList){
                System.out.println(i + " - " + dep.getName());
                i++;
            }
            String option;
            option = sc.nextLine();
            int optionInt;
            do{
                try {
                    optionInt= Integer.parseInt(option);
                } catch (NumberFormatException e) {
                    optionInt = 0;
                }
            }while(optionInt <= 0 || optionInt > this.employeeCandidateList.size());
            this.employeeCandidateList.remove(optionInt - 1);
        }
    }

    private void editStudentCandidates(){
        System.out.print("[1]Add List\n[2]Remove List");
        Scanner sc = new Scanner(System.in);
        String op = sc.nextLine();
        if (op.equals("1")){
            System.out.println("Enter a name for the list:");
            String name = sc.nextLine();
            CandidateList newList = new CandidateList(name);
            this.studentCandidateList.add(newList);
        }
        else if (op.equals("2")){
            if(this.studentCandidateList.isEmpty())
                return;
            System.out.println("Choose a list to remove:");
            int i = 1;
            for (CandidateList dep: this.studentCandidateList){
                System.out.println(i + " - " + dep.getName());
                i++;
            }
            String option;
            option = sc.nextLine();
            int optionInt;
            do{
                try {
                    optionInt= Integer.parseInt(option);
                } catch (NumberFormatException e) {
                    optionInt= 0;
                }
            }while(optionInt <= 0 || optionInt > this.studentCandidateList.size());
            this.studentCandidateList.remove(optionInt - 1);
        }
    }

    private void editTeacherCandidates(){
        System.out.print("[1]Add List\n[2]Remove List");
        Scanner sc = new Scanner(System.in);
        String op = sc.nextLine();
        if (op.equals("1")){
            System.out.println("Enter a name for the list:");
            String name = sc.nextLine();
            CandidateList newList = new CandidateList(name);
            this.teacherCandidateList.add(newList);
        }
        else if (op.equals("2")){
            if(this.teacherCandidateList.isEmpty())
                return;
            System.out.println("Choose a list to remove");
            int i = 1;
            for (CandidateList dep: this.teacherCandidateList){
                System.out.println(i + " - " + dep.getName());
                i++;
            }
            String option;
            option = sc.nextLine();
            int optionInt;
            do{
                try {
                    optionInt = Integer.parseInt(option);
                } catch (NumberFormatException e) {
                    optionInt = 0;
                }
            }while(optionInt <= 0 || optionInt > this.teacherCandidateList.size());
            this.teacherCandidateList.remove(optionInt - 1);
        }
    }

    public void editCandidates(){
        System.out.println("Candidate Lists of : \n1 - Employees\n2 - Teachers\n3 - Students");
        Scanner sc = new Scanner(System.in);
        int op = sc.nextInt();
        switch (op) {
            case 1:
                editEmployeeCandidates();
                break;
            case 2:
                editTeacherCandidates();
                break;
            case 3:
                editStudentCandidates();
                break;
        }
    }

    public void print() {
        System.out.println(title);
        System.out.println(description);
        System.out.println(start.getTime());
        System.out.println(end.getTime());
        System.out.println("EMPLOYEES");
        for (CandidateList list: employeeCandidateList){
            System.out.println(list.getName());
            int count = 0;
            for (Vote vote : employeeVoteList){
                if (vote.getType().getName().equals(list.getName()))
                    count++;
            }
            System.out.println("Number of votes: "+count);
        }
        System.out.println("TEACHERS");
        for (CandidateList list: teacherCandidateList){
            System.out.println(list.getName());
            int count = 0;
            for (Vote vote : teacherVoteList){
                if (vote.getType().getName().equals(list.getName()))
                    count++;
            }
            System.out.println("Number of votes: "+count);
        }
        System.out.println("STUDENTS");
        for (CandidateList list: studentCandidateList){
            System.out.println(list.getName());
            int count = 0;
            for (Vote vote : studentVoteList){
                if (vote.getType().getName().equals(list.getName()))
                    count++;
            }
            System.out.println("Number of votes: "+count);
        }
    }


    @Override
    public void currentNumberOfVotes() {
        System.out.println("(Students)Number of Votes(in real time): " + this.studentVoteList.size());
        System.out.println("(Teachers)Number of Votes(in real time): " + this.teacherVoteList.size());
        System.out.println("(Employees)Number of Votes(in real time): " + this.employeeVoteList.size());
    }

    @Override
    public void votingPlace(String uc) {
        System.out.println(this.getTitle());
        int v = 0;
        for (Vote aux : studentVoteList)
            if (uc.equals(aux.getVoter().getIdCardNumber())) {
                System.out.print("(Students)");
                System.out.println("Name: "+aux.getVoter().getName());
                System.out.println("Place: "+aux.getPlace().getName());
                System.out.println("Time: "+aux.getVotingTime());
                v = 1;
            }
        if (v == 0)
            for (Vote aux : teacherVoteList)
                if (uc.equals(aux.getVoter().getIdCardNumber())) {
                    System.out.print("(Teachers)");
                    System.out.println("Name: "+aux.getVoter().getName());
                    System.out.println("Place: "+aux.getPlace().getName());
                    System.out.println("Time: "+aux.getVotingTime());
                    v = 1;
                }
        if (v == 0)
            for (Vote aux : employeeVoteList)
                if (uc.equals(aux.getVoter().getIdCardNumber())) {
                    System.out.print("(Employees)");
                    System.out.println("Name: "+aux.getVoter().getName());
                    System.out.println("Place: "+aux.getPlace().getName());
                    System.out.println("Time: "+aux.getVotingTime());
                }
    }
}