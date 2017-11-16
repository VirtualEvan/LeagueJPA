package epsilveira.league.persistence;

import javax.persistence.Entity;

@Entity
public class Player extends Member{

    private int birthYear;


    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }
}