package epsilveira.league.persistence;

import static epsilveira.league.persistence.Player.Position.*;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import org.junit.Test;

public class PlayersTest extends SQLBasedTest {

    @Test
    public void testFindPlayerById() throws SQLException {
        // Insert Player
        int playerId = insertPlayer("Dyrus");

        EntityManager em = emf.createEntityManager();

        // Test if player can be found by id
        Players players = new Players(em);
        Player player = players.findById(playerId);
        assertEquals(playerId, player.getId());
    }

    @Test
    public void testAddPlayer() throws SQLException {
        // Existing team
        int teamId = insertTeam();

        EntityManager em = emf.createEntityManager();

        Teams teams = new Teams(em);
        Team team = teams.findById(teamId);

        Player player = new Player();
        Players players = new Players(em);

        em.getTransaction().begin();

            player.setName("Bjergsen");
            player.setBirthYear(1994);
            player.setPosition(mid);
            player.setTeam(team);

            players.addPlayer(player);

        em.getTransaction().commit();

        // Ensure that bi-directional relation is always consistent
        assertEquals(1, team.getPlayers().size());

        int playerId = player.getId();

        Statement statement = jdbcConnection.createStatement();
        ResultSet res = statement.executeQuery("SELECT * from Player where id = " + playerId);
        res.next();
        assertEquals(teamId, res.getInt("team_id"));
        assertEquals("Bjergsen", res.getString("name"));

    }

    @Test
    public void testUpdatePlayer() throws SQLException {
        // Existing player
        int playerId = insertPlayer("Dyrus");

        EntityManager em = emf.createEntityManager();
        Players players = new Players(em);
        Player player = players.findById(playerId);


        em.getTransaction().begin();
            player.setName("Doublelift");
        em.getTransaction().commit();

        Statement statement = jdbcConnection.createStatement();
        ResultSet res = statement.executeQuery("SELECT * from Player where id = " + playerId);
        res.next();
        assertEquals("Doublelift", res.getString("name"));

    }

    @Test
    public void testDeletePlayer() throws SQLException {

        // Insert Player
        int playerId = insertPlayer("Dyrus");

        // Insert team
        int teamId = insertTeam();

        EntityManager em = emf.createEntityManager();

        // Test if player can be found by id
        Players players = new Players(em);
        Player player = players.findById(playerId);
        assertEquals(playerId, player.getId());

        Teams teams = new Teams(em);
        Team team = teams.findById(teamId);

        // Check if player is in team (team-side)
        em.getTransaction().begin();
            player.setTeam(team);
            players.deletePlayer(player);
        em.getTransaction().commit();

        assertEquals(0, team.getPlayers().size());

        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Player player where player.id = " + playerId);
        rs.next();
        //Check if the player was totally removed from table
        assertEquals(0, rs.getInt("total"));
        assertEquals(0, team.getPlayers().size());
    }

    private int insertTeam() throws SQLException {
        Statement statement = jdbcConnection.createStatement();
        statement.executeUpdate("INSERT INTO Team(name, region) values('TSM', 'NA')", Statement.RETURN_GENERATED_KEYS);
        return getLastInsertedId(statement);
    }

    private int insertPlayer( String playerName ) throws SQLException {
        Statement statement = jdbcConnection.createStatement();
        statement.executeUpdate("INSERT INTO Player(name, birthYear) values( '" + playerName + "', 1994)", Statement.RETURN_GENERATED_KEYS);
        return getLastInsertedId(statement);
    }

}
