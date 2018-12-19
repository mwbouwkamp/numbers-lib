package nl.limakajo.numberslib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class JDBCNetworkUtils {

    private static final String TAG = JDBCNetworkUtils.class.getName();
    private static final String URL = "jdbc:postgresql://dumbo.db.elephantsql.com:5432/hclznblm";
    private static final String USERNAME = "hclznblm";
    private static final String PASSWORD = "QYXN4qpruR3E1um4QlPwOt2W6ki5jdAM";

    public static LinkedList<Level> getLevelsJSONFromServer() {
        LinkedList<Level> toReturn= new LinkedList<>();
        String SQL = "SELECT * FROM leveldata";
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);
            while (resultSet.next()) {
                String numbersString = resultSet.getString(1);
                int tile1 = Integer.parseInt(numbersString.substring(0, 3));
                int tile2 = Integer.parseInt(numbersString.substring(3, 6));
                int tile3 = Integer.parseInt(numbersString.substring(6, 9));
                int tile4 = Integer.parseInt(numbersString.substring(9, 12));
                int tile5 = Integer.parseInt(numbersString.substring(12, 15));
                int tile6 = Integer.parseInt(numbersString.substring(15, 18));
                int[] hand = {tile1, tile2, tile3, tile4, tile5, tile6};
                int goal = Integer.parseInt(numbersString.substring(18, 21));
                int averageTime = Integer.parseInt(resultSet.getString(2));
                toReturn.add(new Level(hand, goal, averageTime));
            }
            resultSet.close();
            statement.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return toReturn;
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
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


}



