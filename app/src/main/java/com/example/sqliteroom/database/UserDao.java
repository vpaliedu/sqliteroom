package com.example.sqliteroom.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.sqliteroom.model.User;
import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);

    @Update
    void update(User user);
}
