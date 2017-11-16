package epsilveira.league.persistence;

import javax.persistence.*;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private String name;

    private String region;

    private Player top;
    private Player mid;
    private Player jungle;
    private Player bot;
    private Player support;
    private Player coach;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @OneToOne
    public Player getTop() {
        return top;
    }

    public void setTop(Player top) {
        this.top = top;
    }

    @OneToOne
    public Player getMid() {
        return mid;
    }

    public void setMid(Player mid) {
        this.mid = mid;
    }

    @OneToOne
    public Player getJungle() {
        return jungle;
    }

    public void setJungle(Player jungle) {
        this.jungle = jungle;
    }

    @OneToOne
    public Player getBot() {
        return bot;
    }

    public void setBot(Player bot) {
        this.bot = bot;
    }

    @OneToOne
    public Player getSupport() {
        return support;
    }

    public void setSupport(Player support) {
        this.support = support;
    }

    @OneToOne
    public Player getCoach() {
        return coach;
    }

    public void setCoach(Player coach) {
        this.coach = coach;
    }
}
