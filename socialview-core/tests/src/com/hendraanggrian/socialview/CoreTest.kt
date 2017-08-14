package com.hendraanggrian.socialview

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.hendraanggrian.socialview.activity.InstrumentedActivity
import com.hendraanggrian.socialview.test.R
import org.jetbrains.anko.toast
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
@RunWith(AndroidJUnit4::class)
class CoreTest : BaseTest() {

    @Rule @JvmField var rule = ActivityTestRule(InstrumentedActivity::class.java)

    @Test
    @Throws(Exception::class)
    fun introduction() {
        onView(withId(R.id.editText)).perform(
                typeText("This is a standard TextView with #hashtag, @mention, and http://some.url support."),
                closeSoftKeyboard())
        onView(withId(R.id.progressBar)).perform(delay())
    }

    @Test
    @Throws(Exception::class)
    fun withoutMention() {
        rule.activity.editText.isMentionEnabled = false
        onView(withId(R.id.editText)).perform(
                typeText("You can disable @mention to only have #hashtag."),
                closeSoftKeyboard())
        onView(withId(R.id.progressBar)).perform(delay())
    }

    @Test
    @Throws(Exception::class)
    fun customColors() {
        rule.activity.editText.setHashtagColorRes(android.R.color.holo_red_light)
        rule.activity.editText.setMentionColorRes(android.R.color.holo_green_light)
        rule.activity.editText.setHyperlinkColorRes(android.R.color.holo_blue_light)
        onView(withId(R.id.editText)).perform(
                typeText("Accent color of current app theme is used by default. But you can also have separate color for #hashtag, @mention, and http://hyperlink.com."),
                closeSoftKeyboard())
        onView(withId(R.id.progressBar)).perform(delay())
    }

    @Test
    @Throws(Exception::class)
    fun clickable() {
        rule.activity.editText.setOnHashtagClickListener { _, charSequence ->
            rule.activity.toast(charSequence.toString())
        }
        onView(withId(R.id.editText)).perform(
                typeText("Oh, they are also #clickable!"),
                closeSoftKeyboard())
        onView(withId(R.id.progressBar)).perform(delay())
    }
}