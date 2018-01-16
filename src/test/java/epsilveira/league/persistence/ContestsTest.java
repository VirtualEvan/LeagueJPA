package epsilveira.league.persistence;

import exception.MirrorMatchException;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.sql.Date;

import static org.junit.Assert.assertEquals;

public class ContestsTest extends SQLBasedTest{

    @Test
    public void testAddMatch() throws SQLException {

        // Existing team
        int blueTeamId = insertTeam("TSM", Team.Region.NA);
        int redTeamId = insertTeam("CLG", Team.Region.NA);
        String error = "";

        EntityManager em = emf.createEntityManager();

        Teams teams = new Teams(em);
        Team blueTeam = teams.findById(blueTeamId);
        Team redTeam = teams.findById(redTeamId);

        Contest contest = new Contest();
        Contests contests = new Contests(em);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DATE, 12);

        try {
            em.getTransaction().begin();

                contest.setBlueTeam(blueTeam);
                contest.setRedTeam(redTeam);
                contest.setSchedule(new Date(calendar.getTime().getTime()));

                contests.addContest(contest);

            em.getTransaction().commit();
        }
        catch (MirrorMatchException mme) {
            error = mme.getMessage();
        }


        // Ensure that bi-directional relation is always consistent
        assertEquals(1, blueTeam.getContests().size());

        int contestId = contest.getId();

        Statement statement = jdbcConnection.createStatement();
        ResultSet res = statement.executeQuery("SELECT * from Contest where id = " + contestId);
        res.next();
        assertEquals(redTeamId, res.getInt("redTeam_id"));
        assertEquals(blueTeamId, res.getInt("blueTeam_id"));
        assertEquals("2017-12-12", res.getString("schedule"));
        assertEquals("", error);
    }

    @Test
    public void testFindContestById() throws SQLException {
        int blueTeam = insertTeam("REG", Team.Region.NA);
        int redTeam = insertTeam("DIG", Team.Region.EUW);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2015);
        calendar.set(Calendar.MONTH, 4);
        calendar.set(Calendar.DATE, 20);
        Date schedule = new Date(calendar.getTime().getTime());

        // Insert Contest
        int contestId = insertContest(blueTeam, redTeam, schedule);

        EntityManager em = emf.createEntityManager();

        // Test if contest can be found by id
        Contests contests = new Contests(em);
        Contest contest = contests.findById(contestId);
        assertEquals(schedule.toString(), contest.getSchedule().toString());
    }

    @Test
    public void testDeleteContest() throws SQLException {
        // Insert contest to be removed
        // Existing team
        int blueTeamId = insertTeam("UOL", Team.Region.EUW);
        int redTeamId = insertTeam("SSW", Team.Region.KR);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2017);
        calendar.set(Calendar.MONTH, 12);
        calendar.set(Calendar.DATE, 12);

        int contestId = insertContest(blueTeamId, redTeamId, new Date(calendar.getTime().getTime()));

        EntityManager em = emf.createEntityManager();

        Contests contests = new Contests(em);
        Contest contest = contests.findById(contestId);

        em.getTransaction().begin();
        // Remove contest
        contests.deleteContest(contest);
        em.getTransaction().commit();



        // Check in the DB using JDBC
        Statement statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Contest WHERE Contest.id = " + contestId);
        rs.next();
        assertEquals(0, rs.getInt("total"));
    }


    @Test
    public void testUpdateContest() throws SQLException {
        int blueTeamOldId = insertTeam("CRS", Team.Region.NA);
        int blueTeamNewId = insertTeam("SIR", Team.Region.NA);
        int redTeamId = insertTeam("RFLX", Team.Region.EUW);
        String error = "";

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2016);
        calendar.set(Calendar.MONTH, 7);
        calendar.set(Calendar.DATE, 24);
        Date schedule = new Date(calendar.getTime().getTime());

        // Insert Contest
        int contestId = insertContest(blueTeamOldId, redTeamId, schedule);

        EntityManager em = emf.createEntityManager();

        // Get contest
        Contests contests = new Contests(em);
        Contest contest = contests.findById(contestId);

        //Get teams
        Teams teams = new Teams(em);
        Team blueTeamNew = teams.findById(blueTeamNewId);

        try {
            em.getTransaction().begin();

            // Change team
            contest.setBlueTeam(blueTeamNew);

            contests.addContest(contest);

            em.getTransaction().commit();
        }
        catch (MirrorMatchException mme) {
            error = mme.getMessage();
        }

        Statement statement = jdbcConnection.createStatement();
        ResultSet res = statement.executeQuery("SELECT * from Contest where id = " + contestId);
        res.next();
        assertEquals(blueTeamNewId, res.getInt("blueTeam_id"));
        assertEquals("", error);

    }



    private int insertTeam(String name, Team.Region region) throws SQLException {
        Statement statement = jdbcConnection.createStatement();
        statement.executeUpdate("INSERT INTO Team(name, region) values('" + name + "', '" + region + "')", Statement.RETURN_GENERATED_KEYS);
        return getLastInsertedId(statement);
    }

    private int insertContest(int blueTeam, int redTeam, Date schedule) throws SQLException {
        Statement statement = jdbcConnection.createStatement();
        statement.executeUpdate("INSERT INTO Contest(blueTeam_id, redTeam_id, schedule) values('" + blueTeam + "', '" + redTeam + "', '" + schedule + "')", Statement.RETURN_GENERATED_KEYS);
        return getLastInsertedId(statement);
    }
}
