package univ.orleans.ttl.isokachallenge.orm;

public class Tables {

    //  ###########################
    //  #   DATABASE INFORMATION  #
    //  ###########################

    public static final String DB_NAME = "isoka_db";
    public static final int DB_VERSION = 1;

    public static final String DB_LOG = "isoka_db_log";

    //  #################
    //  #   USER TABLE  #
    //  #################

    public static final String USER_TABLE = "USER";
    public static final String USER_NAME = "u_username";
    public static final String USER_DATE = "u_date";

    public static final String USER_CREATE = "CREATE TABLE " + USER_TABLE + "(" + USER_NAME + " VARCHAR(255) PRIMARY KEY," + USER_DATE + " DATE NOT NULL);";

    //  ####################
    //  #   DRAWING TABLE  #
    //  ####################

    public static final String DRAWING_TABLE = "DRAWING";
    public static final String DRAWING_ID = "d_id";
    public static final String DRAWING_LINK = "d_link";
    public static final String DRAWING_DATE = "d_date";

    public static final String DRAWING_CREATE = "CREATE TABLE " + DRAWING_TABLE + "("
            + DRAWING_ID + " INTEGER PRIMARY KEY,"
            + DRAWING_LINK + " VARCHAR(512) NOT NULL,"
            + DRAWING_DATE + " DATE NOT NULL);";

    //  ######################
    //  #   CHALLENGE TABLE  #
    //  ######################

    public static final String CHALLENGE_TABLE = "CHALLENGE";
    public static final String CHALLENGE_ID = "c_id";
    public static final String CHALLENGE_NAME = "c_name";
    public static final String CHALLENGE_TYPE = "c_type";
    public static final String CHALLENGE_THEME = "c_theme";
    public static final String CHALLENGE_DURATION = "c_duration";
    public static final String CHALLENGE_TIMER = "c_timer";

    public static final String CHALLENGE_CREATE = "CREATE TABLE " + CHALLENGE_TABLE + "("
            + CHALLENGE_ID + " INTEGER PRIMARY KEY,"
            + CHALLENGE_NAME + " VARCHAR(255) NOT NULL,"
            + CHALLENGE_TYPE + " BOOLEAN NOT NULL,"
            + CHALLENGE_THEME + " VARCHAR(255) NOT NULL,"
            + CHALLENGE_DURATION + " INTEGER NOT NULL,"
            + CHALLENGE_TIMER + " INTEGER NOT NULL);";

    //  ##########################
    //  #   PARTICIPATION TABLE  #
    //  ##########################

    public static final String PARTICIPATION_TABLE = "PARTICIPATION";
    public static final String PARTICIPATION_CHALLENGE_ID = "p_c_id";
    public static final String PARTICIPATION_USER_ID = "p_u_id";
    public static final String PARTICIPATION_DRAWING_ID = "p_d_id";
    public static final String PARTICIPATION_IS_CREATOR = "p_is_creator";
    public static final String PARTICIPATION_VOTES = "p_votes";

    public static final String PARTICIPATION_CREATE = "CREATE TABLE " + PARTICIPATION_TABLE + "("
            + PARTICIPATION_USER_ID + " VARCHAR(255),"
            + PARTICIPATION_DRAWING_ID + " INTEGER,"
            + PARTICIPATION_CHALLENGE_ID + " INTEGER,"
            + PARTICIPATION_IS_CREATOR + " BOOLEAN NOT NULL,"
            + PARTICIPATION_VOTES + " INTEGER DEFAULT 0,"
            + "FOREIGN KEY(" + PARTICIPATION_USER_ID + ") REFERENCES " + USER_TABLE + "(" + USER_NAME + ") ON DELETE CASCADE,"
            + "FOREIGN KEY(" + PARTICIPATION_DRAWING_ID + ") REFERENCES " + DRAWING_TABLE + "( " + DRAWING_ID + ") ON DELETE CASCADE,"
            + "FOREIGN KEY(" + PARTICIPATION_CHALLENGE_ID + ") REFERENCES " + CHALLENGE_TABLE + "(" + CHALLENGE_ID + ") ON DELETE CASCADE,"
            + "PRIMARY KEY(" + PARTICIPATION_USER_ID + "," + PARTICIPATION_DRAWING_ID + "," + PARTICIPATION_CHALLENGE_ID + "));";

    //  ############
    //  #   OTHER  #
    //  ############

    public static final String DROP = "DROP TABLE IF EXISTS " + PARTICIPATION_TABLE + ";"
            + "DROP TABLE IF EXISTS " + USER_TABLE + ";"
            + "DROP TABLE IF EXISTS " + DRAWING_TABLE + ";"
            + "DROP TABLE IF EXISTS " + CHALLENGE_TABLE + ";";
}