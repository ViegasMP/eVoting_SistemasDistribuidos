import java.io.Serializable;
import java.util.ArrayList;



public abstract class User implements Serializable {

    private String name;
    private String phoneNumber;
    private String address;
    private String password;
    private Department Department;
    private String idCardNumber;
    private String expirationDate;
    static final long serialVersionUID = -5366253201646914413L;

    protected User(String name, String phoneNumber, String address, String password, Department department, String idCardNumber, String expirationDate) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.password = password;
        Department = department;
        this.idCardNumber = idCardNumber;
        this.expirationDate = expirationDate;
    }

    public String getName() {
        return name;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public Department getDepartment() {
        return Department;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public abstract void addUserToList(ArrayList<User> list, Department dep);

    public abstract void addGeneralVoter(GeneralCounsel generalCounsel);

    public abstract void removeGeneralVoter(GeneralCounsel generalCounsel, Vote vote);

    public abstract void addGeneralVote(GeneralCounsel generalCounsel, Vote vote);

    public abstract ArrayList<CandidateList> getGeneralCandidateList(GeneralCounsel generalCounsel);

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDepartment(Department department) {
        Department = department;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
