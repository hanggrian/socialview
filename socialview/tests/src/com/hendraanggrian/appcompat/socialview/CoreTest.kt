package com.hendraanggrian.appcompat.socialview

import android.graphics.Color.BLUE
import android.graphics.Color.GREEN
import android.graphics.Color.RED
import android.widget.Toast
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.hendraanggrian.appcompat.socialview.activity.InstrumentedActivity
import com.hendraanggrian.appcompat.socialview.test.R
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.Test

@LargeTest
@RunWith(AndroidJUnit4::class)
class CoreTest {
    @Rule @JvmField var rule = ActivityTestRule(InstrumentedActivity::class.java)

    @Test
    fun introduction() {
        onView(withId(R.id.editText)).perform(
            typeText(
                "This is a standard TextView with #hashtag, @mention, " +
                    "and http://some.url support."
            )
        )
    }

    @Test
    fun withoutMention() {
        rule.activity.editText.isMentionEnabled = false
        onView(withId(R.id.editText)).perform(
            typeText("You can disable @mention to only have #hashtag.")
        )
    }

    @Test
    fun customColors() {
        rule.activity.editText.hashtagColor = RED
        rule.activity.editText.mentionColor = GREEN
        rule.activity.editText.hyperlinkColor = BLUE
        onView(withId(R.id.editText)).perform(
            typeText(
                "Accent color of current app theme is used by default. " +
                    "But you can also have separate color for #hashtag, @mention, " +
                    "and http://hyperlink.com."
            )
        )
    }

    @Test
    fun clickable() {
        rule.activity.editText.setOnHashtagClickListener { _, s ->
            Toast.makeText(rule.activity, s, Toast.LENGTH_SHORT).show()
        }
        onView(withId(R.id.editText)).perform(
            typeText("Oh, they are also #clickable!")
        )
    }
}
