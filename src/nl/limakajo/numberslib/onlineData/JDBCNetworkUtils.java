package nl.limakajo.numberslib.onlineData;

import nl.limakajo.numberslib.numbersGame.Level;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class JDBCNetworkUtils {

    private static final String TAG = JDBCNetworkUtils.class.getName();

    public static LinkedList<Level> queryLevels(String tableName) {
        LinkedList<Level> toReturn= new LinkedList<>();
        String SQL = "SELECT * FROM " + tableName;
        switch (tableName) {
            case NumbersContract.LevelData.TABLE_NAME:
                try {
                    Connection connection = getConnection();
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(SQL);
                    while (resultSet.next()) {
                        String numbersString = resultSet.getString(2);
                        int averageTime = Integer.parseInt(resultSet.getString(3));
                        int timesPlayed = Integer.parseInt(resultSet.getString(4));
                        Level levelToAdd = new Level.LevelBuilder(numbersString)
                                .setAverageTime(averageTime)
                                .setTimesPlayed(timesPlayed)
                                .build();
                        toReturn.add(levelToAdd);
                    }
                    resultSet.close();
                    statement.close();
                }
                catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case NumbersContract.CompletedLevelData.TABLE_NAME:
                try {
                    Connection connection = getConnection();
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(SQL);
                    while (resultSet.next()) {
                        String numbersString = resultSet.getString(2);
                        int userTime = Integer.parseInt(resultSet.getString(3));
                        Level levelToAdd = new Level.LevelBuilder(numbersString)
                                .setUserTime(userTime)
                                .build();
                        toReturn.add(levelToAdd);
                    }
                    resultSet.close();
                    statement.close();
                }
                catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                break;
            default:
                throw new UnsupportedOperationException("Unsupported tableName: " + tableName);
        }
        return toReturn;
    }

    public static LinkedList<Level> insertLevels(String tableName, LinkedList<Level> levels) {
        LinkedList<Level> succesfullySentLevels = new LinkedList<>();
        String SQL = "INSERT INTO " + tableName + "(numbers, usertime) VALUES (?, ?)";
        switch (tableName) {
            case NumbersContract.CompletedLevelData.TABLE_NAME:
                try {
                    Connection connection = getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    for (Level level : levels) {
                        int affectedrows;
                        preparedStatement.setString(1, level.toString());
                        preparedStatement.setInt(2, level.getUserTime());
                        affectedrows = preparedStatement.executeUpdate();
                        if (affectedrows == 1) {
                            succesfullySentLevels.add(level);
                        }
                    }
                    preparedStatement.close();
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                break;
            default:
                throw new UnsupportedOperationException("Unsupported tableName: " + tableName);
        }
        return succesfullySentLevels;
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(NumbersContract.URL, NumbersContract.USERNAME, NumbersContract.PASSWORD);
    }


}



