package epsilveira.league.persistence;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private String name;

    public enum Region {
        NA, EUW, EUNE, LAN, LAS, BR, JP, RU, TR, OCE, KR
    }

    @Enumerated(EnumType.STRING)
    private Region region;

    @OneToMany(mappedBy="team")
    private Set<Player> players = new HashSet<Player>();

    @OneToMany (mappedBy = "blueTeam", cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private Set<Contest> matchesBlue = new HashSet<>();

    @OneToMany (mappedBy = "redTeam", cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private Set<Contest> matchesRed = new HashSet<>();

    @OneToOne (mappedBy = "team")
    private Coach coach;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Set<Player> getPlayers() {
        return Collections.unmodifiableSet(players);
    }

    public void addPlayer(Player player, Player.Position position) {
        player.setTeam(this);
        player.setPosition(position);
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        if (this.coach != null) {
            this.coach.setTeam(null);
        }
        if (coach != null) {
            coach.setTeam(this);
        }
    }

    public void internalSetCoach(Coach coach) {
        this.coach = coach;
    }

    public Set<Contest> getContests(){
        Set<Contest> toRet = new HashSet<>();
        toRet.addAll(matchesBlue);
        toRet.addAll(matchesRed);
        return Collections.unmodifiableSet(toRet);
    }

}
