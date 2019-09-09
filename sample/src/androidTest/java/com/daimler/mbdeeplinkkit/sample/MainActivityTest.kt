package com.daimler.mbdeeplinkkit.sample

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.daimler.mbdeeplinkkit.sample.main.MainActivity
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    val mainActivityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule(
            MainActivity::class.java,
            true,
            true
    )

    /** Tests whether the MainActivity's TextView is non-empty after a click on the button. */
    @Test
    fun testTextView() {
        mainActivityTestRule.launchActivity(Intent())
        onView(withId(R.id.main_button)).perform(click())
        onView(withId(R.id.main_text)).check(matches(not(withText(""))))
    }
}
