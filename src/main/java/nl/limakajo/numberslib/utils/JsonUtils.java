package nl.limakajo.numberslib.utils;

import nl.limakajo.numberslib.numbersGame.Level;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.LinkedList;

public class JsonUtils {

    public static Level jsonToLevel(JSONObject levelJson) {
        String numbers = levelJson.getString(DatabaseScheme.KEY_NUMBERS);
        int userTime = levelJson.getInt(DatabaseScheme.KEY_USER_TIME);
        int averageTime = levelJson.getInt(DatabaseScheme.KEY_AVERAGE_TIME);
        int timesPlayed = levelJson.getInt(DatabaseScheme.KEY_TIMES_PLAYED);
        return new Level.LevelBuilder(numbers)
                .setAverageTime(averageTime)
                .setUserTime(userTime)
                .setTimesPlayed(timesPlayed)
                .build();
    }

    public static LinkedList<Level> jsonToLevels(JSONObject levelsJson){
        LinkedList<Level> toReturn = new LinkedList<Level>();
        Iterator<String> keys = levelsJson.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                Level level = jsonToLevel((JSONObject) levelsJson.get(key));
                toReturn.add(level);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return toReturn;
    }

    public static JSONObject levelToJson(Level level) {
        String numbers = level.toString();
        int averagetime = level.getAverageTime();
        int usertime = level.getUserTime();
        int timesPlayed = level.getTimesPlayed();
        return new JSONObject()
                .put(DatabaseScheme.KEY_NUMBERS, numbers)
                .put(DatabaseScheme.KEY_AVERAGE_TIME, averagetime)
                .put(DatabaseScheme.KEY_USER_TIME, usertime)
                .put(DatabaseScheme.KEY_TIMES_PLAYED, timesPlayed);
    }

    public static JSONObject levelsToJson(LinkedList<Level> levels) {
        JSONObject toReturn = new JSONObject();
        int key = 0;
        for (Level level: levels) {
            try {
                toReturn.put(Integer.toString(key), levelToJson(level));
                key++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return toReturn;
    }
}
