package nl.limakajo.numberslib.onlineData;

public class NumbersContract {
    static final String URL = "jdbc:postgresql://dumbo.db.elephantsql.com:5432/hclznblm";
    static final String USERNAME = "hclznblm";
    static final String PASSWORD = "QYXN4qpruR3E1um4QlPwOt2W6ki5jdAM";

    public static final class LevelData {

        //Table Name
        public static final String TABLE_NAME = "leveldata";

        //Column names
        public static final String KEY_NUMBERS = "nl/limakajo/numberslib/numbersGame";
        public static final String KEY_AVERAGE_TIME = "averagetime";
        public static final String KEY_TIMES_PLAYED = "timesplayed";
    }

    public static final class CompletedLevelData {

        //Table Name
        public static final String TABLE_NAME = "completedleveldata";

        //Column names
        public static final String KEY_NUMBERS = "nl/limakajo/numberslib/numbersGame";
        public static final String KEY_AVERAGE_TIME = "usertime";
    }
}
