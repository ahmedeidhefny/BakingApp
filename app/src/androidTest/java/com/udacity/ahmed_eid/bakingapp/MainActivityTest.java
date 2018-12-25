package com.udacity.ahmed_eid.bakingapp;

import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;

import com.udacity.ahmed_eid.bakingapp.Activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void contentTest() {
        //onView(withId(R.id.progress_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));
    }
}
