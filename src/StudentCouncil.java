import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class StudentCouncil extends Election implements Serializable {

    private Department department;
    private ArrayList<User> votersList;

    private ArrayList<Vote> voteList;

    private ArrayList<CandidateList> candidateList;

    static final long serialVersionUID = -1066285098671505096L;



    public StudentCouncil(String title, String description, Calendar start, Calendar end, Department department, ArrayList<CandidateList> candidateList) {
        super(title, description, start, end);
        this.department = department;
        this.candidateList = candidateList;
        this.voteList = new ArrayList<>();
        this.votersList = new ArrayList<>(department.getStudentList());
        this.candidateList.add(new CandidateList("Null Vote"));
        this.candidateList.add(new CandidateList("Blank Vote"));
    }
    public void removeVoter(Vote vote) {
        for (User p : votersList)
            if (p.getIdCardNumber().equals(vote.getVoter().getIdCardNumber())) {
                this.votersList.remove(p);
                return;
            }
    }

    public void addVote(Vote vote) {
        this.voteList.add(vote);
    }

    public ArrayList<User> getVotersList(){
        ArrayList<User> list = new ArrayList<>();
        list.addAll(this.department.getStudentList());
        return list;
    }

    public ArrayList<CandidateList> getCandidateList(User user) {
        return candidateList;
    }


    public ArrayList<CandidateList> getCandidateList() {
        return candidateList;
    }

    public void editCandidates(){
        System.out.print("1 - Add Candidate\n2 - Remove Candidate\n");
        Scanner sc= new Scanner(System.in);
        String op=sc.nextLine();
        if (op.equals("1")){
            System.out.println("Enter a name for the list:");
            String name = sc.nextLine();
            CandidateList newList = new CandidateList(name);
            this.candidateList.add(newList);
        }
        else if (op.equals("2")){
            if(this.candidateList.isEmpty())
                return;
            System.out.println("Choose a candidate to remove");
            int i=0;
            for (CandidateList dep: this.candidateList){
                i++;
                System.out.println(i+" - "+dep.getName());
            }
            String option;
            option=sc.nextLine();
            int optionInt;
            do{
                try {
                    optionInt= Integer.parseInt(option);
                } catch (NumberFormatException e) {
                    optionInt= 0;
                }
            }while(optionInt<=0 || optionInt>this.candidateList.size());
            this.candidateList.remove(optionInt-1);
        }
    }

    public void print() {
        System.out.println(title);
        System.out.println(description);
        System.out.println(start.getTime());
        System.out.println(end.getTime());
        for (CandidateList list: candidateList){
            System.out.println(list.getName());
            int count = 0;
            for (Vote vote : voteList){
                if (vote.getType().getName().equals(list.getName()))
                    count++;
            }
            System.out.println("Number of votes: "+count);
        }
    }

    public void currentNumberOfVotes() {
        System.out.println("Number of votes(in real time): "+ this.voteList.size());
    }

    public void votingPlace(String uc) {
        System.out.println(this.getTitle());
        for (Vote aux : voteList)
            if (uc.equals(aux.getVoter().getIdCardNumber())) {
                System.out.println("Name: "+aux.getVoter().getName());
                System.out.println("Place: "+aux.getPlace().getName());
                System.out.println("Hour: "+aux.getVotingTime());
            }
    }

    public String toString() {
        return "Student Council{" +
                "Department=" + department +
                ", voterList=" + votersList +
                ", voteList=" + voteList +
                ", candidateList=" + candidateList +
                ", Title='" + title + '\'' +
                ", Description='" + description + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
