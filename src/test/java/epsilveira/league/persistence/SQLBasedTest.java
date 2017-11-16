package epsilveira.league.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLBasedTest {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/league";
    private static final String DB_USER = "leagueuser";
    private static final String DB_PASS = "leaguepass";
    protected static Connection jdbcConnection = createConnection();

    private static Connection createConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }

    }

    protected int getLastInsertedId(Statement statement) throws SQLException {
        ResultSet rs = statement.getGeneratedKeys();
        rs.next();

        return rs.getInt(1);
    }
}
