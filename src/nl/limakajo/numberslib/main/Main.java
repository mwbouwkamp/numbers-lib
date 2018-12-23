package nl.limakajo.numberslib.main;

import nl.limakajo.numberslib.numbersGame.Level;
import nl.limakajo.numberslib.onlineData.JDBCNetworkUtils;
import nl.limakajo.numberslib.onlineData.NetworkContract;
import nl.limakajo.numberslib.utils.DatabaseScheme;
import nl.limakajo.numberslib.utils.JsonUtils;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
        System.out.println("QUERY");
        JSONObject jsonObjectLevels = JDBCNetworkUtils.queryLevels(NetworkContract.LevelData.TABLE_NAME);
        System.out.println(jsonObjectLevels);
        JSONObject jsonObjectCompletedLevels = JDBCNetworkUtils.queryLevels(NetworkContract.CompletedLevelData.TABLE_NAME);
        System.out.println(jsonObjectCompletedLevels);

        System.out.println("\nINSERT");
        String numbers = "001002003004005006007";
        Level levelToAdd = new Level.LevelBuilder(numbers)
                .build();
        LinkedList<Level> levelsToAdd = new LinkedList<>();
        levelsToAdd.add(levelToAdd);
        JSONObject levelsToAddJson = JsonUtils.levelsToJson(levelsToAdd);
        JSONObject successfullyInsertedLevelsJson = JDBCNetworkUtils.insertLevels(NetworkContract.CompletedLevelData.TABLE_NAME, levelsToAddJson);
        Iterator<String> keys = successfullyInsertedLevelsJson.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            JSONObject levelJson = successfullyInsertedLevelsJson.getJSONObject(key);
            if (levelJson.getString(DatabaseScheme.KEY_NUMBERS).equals(numbers)) {
                System.out.println("Successfully inserted");
            }
        }

        System.out.println("\nDELETE");
        JSONObject successfullyDeletedJson = JDBCNetworkUtils.deleteLevels(NetworkContract.CompletedLevelData.TABLE_NAME, levelsToAddJson);
        keys = successfullyDeletedJson.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            JSONObject levelJson = successfullyDeletedJson.getJSONObject(key);
            if (levelJson.getString(DatabaseScheme.KEY_NUMBERS).equals(numbers)) {
                System.out.println("Successfully deleted");
            }
        }





    }


}
