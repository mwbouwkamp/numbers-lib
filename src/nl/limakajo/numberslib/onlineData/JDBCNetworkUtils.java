package nl.limakajo.numberslib.onlineData;

import nl.limakajo.numberslib.utils.DatabaseScheme;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

public class JDBCNetworkUtils {

    private static final String TAG = JDBCNetworkUtils.class.getName();

    /**
     * Calls getLevelsFromServer and returns leveldata from postgresql database
     *
     * @param tableName     table from which the data needs to be retrieved
     * @return              all levels from database
     */
    public static JSONObject queryLevelJSON(String tableName) {
        JSONObject toReturn = new JSONObject();
        String SQL = "SELECT * FROM " + tableName;
        try {
            Class.forName("org.postgresql.Driver");
            switch (tableName) {
                case NetworkContract.LevelData.TABLE_NAME:
                    try {
                        Connection connection = getConnection();
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(SQL);
                        while (resultSet.next()) {
                            int key = resultSet.getInt(1);
                            String numbersString = resultSet.getString(2);
                            int averageTime = resultSet.getInt(3);
                            int timesPlayed = resultSet.getInt(4);
                            JSONObject levelJson = new JSONObject()
                                    .put(DatabaseScheme.KEY_NUMBERS, numbersString)
                                    .put(DatabaseScheme.KEY_AVERAGE_TIME, averageTime)
                                    .put(DatabaseScheme.KEY_USER_TIME, 0)
                                    .put(DatabaseScheme.KEY_TIMES_PLAYED, timesPlayed);
                            toReturn.put(Integer.toString(key), levelJson);
                        }
                        resultSet.close();
                        statement.close();
                    }
                    catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                case NetworkContract.CompletedLevelData.TABLE_NAME:
                    try {
                        Connection connection = getConnection();
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(SQL);
                        while (resultSet.next()) {
                            int key = resultSet.getInt(1);
                            String numbersString = resultSet.getString(2);
                            int userTime = resultSet.getInt(3);
                            JSONObject levelJson = new JSONObject()
                                    .put(DatabaseScheme.KEY_NUMBERS, numbersString)
                                    .put(DatabaseScheme.KEY_AVERAGE_TIME, 0)
                                    .put(DatabaseScheme.KEY_USER_TIME, userTime)
                                    .put(DatabaseScheme.KEY_TIMES_PLAYED, 0);
                            toReturn.put(Integer.toString(key), levelJson);
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    /**
     * Inserts leveldata in postgresql database
     * For table completedleveldata key, numbers and userTime are inserted
     *
     * @param tableName     table into which the levels need to be inserted
     * @param levelsJson        levels to insert
     * @return              levels that have been inserted successfully
     */
    public static JSONObject insertLevels(String tableName, JSONObject levelsJson) {
        JSONObject toReturn = new JSONObject();
        String SQL = "INSERT INTO " + tableName + "(numbers, usertime) VALUES (?, ?)";
        switch (tableName) {
            case NetworkContract.CompletedLevelData.TABLE_NAME:
                try {
                    Connection connection  = getConnection();
                    Iterator<String> keys = levelsJson.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        JSONObject levelJson = (JSONObject) levelsJson.get(key);
                        PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                        int affectedrows;
                        preparedStatement.setString(1, levelJson.getString(DatabaseScheme.KEY_NUMBERS));
                        preparedStatement.setInt(2, levelJson.getInt(DatabaseScheme.KEY_USER_TIME));
                        affectedrows = preparedStatement.executeUpdate();
                        if (affectedrows == 1) {
                            toReturn.put(key, levelJson);
                        }
                        preparedStatement.close();
                    }
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            default:
                throw new UnsupportedOperationException("Unsupported tableName: " + tableName);
        }
        return toReturn;
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(NetworkContract.URL, NetworkContract.USERNAME, NetworkContract.PASSWORD);
    }
}



