package epsilveira.league.persistence;

import javax.persistence.Entity;

@Entity
public class Coach extends Member{

    private int couchingStart;

    public int getCouchingStart() {
        return couchingStart;
    }

    public void setCouchingStart(int couchingStart) {
        this.couchingStart = couchingStart;
    }
}
