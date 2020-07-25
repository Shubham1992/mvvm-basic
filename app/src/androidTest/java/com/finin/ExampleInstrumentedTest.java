package com.finin;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.finin.models.database.AppDatabase;
import com.finin.models.user.User;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.functions.Predicate;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.finin", appContext.getPackageName());
    }


    @Test
    public void insertAndGetUsers() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        // Given that we have a user in the data source
        final User user = new User();
        user.setId(100);
        user.setFirst_name("Shubham");
        AppDatabase.getInstance(appContext).userDao().insertUser(user);
        // When subscribing to the emissions of user
        AppDatabase.getInstance(appContext).userDao()
                .getAll()
                .test()
                // assertValue asserts that there was only one emission
                .assertValue(new Predicate<List<User>>() {
                    @Override
                    public boolean test(List<User> users) throws Exception {
                        // The emitted user is the expected one
                        return users.size() > 0;
                    }
                });
    }
}
