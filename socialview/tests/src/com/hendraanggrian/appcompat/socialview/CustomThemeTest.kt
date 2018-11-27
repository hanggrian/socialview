package com.hendraanggrian.appcompat.socialview

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.hendraanggrian.appcompat.socialview.activity.CustomThemeActivity
import com.hendraanggrian.appcompat.socialview.test.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class CustomThemeTest : AbstractTest() {

    @Rule @JvmField var rule = ActivityTestRule(CustomThemeActivity::class.java)

    @Test fun introduction() {
        onView(withId(R.id.editText)).perform(
            typeText("This is a standard TextView with #hashtag, @mention, " +
                "and http://some.url support."),
            closeSoftKeyboard())
        onView(withId(R.id.progressBar)).perform(delay())
    }
}