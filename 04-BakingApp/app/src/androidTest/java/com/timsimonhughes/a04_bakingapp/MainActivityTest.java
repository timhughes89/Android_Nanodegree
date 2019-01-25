package com.timsimonhughes.a04_bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import com.timsimonhughes.a04_bakingapp.controller.MainActivity;
import com.timsimonhughes.a04_bakingapp.controller.fragments.ListFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void toolbarTitleTest() {
        // Check to see if toolbarTitle isEnabled().
        onView(withId(R.id.toolbarTitle))
                .check(matches(isEnabled()));
    }

    @Test
    public void checkTextDisplayedInDynamicallyCreatedFragment() {

        String nutellaPie = "Nutella Pie";

        ListFragment fragment = new ListFragment();
        mActivityTestRule.getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit();

        onView(withId(R.id.rv_recipe_list))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText(nutellaPie)), click()));

//        onView(withId(R.id.txt_cool_message)).check(matches(withText("Cool")));
    }

    @Test
    public void checkRecyclerViewItemClickable() {
        // Check to see if item @ position 0 is Clickable.
        onView(withId(R.id.rv_recipe_list))
                .perform(actionOnItemAtPosition(0, click()));

    }
}

