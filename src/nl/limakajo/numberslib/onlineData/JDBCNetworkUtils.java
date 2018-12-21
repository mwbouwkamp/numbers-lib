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

    public static LinkedList<Level> getLevelsJSONFromServer(String tableName) {
        LinkedList<Level> toReturn= new LinkedList<>();
        String SQL = "SELECT * FROM ";
        switch (tableName) {
            case NumbersContract.LevelData.TABLE_NAME:
                SQL += NumbersContract.LevelData.TABLE_NAME;
                try {
                    Connection connection = getConnection();
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(SQL);
                    while (resultSet.next()) {
                        String numbersString = resultSet.getString(1);
                        int averageTime = Integer.parseInt(resultSet.getString(2));
                        Level levelToAdd = new Level.LevelBuilder(numbersString)
                                .setAverageTime(averageTime)
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
                SQL += NumbersContract.LevelData.TABLE_NAME;
                try {
                    Connection connection = getConnection();
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(SQL);
                    while (resultSet.next()) {
                        //TODO: Logic for CompletedLevelData
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

    public static LinkedList<Level> sendLevelsToServer(LinkedList<Level> levels) {
        LinkedList<Level> succesfullySentLevels = new LinkedList<>();
        String SQL = "INSERT INTO completedleveldata (numbers, usertime) VALUES (?, ?)";
        try {
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            for (Level level: levels) {
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
        return succesfullySentLevels;
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(NumbersContract.URL, NumbersContract.USERNAME, NumbersContract.PASSWORD);
    }


}



