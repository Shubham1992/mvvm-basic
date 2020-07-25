package com.finin.models.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.finin.models.user.User;
import com.finin.models.user.UserDao;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    static AppDatabase db = null;

    public static AppDatabase getInstance(Context context) {
        if (db == null)
            synchronized (AppDatabase.class) {
                db = Room.databaseBuilder(context,
                        AppDatabase.class, "finindatabase.db").allowMainThreadQueries().build();
            }
        return db;
    }
}
