package com.finin;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.finin.views.activities.UserListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class SimpleActivityTest {

    @Rule
    public ActivityTestRule<UserListActivity> activityRule =
            new ActivityTestRule<>(UserListActivity.class);

    @Test
    public void userDataAvailable() {
        Espresso.onView(withText("George Bluth")).check(matches(isDisplayed()));
    }


}

