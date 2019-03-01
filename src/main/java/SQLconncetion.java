import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class SQLconncetion {
    private static String URL, username, password;
    private static Statement statement;
    private static Logger logger = LogManager.getLogger(SQLconncetion.class);

    public SQLconncetion(String URL, String username, String password) {
        this.URL = URL;
        this.username = username;
        this.password = password;

        try {
            Connection connection = DriverManager.getConnection(URL, username, password);
            statement = connection.createStatement();
        } catch (SQLException e) {
            logger.error("SQLException: ", e);
        }
    }

    public Player getScoreBySteam(String steamid) throws SQLException {
        ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM rankme where steam = '%s';", steamid));
        while (resultSet.next()) {
            Player player = new Player();
            player.setSteamid(steamid);
            player.setName(resultSet.getString("name"));
            player.setScore(resultSet.getBigDecimal("score").toBigInteger());
            player.setKils(resultSet.getBigDecimal("kills").toBigInteger());
            player.setDeaths(resultSet.getBigDecimal("deaths").toBigInteger());
            player.setAssists(resultSet.getBigDecimal("assists").toBigInteger());
            player.setShots(resultSet.getBigDecimal("shots").toBigInteger());
            player.setHits(resultSet.getBigDecimal("hits").toBigInteger());
            player.setHeadshots(resultSet.getBigDecimal("headshots").toBigInteger());
            return player;
        }
        return null;
    }
}