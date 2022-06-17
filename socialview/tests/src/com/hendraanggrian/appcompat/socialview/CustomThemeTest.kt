package com.hendraanggrian.appcompat.socialview

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.hendraanggrian.appcompat.socialview.activity.CustomThemeActivity
import com.hendraanggrian.appcompat.socialview.test.R
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
@LargeTest
class CustomThemeTest {
    @Rule @JvmField var rule = ActivityTestRule(CustomThemeActivity::class.java)

    @Test
    fun introduction() {
        onView(withId(R.id.editText)).perform(
            typeText(
                "This is a standard TextView with #hashtag, @mention, " +
                    "and http://some.url support."
            )
        )
    }
}
