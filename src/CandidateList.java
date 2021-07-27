import java.io.Serializable;

public class CandidateList implements Serializable {

    private String name;

    public CandidateList() {
    }

    public CandidateList(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
