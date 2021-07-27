import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Vote implements Serializable {

    private User voter;

    private CandidateList voteType;

    private Calendar voteTime;

    private Department place;

    public Vote(User voter, CandidateList type, Department place) {
        this.voter = voter;
        this.voteType = type;
        this.voteTime = new GregorianCalendar();
        this.place = place;
    }

    public User getVoter() {
        return voter;
    }

    public CandidateList getType() {
        return voteType;
    }

    public Calendar getVotingTime() {
        return voteTime;
    }

    public Department getPlace() {
        return place;
    }
}
