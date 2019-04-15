package database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

@Dao
public interface UserDao {

    @Insert
    void insert(UserData obj);

    @Update
    void update(UserData obj);

    @Delete
    void delete(UserData obj);



    @Query("Select id from signUpData where password = :password AND name =:name")
    Cursor fetch(String password, String name);


    @Query("Select * from signUpData where id=:id")
    Cursor fetchAll(int id);

    @Query("Select email from signUpData")
    Cursor fetchEmailAll();

}
