package com.hendraanggrian.appcompat.socialview

import android.graphics.Color.*
import android.widget.Toast
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.hendraanggrian.appcompat.socialview.activity.InstrumentedActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.hendraanggrian.appcompat.socialview.test.R

@LargeTest
@RunWith(AndroidJUnit4::class)
class CoreTest : AbstractTest() {

    @Rule
    @JvmField
    var rule = ActivityTestRule(InstrumentedActivity::class.java)

    @Test
    fun introduction() {
        onView(withId(R.id.editText)).perform(
            typeText(
                "This is a standard TextView with #hashtag, @mention, " +
                    "and http://some.url support."
            ),
            closeSoftKeyboard()
        )
        onView(withId(R.id.progressBar)).perform(delay())
    }

    @Test
    fun withoutMention() {
        rule.activity.editText.setMentionEnabled(false)
        onView(withId(R.id.editText)).perform(
            typeText("You can disable @mention to only have #hashtag."),
            closeSoftKeyboard()
        )
        onView(withId(R.id.progressBar)).perform(delay())
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
            ),
            closeSoftKeyboard()
        )
        onView(withId(R.id.progressBar)).perform(delay())
    }

    @Test
    fun clickable() {
        rule.activity.editText.setOnHashtagClickListener { _, s ->
            Toast.makeText(rule.activity, s, Toast.LENGTH_SHORT).show()
        }
        onView(withId(R.id.editText)).perform(
            typeText("Oh, they are also #clickable!"),
            closeSoftKeyboard()
        )
        onView(withId(R.id.progressBar)).perform(delay())
    }
}