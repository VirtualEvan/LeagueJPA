package epsilveira.league.persistence;

import javax.persistence.EntityManager;
import java.util.List;

public class Coaches {

    private EntityManager em;

    public Coaches(EntityManager em) {
        this.em = em;
    }

    public List<Coach> findAll() {
        return this.em.createQuery("SELECT coach FROM Coach coach").getResultList();
    }

    public void addCoach(Coach coach) {
        this.em.persist(coach);
    }

    public Coach findById(int id) {
        return em.find(Coach.class, id);
    }

    public void deleteCoach(Coach coach) {
        this.em.remove(coach);
    }
}
