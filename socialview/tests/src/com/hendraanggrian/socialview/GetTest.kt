package com.hendraanggrian.socialview

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.hendraanggrian.socialview.activity.InstrumentedActivity
import com.hendraanggrian.socialview.test.R
import com.hendraanggrian.widget.SocialEditText
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class GetTest : BaseTest() {

    @Rule @JvmField var rule = ActivityTestRule(InstrumentedActivity::class.java)

    @Test
    @Throws(Exception::class)
    fun getHashtags() {
        onView(withId(R.id.editText)).perform(
                typeText("Hi there #eitantest"),
                closeSoftKeyboard(),
                object : ViewAction {
                    override fun getDescription() = "assertion"
                    override fun getConstraints() = isAssignableFrom(SocialEditText::class.java)
                    override fun perform(uiController: UiController?, view: View) {
                        val editText = view as SocialEditText
                        val hashtags = editText.hashtags
                        assertEquals(hashtags.size, 1)
                        assertEquals(hashtags[0], "eitantest")
                    }
                })
    }

    @Test
    @Throws(Exception::class)
    fun getMentions() {
        onView(withId(R.id.editText)).perform(
                typeText("Hi there @eitantest"),
                closeSoftKeyboard(),
                object : ViewAction {
                    override fun getDescription() = "assertion"
                    override fun getConstraints() = isAssignableFrom(SocialEditText::class.java)
                    override fun perform(uiController: UiController?, view: View) {
                        val editText = view as SocialEditText
                        val mentions = editText.mentions
                        assertEquals(mentions.size, 1)
                        assertEquals(mentions[0], "eitantest")
                    }
                })
    }

    @Test
    @Throws(Exception::class)
    fun getHyperlinks() {
        onView(withId(R.id.editText)).perform(
                typeText("Check out https://my.website.com"),
                closeSoftKeyboard(),
                object : ViewAction {
                    override fun getDescription() = "assertion"
                    override fun getConstraints() = isAssignableFrom(SocialEditText::class.java)
                    override fun perform(uiController: UiController?, view: View) {
                        val editText = view as SocialEditText
                        val hyperlinks = editText.hyperlinks
                        assertEquals(hyperlinks.size, 1)
                        assertEquals(hyperlinks[0], "https://my.website.com")
                    }
                })
    }
}