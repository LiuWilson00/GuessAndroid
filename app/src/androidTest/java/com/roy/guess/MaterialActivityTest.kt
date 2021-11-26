package com.roy.guess

import android.content.res.Resources
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MaterialActivityTest {
    @Rule
    @JvmField
    val rule = ActivityScenarioRule(MaterialActivity::class.java)

    private lateinit var resource: Resources
    private var secret = 0
    private val guessRound: Int = 3

    @Test
    fun guessWrong() {

        rule.scenario.onActivity {
            secret = it.secretNumber.secret
            resource = it.resources
        }
        for (n: Int in 1..10) {
            if (n != secret) {
                onView(withId(R.id.ed_number)).perform(clearText())
                onView(withId(R.id.ed_number)).perform(typeText(n.toString()))
                onView(withId(R.id.ok_button)).perform(click())
                val message: String =
                    if (n < secret) resource.getString(R.string.bigger)
                    else resource.getString(R.string.smaller)
                onView(withText(message)).check(matches(isDisplayed()))
                onView(withText(resource.getString(R.string.ok))).perform(click())
            }
        }


    }

    @Test
    fun replayAndClearCounterTest() {

        rule.scenario.onActivity {
            secret = it.secretNumber.secret
            resource = it.resources
        }
        val guessNum: Int = if (secret == 10) 9 else (secret + 1)
        for (n: Int in 1..guessRound) {
            guessOne(guessNum)
        }
        onView(withId(R.id.ed_number)).perform(closeSoftKeyboard())
        onView(withId(R.id.fab)).perform(click())
        onView(withText(resource.getString(R.string.ok))).perform(click())
        onView(withId(R.id.counter)).check(matches(withText("0")))
    }

    @Test
    fun replayCancelTest() {

        rule.scenario.onActivity {
            secret = it.secretNumber.secret
            resource = it.resources
        }
        val guessNum: Int = if (secret == 10) 9 else (secret + 1)
        for (n: Int in 1..guessRound) {
            guessOne(guessNum)
        }
        onView(withId(R.id.ed_number)).perform(closeSoftKeyboard())
        onView(withId(R.id.fab)).perform(click())
        onView(withText(resource.getString(R.string.cancel))).perform(click())
        onView(withId(R.id.counter)).check(matches(withText(guessRound.toString())))
    }
    private fun guessOne(n:Int){
        onView(withId(R.id.ed_number)).perform(clearText())
        onView(withId(R.id.ed_number)).perform(typeText(n.toString()))
        onView(withId(R.id.ok_button)).perform(click())
        onView(withText(resource.getString(R.string.ok))).perform(click())
    }
}