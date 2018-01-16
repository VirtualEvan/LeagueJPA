package epsilveira.league.persistence;

import exception.MirrorMatchException;

import javax.persistence.*;
import java.sql.Date;


@Entity
@Table(name="Contest", uniqueConstraints={
        @UniqueConstraint(columnNames = {"blueTeam_id", "redTeam_id", "schedule"})
})
public class Contest {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private Date schedule;

    @ManyToOne
    private Team blueTeam;

    @ManyToOne
    private Team redTeam;


    public int getId() {
        return id;
    }

    public Date getSchedule() {
        return schedule;
    }

    public void setSchedule(Date schedule) {
        this.schedule = schedule;
    }

    public void setMatch(Team blueTeam, Team redTeam, Date schedule) throws MirrorMatchException {
        if(blueTeam == redTeam)
            throw new MirrorMatchException();

        this.blueTeam = blueTeam;
        this.redTeam = redTeam;
        this.schedule = schedule;
    }

    public Team getBlueTeam() {
        return blueTeam;
    }

    public void setBlueTeam(Team blueTeam) throws MirrorMatchException{
        if(blueTeam == this.redTeam)
            throw new MirrorMatchException();
        this.blueTeam = blueTeam;
    }

    public Team getRedTeam() {
        return redTeam;
    }

    public void setRedTeam(Team redTeam) throws MirrorMatchException {
        if(redTeam == this.blueTeam)
            throw new MirrorMatchException();
        this.redTeam = redTeam;
    }
}
