package epsilveira.league.persistence;

import org.junit.Test;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class TeamsTest extends SQLBasedTest{

    @Test
    public void testAddTeam() throws SQLException {
        Team team = new Team();
        team.setName("Moscow 5");
        team.setRegion(Team.Region.RU);


        EntityManager em = emf.createEntityManager();
        Teams teams = new Teams(em);
        em.getTransaction().begin();
            teams.addTeam(team);
        em.getTransaction().commit();

        // Check if team was added to the DB using JDBC
        int teamId = team.getId();
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Team team where team.id = " + teamId);
        rs.next();

        assertEquals(1, rs.getInt("total"));
    }

    @Test
    public void testFindTeamById() throws SQLException {
        // Insert a Team previously with JDBC
        int teamId = insertTeam("SKT", Team.Region.KR);

        EntityManager em = emf.createEntityManager();

        Team team = em.find(Team.class, teamId);

        assertEquals(teamId, team.getId());
        assertEquals("SKT", team.getName());
        assertEquals(Team.Region.KR, team.getRegion());
    }

    @Test
    public void testUpdateTeam() throws SQLException {
        // Insert the team to be modified
        int teamId = insertTeam("C9", Team.Region.NA);

        EntityManager em = emf.createEntityManager();
        Team team = em.find(Team.class, teamId);

        em.getTransaction().begin();
            team.setName("Cloud 9");
        em.getTransaction().commit();

        // Check if team was updated in the DB using JDBC
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM Team WHERE Team.id = " + teamId);
        rs.next();
        assertEquals("Cloud 9", rs.getString("name"));

    }

    @Test
    public void testDeleteTeam() throws SQLException {
        // Insert the team to be removed
        int teamId = insertTeam("TSM", Team.Region.NA);

        // Insert related Player and Coach
        insertPlayerInTeam("Dyrus", 1994, teamId, Player.Position.top);
        insertPlayerInTeam("TheOddOne", 1993, teamId, Player.Position.jungle);
        insertCoachInTeam("Locodoco", 2015, teamId);
        EntityManager em = emf.createEntityManager();

        Teams teams = new Teams(em);
        Team team = teams.findById(teamId);

        em.getTransaction().begin();
            // Remove team
            teams.deleteTeam(team);
        em.getTransaction().commit();



        // Check in the DB using JDBC
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Team WHERE Team.id = " + teamId);
        rs.next();
        assertEquals(0, rs.getInt("total"));
    }

    @Test
    public void testFindAllTeams() throws SQLException {
        // Database cleanse
        Statement statement = jdbcConnection.createStatement();

        statement.executeUpdate("UPDATE Player set team_id = NULL WHERE team_id IS NOT NULL", Statement.RETURN_GENERATED_KEYS);
        statement.executeUpdate("UPDATE Coach set team_id = NULL WHERE team_id IS NOT NULL", Statement.RETURN_GENERATED_KEYS);
        statement.executeUpdate("DELETE FROM Team", Statement.RETURN_GENERATED_KEYS);


        insertTeam("CLG", Team.Region.NA);
        insertTeam("G2", Team.Region.EUW);

        EntityManager em = emf.createEntityManager();
        List<Team> teams = em.createQuery("SELECT teams FROM Team teams").getResultList();

        assertEquals(2, teams.size());

        Set<String> namesToTest = new HashSet(Arrays.asList("CLG", "G2"));
        for (Team team: teams) {
            namesToTest.remove(team.getName());
        }

        assertTrue(namesToTest.isEmpty());
    }

    private int insertTeam(String name, Team.Region region) throws SQLException {
        Statement statement = jdbcConnection.createStatement();
        statement.executeUpdate("INSERT INTO Team(name, region) values('" + name + "', '" + region +"')", Statement.RETURN_GENERATED_KEYS);

        return getLastInsertedId(statement);
    }

    private int insertPlayer(String name, int birthYear) throws SQLException {
        Statement statement = jdbcConnection.createStatement();
        statement.executeUpdate("INSERT INTO Player(name, birthYear) values('" + name + "', " + birthYear + ")", Statement.RETURN_GENERATED_KEYS);
        return getLastInsertedId(statement);
    }

    private int insertPlayerInTeam(String name, int birthYear, int team_id, Player.Position position) throws SQLException {
        Statement statement = jdbcConnection.createStatement();
        statement.executeUpdate("INSERT INTO Player(name, birthYear, team_id, position) values('" + name + "', " + birthYear + ", " + team_id + ", '" + position + "')", Statement.RETURN_GENERATED_KEYS);
        return getLastInsertedId(statement);
    }

    private int insertCoach(String name, int couchingStart) throws SQLException {
        Statement statement = jdbcConnection.createStatement();
        statement.executeUpdate("INSERT INTO Coach(name, couchingStart) values('" + name + "', " + couchingStart + ")", Statement.RETURN_GENERATED_KEYS);
        return getLastInsertedId(statement);
    }

    private int insertCoachInTeam(String name, int couchingStart, int team_id) throws SQLException {
        Statement statement = jdbcConnection.createStatement();
        statement.executeUpdate("INSERT INTO Coach(name, couchingStart, team_id) values('" + name + "', " + couchingStart + ", " + team_id + ")", Statement.RETURN_GENERATED_KEYS);
        return getLastInsertedId(statement);
    }

}
