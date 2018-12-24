package nl.limakajo.numberslib.main;

import nl.limakajo.numberslib.numbersGame.Level;
import nl.limakajo.numberslib.utils.NetworkUtils;
import nl.limakajo.numberslib.utils.DatabaseScheme;
import nl.limakajo.numberslib.utils.JsonUtils;
import org.json.JSONObject;
import sun.nio.ch.Net;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        System.out.println("QUERY");
        JSONObject jsonObjectLevels = NetworkUtils.queryLevels(NetworkUtils.NetworkContract.LevelData.TABLE_NAME);
        System.out.println(jsonObjectLevels);
        JSONObject jsonObjectLevel = NetworkUtils.queryLevel(NetworkUtils.NetworkContract.LevelData.TABLE_NAME, "005007003003004003288");
        System.out.println(jsonObjectLevel);
        JSONObject jsonObjectCompletedLevels = NetworkUtils.queryLevels(NetworkUtils.NetworkContract.CompletedLevelData.TABLE_NAME);
        System.out.println(jsonObjectCompletedLevels);

        System.out.println("\nINSERT");
        String numbers = "001002003004005006007";
        Level levelToAdd = new Level.LevelBuilder(numbers)
                .build();
        LinkedList<Level> levelsToAdd = new LinkedList<>();
        levelsToAdd.add(levelToAdd);
        JSONObject levelsToAddJson = JsonUtils.levelsToJson(levelsToAdd);
        JSONObject successfullyInsertedLevelsJson = NetworkUtils.insertLevels(NetworkUtils.NetworkContract.CompletedLevelData.TABLE_NAME, levelsToAddJson);
        Iterator<String> keys = successfullyInsertedLevelsJson.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            JSONObject levelJson = successfullyInsertedLevelsJson.getJSONObject(key);
            if (levelJson.getString(DatabaseScheme.KEY_NUMBERS).equals(numbers)) {
                System.out.println("Successfully inserted");
            }
        }

        System.out.println("\nUPDATE");
        int randomAverageTime = (int) (Math.random() * 120000);
        System.out.println("averageTime: " + randomAverageTime);
        Level levelToUpdate = new Level.LevelBuilder("005007003003004003288")  //this is the level with id = 1
                .setAverageTime(randomAverageTime)
                .build();
        JSONObject levelToUpdateJson = JsonUtils.levelToJson(levelToUpdate);
        JSONObject levelsToUpdateJson = new JSONObject();
        levelsToUpdateJson.put("1", levelToUpdateJson);
        JSONObject successfullyUpdatedLevelsJson = NetworkUtils.updateLevelAverageTime(NetworkUtils.NetworkContract.LevelData.TABLE_NAME, levelsToUpdateJson);

        System.out.println("\nDELETE");
        JSONObject successfullyDeletedJson = NetworkUtils.deleteLevels(NetworkUtils.NetworkContract.CompletedLevelData.TABLE_NAME, levelsToAddJson);
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
