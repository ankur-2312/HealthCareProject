package database;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@android.arch.persistence.room.Database(entities = {UserData.class}, version = 3)
public abstract class MyDatabase extends RoomDatabase {

    private static MyDatabase instance;

    public static void contextHandler(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, MyDatabase.class, "Mydb").allowMainThreadQueries().build();
        }
    }

    public static MyDatabase getInstance() {
        return instance;
    }

    public abstract UserDao UserDao();

}
