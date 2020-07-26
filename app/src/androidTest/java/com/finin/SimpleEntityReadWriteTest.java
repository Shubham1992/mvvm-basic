package com.finin;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.finin.models.database.AppDatabase;
import com.finin.models.user.User;
import com.finin.models.user.UserDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@RunWith(AndroidJUnit4.class)
public class SimpleEntityReadWriteTest {
    private UserDao userDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        userDao = db.userDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        User user = new User();
        user.setId(1001);
        user.setFirst_name("george");
        userDao.insertUser(user);
        List<User> users = userDao.getAll();
        assert users.size() > 0 && users.get(0).getFirst_name().equalsIgnoreCase("george");
    }
}