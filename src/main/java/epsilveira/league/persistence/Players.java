package epsilveira.league.persistence;

import java.util.List;

import javax.persistence.EntityManager;

public class Players {

    private EntityManager em;

    public Players(EntityManager em) {
        this.em = em;
    }

    public List<Player> findAll() {
        return this.em.createQuery("SELECT e FROM Player e").getResultList();
    }

    public void addPlayer(Player e) {
        this.em.persist(e);
    }

    public Player findById(int id) {
        return em.find(Player.class, id);
    }

    public void deletePlayer(Player e) {
        this.em.remove(e);
    }
}
