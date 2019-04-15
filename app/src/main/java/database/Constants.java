package database;

 public class Constants {

    static final int VERSION_NO= 1;
    @SuppressWarnings("SpellCheckingInspection")
    static final String DATABASE_NAME="SignupDetails" ;
    static final String TABLE_NAME="signUpData";
    public static final String ID="id";
    public static final String NAME="name";
    public static final String EMAIL="email";
    public static final String PASSWORD="password";
    @SuppressWarnings("SpellCheckingInspection")
    public static final String PHONE_NO="phoneNo";

    static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT NOT NULL, " + EMAIL + " TEXT, "  + PASSWORD + " TEXT, " + PHONE_NO + " INTEGER);";


}
