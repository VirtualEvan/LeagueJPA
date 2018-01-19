package epsilveira.league.persistence;

import javax.persistence.EntityManager;
import java.util.List;

public class Contests {
    private EntityManager em;

    public Contests(EntityManager em) {
        this.em = em;
    }

    public List<Contest> findAll() {
        return this.em.createQuery("SELECT match FROM Contest match").getResultList();
    }

    public void addContest(Contest contest) {
        this.em.persist(contest);
    }

    public Contest findById(int id) {
        return em.find(Contest.class, id);
    }

    public void deleteContest(Contest contest) {
        this.em.remove(contest);
    }
}
