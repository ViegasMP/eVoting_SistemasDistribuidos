import java.io.Serializable;
import java.util.*;

/**
 * Classe abstrata que representa uma eleição.
 */
public abstract class Election implements Serializable {
    String title;
    String description;
    Calendar start;
    Calendar end;
    static final long serialVersionUID = 793707694382665011L;

    Election(String title, String description, Calendar start, Calendar end) {
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    public abstract void removeVoter(Vote vote);

    public abstract void addVote(Vote vote);

    public abstract ArrayList<User> getVotersList();

    public abstract ArrayList<CandidateList> getCandidateList(User user);

    public void editCandidates(){}

    public void print(){}

    public abstract void currentNumberOfVotes();

    public abstract void votingPlace(String uc);

    public String toString() {
        return "Election{" +
                "Title='" + title + '\'' +
                ", Description='" + description + '\'' +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

    public Boolean electionEquals(Election ele){
        return ele.getTitle().equals(this.title) && ele.getDescription().equals(this.description) && ele.getStart().equals(this.start) && ele.getEnd().equals(this.end);
    }
    public boolean verifyVoting(){
        Calendar currentDate = Calendar.getInstance();
        System.out.println(currentDate.after(this.start));
        System.out.println(currentDate.before(this.end));
        return currentDate.after(this.start) && currentDate.before(this.end);
    }
    public boolean verifyVotingExpired(){
        Calendar currentDate = Calendar.getInstance();
        return !currentDate.after(this.end);
    }
}
