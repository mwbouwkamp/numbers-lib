package nl.limakajo.numberslib.onlineData;

import nl.limakajo.numberslib.utils.DatabaseScheme;

import javax.xml.crypto.Data;

public class NetworkContract {
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
