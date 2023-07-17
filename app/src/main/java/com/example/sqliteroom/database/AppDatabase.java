package com.example.sqliteroom.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.sqliteroom.model.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}