
import java.io.Serializable;
import java.util.ArrayList;

public class Student extends User implements Serializable {

    public Student(String name, String phoneNumber, String address, String password, Department department, String idCardNumber, String expirationDate) {
        super(name, phoneNumber, address, password, department, idCardNumber, expirationDate);
    }

    public void addUserToList(ArrayList<User> list, Department dep) {
        dep.addStudent(this);
        list.add(this);
    }

    public void addGeneralVoter(GeneralCounsel generalCounsel) {

    }

    public void removeGeneralVoter(GeneralCounsel generalCounsel, Vote vote) {

    }

    public void addGeneralVote(GeneralCounsel generalCounsel, Vote vote) {

    }

    public ArrayList<CandidateList> getGeneralCandidateList(GeneralCounsel generalCounsel) {
        return null;
    }


    public String toString() {
        return "Student{}";
    }

}
