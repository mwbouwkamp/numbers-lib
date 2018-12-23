package nl.limakajo.numberslib.utils;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getName();

    /**
     * Calls getLevelsFromServer and returns leveldata from postgresql database
     *
     * @param tableName     table from which the data needs to be retrieved
     * @return              all levels from database
     */
    public static JSONObject queryLevels(String tableName) {
        JSONObject jsonObjectToReturn = new JSONObject();
        if (!tableName.equals(NetworkContract.LevelData.TABLE_NAME) && !tableName.equals(NetworkContract.CompletedLevelData.TABLE_NAME)) {
            throw new UnsupportedOperationException("Unsupported tableName: " + tableName);
        }
        else {
            String SQL = "SELECT * FROM " + tableName;
            try {
                Class.forName("org.postgresql.Driver");
                Connection connection = getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(SQL);
                while (resultSet.next()) {
                    switch (tableName) {
                        case NetworkContract.LevelData.TABLE_NAME:
                            int key = resultSet.getInt(1);
                            String numbersString = resultSet.getString(2);
                            int averageTime = resultSet.getInt(3);
                            int userTime = GameConstants.TIMEPENALTY;
                            int timesPlayed = resultSet.getInt(4);
                            JSONObject levelJson = new JSONObject()
                                    .put(DatabaseScheme.KEY_NUMBERS, numbersString)
                                    .put(DatabaseScheme.KEY_AVERAGE_TIME, averageTime)
                                    .put(DatabaseScheme.KEY_USER_TIME, userTime)
                                    .put(DatabaseScheme.KEY_TIMES_PLAYED, timesPlayed);
                            jsonObjectToReturn.put(Integer.toString(key), levelJson);
                            break;
                        case NetworkContract.CompletedLevelData.TABLE_NAME:
                            key = resultSet.getInt(1);
                            numbersString = resultSet.getString(2);
                            averageTime = 0;
                            userTime = resultSet.getInt(3);
                            timesPlayed = 0;
                            levelJson = new JSONObject()
                                    .put(DatabaseScheme.KEY_NUMBERS, numbersString)
                                    .put(DatabaseScheme.KEY_AVERAGE_TIME, averageTime)
                                    .put(DatabaseScheme.KEY_USER_TIME, userTime)
                                    .put(DatabaseScheme.KEY_TIMES_PLAYED, timesPlayed);
                            jsonObjectToReturn.put(Integer.toString(key), levelJson);
                            break;
                        default:
                            throw new UnsupportedOperationException("Unsupported tableName: " + tableName);
                    }
                }
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return jsonObjectToReturn;
    }

    /**
     * Inserts leveldata in postgresql database
     * For table completedleveldata key, numbers and userTime are inserted
     *
     * @param tableName     table into which the levels need to be inserted
     * @param levelsJson    levels to insert
     * @return              levels that have been inserted successfully
     */
    public static JSONObject insertLevels(String tableName, JSONObject levelsJson) {
        JSONObject jsonObjectToReturn = new JSONObject();
        if (!tableName.equals(NetworkContract.CompletedLevelData.TABLE_NAME)) {
            throw new UnsupportedOperationException("Unsupported tableName: " + tableName);
        }
        else {
            String SQL = "INSERT INTO " + tableName + "(numbers, usertime) VALUES (?, ?)";
            try {
                Class.forName("org.postgresql.Driver");
                Connection connection = getConnection();
                Iterator<String> keys = levelsJson.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    JSONObject levelJson = (JSONObject) levelsJson.get(key);
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setString(1, levelJson.getString(DatabaseScheme.KEY_NUMBERS));
                    preparedStatement.setInt(2, levelJson.getInt(DatabaseScheme.KEY_USER_TIME));
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows == 1) {
                        jsonObjectToReturn.put(key, levelJson);
                    }
                    preparedStatement.close();
                }
                connection.close();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return jsonObjectToReturn;
    }

    /**
     * Deletes levels from database based on numbers
     *
     * @param tableName     table from which the leves need to be deleted
     * @param levelsJson    levels to delete
     * @return              levels that have been deleted succesfully
     */
    public static JSONObject deleteLevels(String  tableName, JSONObject levelsJson) {
        JSONObject jsonObjectToReturn = new JSONObject();
        //
        if (!tableName.equals(NetworkContract.CompletedLevelData.TABLE_NAME)) {
            throw new UnsupportedOperationException("Unsupported tableName: " + tableName);
        }
        else {
            String SQL = "DELETE FROM " + tableName + " WHERE numbers = ?";
            try {
                Class.forName("org.postgresql.Driver");
                Connection connection = getConnection();
                Iterator<String> keys = levelsJson.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    JSONObject levelJson = (JSONObject) levelsJson.get(key);
                    PreparedStatement preparedStatement = connection.prepareStatement(SQL);
                    preparedStatement.setString(1, levelJson.getString(DatabaseScheme.KEY_NUMBERS));
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows == 1) {
                        jsonObjectToReturn.put(key, levelJson);
                    }
                    preparedStatement.close();
                }
                connection.close();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return jsonObjectToReturn;
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(NetworkContract.URL, NetworkContract.USERNAME, NetworkContract.PASSWORD);
    }

    public static class NetworkContract {
        static final String URL = "jdbc:postgresql://dumbo.db.elephantsql.com:5432/hclznblm";
        static final String USERNAME = "hclznblm";
        static final String PASSWORD = "QYXN4qpruR3E1um4QlPwOt2W6ki5jdAM";

        public static final class LevelData {

            //Table Name
            public static final String TABLE_NAME = "leveldata";

            //Column names
            public static final String KEY_PRIMARY = DatabaseScheme.KEY_PRIMARY;
            public static final String KEY_NUMBERS = DatabaseScheme.KEY_NUMBERS;
            public static final String KEY_AVERAGE_TIME = DatabaseScheme.KEY_AVERAGE_TIME;
            public static final String KEY_TIMES_PLAYED = DatabaseScheme.KEY_TIMES_PLAYED;
        }

        public static final class CompletedLevelData {

            //Table Name
            public static final String TABLE_NAME = "completedleveldata";

            //Column names
            public static final String KEY_PRIMARY = DatabaseScheme.KEY_PRIMARY;
            public static final String KEY_NUMBERS = DatabaseScheme.KEY_NUMBERS;
            public static final String KEY_USER_TIME = DatabaseScheme.KEY_USER_TIME;
        }
    }
}



