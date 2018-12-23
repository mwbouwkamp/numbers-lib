package nl.limakajo.numberslib.main;

import nl.limakajo.numberslib.onlineData.JDBCNetworkUtils;
import nl.limakajo.numberslib.onlineData.NetworkContract;
import org.json.JSONObject;

public class Main {

    public static void main(String[] args) {
        JSONObject jsonObject = JDBCNetworkUtils.queryLevels(NetworkContract.CompletedLevelData.TABLE_NAME);
        System.out.println(jsonObject);
        jsonObject = JDBCNetworkUtils.queryLevels(NetworkContract.LevelData.TABLE_NAME);
        System.out.println(jsonObject);
        jsonObject = JDBCNetworkUtils.queryLevels("table-does-not-exist");

    }


}
