import java.io.Serializable;
import java.util.*;

public class Department implements Serializable{

    private String name;

    private ArrayList<Student> studentList;

    static final long serialVersionUID = 8931795498210708348L;


    public Department(String name, ArrayList<Student> studentList) {
        this.name = name;
        this.studentList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    ArrayList<Student> getStudentList() {
        return this.studentList;
    }

    public void setStudentList(ArrayList<Student> studentList) {
        studentList = studentList;
    }

    void addStudent(Student student) {
        studentList.add(student);
    }


}
