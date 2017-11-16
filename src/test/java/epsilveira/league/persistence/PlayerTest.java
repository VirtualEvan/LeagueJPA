package epsilveira.league.persistence;

import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.BeforeClass;
import org.junit.Test;

public class PlayerTest extends SQLBasedTest {

    private static EntityManagerFactory emf;

    @BeforeClass
    public static void setUpEntityManagerFactory() {
        emf = Persistence.createEntityManagerFactory("si-database");
    }

    @Test
    public void testCreatePlayer() throws Exception {
        EntityManager em = emf.createEntityManager();

        Player p = new Player();
        p.setName("Bjergsen");
        p.setBirthYear(1990);

        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();

        // check in the DB using JDBC
        int playerId = p.getId();


        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Player p where p.id = " + playerId);
        rs.next();

        assertEquals(1, rs.getInt("total"));
    }

    @Test
    public void testFindPlayer() throws SQLException {
        // insert a player previously with JDBC
        Statement statement = jdbcConnection.createStatement();
        statement.executeUpdate("INSERT INTO Player(name, birthYear) values('Bjergsen', 1990)", Statement.RETURN_GENERATED_KEYS);
        int playerId = getLastInsertedId(statement);


        EntityManager em = emf.createEntityManager();

        Player p = em.find(Player.class, playerId);

        assertEquals(playerId, p.getId());
        assertEquals("Bjergsen", p.getName());
    }
}
