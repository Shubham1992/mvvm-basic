package com.finin.models.user;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ArrayList<User> users);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Delete
    void delete(User user);

    @Query("DELETE FROM user")
    void deleteAll();
}