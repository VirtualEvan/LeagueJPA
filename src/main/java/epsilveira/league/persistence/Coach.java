package epsilveira.league.persistence;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Coach extends Member{

    private int couchingStart;

    @OneToOne
    private Team team;

    public int getCouchingStart() {
        return couchingStart;
    }

    public void setCouchingStart(int couchingStart) {
        this.couchingStart = couchingStart;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        if (this.team != null) {
            this.team.internalSetCoach(null);
        }
        this.team = team;
        if (this.team != null) {
            if(this.team.getCoach() != null){
                this.team.getCoach().internalSetTeam(null);
            }
            this.team.internalSetCoach(this);
        }
    }
    public void internalSetTeam(Team team){
        this.team = team;
    }
}
