package epsilveira.league.persistence;

import java.util.List;

import javax.persistence.EntityManager;

public class Teams {

    private EntityManager em;

    public Teams(EntityManager em) {
        this.em = em;
    }

    public void addTeam(Team d) {
        em.persist(d);
    }

    public Team findById(int id) {
        return em.find(Team.class, id);
    }

    public void deleteTeam(Team team) {
        // Unassign players and coach of this team to preserve DB integrity
        for (Player player: team.getPlayers()) {
            player.setTeam(null);
        }
        Coach coach = team.getCoach();
        if(coach != null)
            coach.setTeam(null);

        em.remove(team);
    }

    public List<Team> findAll() {
        return em.createQuery("SELECT teams FROM Team teams").getResultList();
    }



}
