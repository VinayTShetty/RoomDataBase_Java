package com.education.database.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Users.class}, version = 2, exportSchema = false)
public abstract class UserDataBase extends RoomDatabase {
    public abstract UserDao userDao();
    private static volatile UserDataBase INSTANCE;

    // Singleton pattern to ensure only one instance of the database
    public static synchronized UserDataBase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context,UserDataBase.class, "user_database").build();
        }
        return INSTANCE;
    }
}
