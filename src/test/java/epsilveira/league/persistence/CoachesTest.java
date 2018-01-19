package epsilveira.league.persistence;

import org.junit.Test;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;

public class CoachesTest extends SQLBasedTest {

    @Test
    public void testFindCoachById() throws SQLException {
        // Insert Player
        int coachId = insertCoach("Locodoco", 2014);

        EntityManager em = emf.createEntityManager();

        // Test if player can be found by id
        Coaches coaches = new Coaches(em);
        Coach coach = coaches.findById(coachId);
        assertEquals(coachId, coach.getId());
    }

    @Test
    public void testAddCoach() throws SQLException {
        // Existing team
        int teamId = insertTeam("Origen", Team.Region.EUW);

        EntityManager em = emf.createEntityManager();

        Teams teams = new Teams(em);
        Team team = teams.findById(teamId);

        Coach coach = new Coach();
        Coaches coaches = new Coaches(em);

        em.getTransaction().begin();

            coach.setName("Araneae");
            coach.setCouchingStart(2015);
            coach.setTeam(team);

            coaches.addCoach(coach);

        em.getTransaction().commit();

        // Ensure that bi-directional relation is always consistent
        assertEquals(coach, team.getCoach());

        int coachId = coach.getId();

        Statement statement = jdbcConnection.createStatement();
        ResultSet res = statement.executeQuery("SELECT * from Coach where id = " + coachId);
        res.next();
        assertEquals(teamId, res.getInt("team_id"));
        assertEquals("Araneae", res.getString("name"));

    }

    @Test
    public void testUpdateCoach() throws SQLException {
        // Existing coach
        int coachId = insertCoach("Dailor", 2017);

        EntityManager em = emf.createEntityManager();
        Coaches coaches = new Coaches(em);
        Coach coach = coaches.findById(coachId);


        em.getTransaction().begin();
            coach.setName("Deilor");
        em.getTransaction().commit();

        Statement statement = jdbcConnection.createStatement();
        ResultSet res = statement.executeQuery("SELECT * from Coach where id = " + coachId);
        res.next();
        assertEquals("Deilor", res.getString("name"));

    }

    @Test
    public void testDeleteCoach() throws SQLException {

        // Insert Player
        int coachId = insertCoach("SmithZz", 2016);

        // Insert team
        int teamId = insertTeam("G2", Team.Region.EUW);

        EntityManager em = emf.createEntityManager();

        // Test if player can be found by id
        Coaches coaches = new Coaches(em);
        Coach coach = coaches.findById(coachId);
        assertEquals(coachId, coach.getId());

        Teams teams = new Teams(em);
        Team team = teams.findById(teamId);

        // Check if player is in team (team-side)
        em.getTransaction().begin();
            coach.setTeam(team);
            coaches.deleteCoach(coach);
        em.getTransaction().commit();

        assertEquals(0, team.getPlayers().size());

        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Coach  where id = " + coachId);
        rs.next();
        //Check if the player was totally removed from table
        assertEquals(0, rs.getInt("total"));
        assertEquals(0, team.getPlayers().size());
    }

    private int insertTeam(String name, Team.Region region) throws SQLException {
        Statement statement = jdbcConnection.createStatement();
        statement.executeUpdate("INSERT INTO Team(name, region) values( '" + name + "', '" + region + "' )", Statement.RETURN_GENERATED_KEYS);
        return getLastInsertedId(statement);
    }

    private int insertCoach( String coachName, int couchingStart ) throws SQLException {
        Statement statement = jdbcConnection.createStatement();
        statement.executeUpdate("INSERT INTO Coach(name, couchingStart) values( '" + coachName + "', " + couchingStart + ")", Statement.RETURN_GENERATED_KEYS);
        return getLastInsertedId(statement);
    }

}
