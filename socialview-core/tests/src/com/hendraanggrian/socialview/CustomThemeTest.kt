package com.hendraanggrian.socialview

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.hendraanggrian.socialview.activity.CustomThemeActivity
import com.hendraanggrian.socialview.test.R
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters.JVM

@RunWith(AndroidJUnit4::class)
@LargeTest
@FixMethodOrder(JVM)
class CustomThemeTest : BaseTest() {

    @Rule @JvmField var rule = ActivityTestRule(CustomThemeActivity::class.java)

    @Test
    @Throws(Exception::class)
    fun introduction() {
        onView(withId(R.id.editText)).perform(
                typeText("This is a standard TextView with #hashtag, @mention, and http://some.url support."),
                closeSoftKeyboard())
        onView(withId(R.id.progressBar)).perform(delay())
    }
}