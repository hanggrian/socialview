package com.hendraanggrian.appcompat.socialview

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.hendraanggrian.appcompat.socialview.activity.InstrumentedActivity
import com.hendraanggrian.appcompat.socialview.test.R
import com.hendraanggrian.appcompat.widget.SocialEditText
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
@LargeTest
class GetTest : AbstractTest() {
    @Rule @JvmField var rule = ActivityTestRule(InstrumentedActivity::class.java)

    @Test fun getHashtags() {
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

    @Test fun getMentions() {
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

    @Test fun getHyperlinks() {
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