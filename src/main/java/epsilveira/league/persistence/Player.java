package epsilveira.league.persistence;

import javax.persistence.*;

@Entity
@Table(name="Player", uniqueConstraints={
        @UniqueConstraint(columnNames = {"team_id", "position"})
})
public class Player extends Member{

    private int birthYear;

    public enum Position {
        top, mid, jungle, bot, support
    }

    @ManyToOne
    private Team team;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Enumerated(EnumType.STRING)
    private Position position;

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}