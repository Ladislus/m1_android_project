package univ.orleans.ttl.isokachallenge.orm

//  ###########################
//  #   DATABASE INFORMATION  #
//  ###########################

const val DB_NAME: String = "isoka_db"
const val DB_VERSION: Int = 1

const val DB_LOG: String = "isoka_db_log"

//  #################
//  #   USER TABLE  #
//  #################

const val USER_TABLE: String = "USER"
const val USER_NAME: String = "u_username"
const val USER_DATE: String = "u_date"

const val USER_CREATE: String = "CREATE TABLE $USER_TABLE($USER_NAME VARCHAR(255) PRIMARY KEY, $USER_DATE DATE NOT NULL);"

//  ####################
//  #   DRAWING TABLE  #
//  ####################

const val DRAWING_TABLE: String = "DRAWING"
const val DRAWING_ID: String = "d_id"
const val DRAWING_LINK: String = "d_link"
const val DRAWING_DATE: String = "d_date"

const val DRAWING_CREATE: String = "CREATE TABLE $DRAWING_TABLE($DRAWING_ID INTEGER PRIMARY KEY, $DRAWING_LINK VARCHAR(512) NOT NULL, $DRAWING_DATE DATE NOT NULL);"

//  ######################
//  #   CHALLENGE TABLE  #
//  ######################

const val CHALLENGE_TABLE: String = "CHALLENGE"
const val CHALLENGE_ID: String = "c_id"
const val CHALLENGE_NAME: String = "c_name"
const val CHALLENGE_TYPE: String = "c_type"
const val CHALLENGE_THEME: String = "c_theme"
const val CHALLENGE_DURATION: String = "c_duration"
const val CHALLENGE_TIMER: String = "c_timer"

const val CHALLENGE_CREATE: String = "CREATE TABLE $CHALLENGE_TABLE($CHALLENGE_ID INTEGER PRIMARY KEY, $CHALLENGE_NAME VARCHAR(255) NOT NULL, $CHALLENGE_TYPE BOOLEAN NOT NULL, $CHALLENGE_THEME VARCHAR(255) NOT NULL, $CHALLENGE_DURATION INTEGER NOT NULL, $CHALLENGE_TIMER INTEGER NOT NULL);"

//  ##########################
//  #   PARTICIPATION TABLE  #
//  ##########################

const val PARTICIPATION_TABLE: String = "PARTICIPATION"
const val PARTICIPATION_CHALLENGE_ID: String = "p_c_id"
const val PARTICIPATION_USER_ID: String = "p_u_id"
const val PARTICIPATION_DRAWING_ID: String = "p_d_id"
const val PARTICIPATION_IS_CREATOR: String = "p_is_creator"
const val PARTICIPATION_VOTES: String = "p_votes"

const val PARTICIPATION_CREATE: String = "CREATE TABLE $PARTICIPATION_TABLE($PARTICIPATION_USER_ID VARCHAR(255), $PARTICIPATION_DRAWING_ID INTEGER, $PARTICIPATION_CHALLENGE_ID INTEGER, $PARTICIPATION_IS_CREATOR BOOLEAN NOT NULL, $PARTICIPATION_VOTES INTEGER DEFAULT 0, FOREIGN KEY($PARTICIPATION_USER_ID) REFERENCES $USER_TABLE($USER_NAME) ON DELETE CASCADE, FOREIGN KEY($PARTICIPATION_DRAWING_ID) REFERENCES $DRAWING_TABLE($DRAWING_ID) ON DELETE CASCADE, FOREIGN KEY($PARTICIPATION_CHALLENGE_ID) REFERENCES $CHALLENGE_TABLE($CHALLENGE_ID) ON DELETE CASCADE, PRIMARY KEY($PARTICIPATION_USER_ID, $PARTICIPATION_DRAWING_ID, $PARTICIPATION_CHALLENGE_ID));"

//  ############
//  #   OTHER  #
//  ############

const val DROP: String = "DROP TABLE IF EXISTS $PARTICIPATION_TABLE; DROP TABLE IF EXISTS $USER_TABLE; DROP TABLE IF EXISTS $DRAWING_TABLE; DROP TABLE IF EXISTS $CHALLENGE_TABLE;"