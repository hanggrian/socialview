package com.hendraanggrian.socialview

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.action.ViewActions.replaceText
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.hendraanggrian.socialview.activity.InstrumentedActivity
import com.hendraanggrian.socialview.test.R
import kota.toast
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters.JVM

@RunWith(AndroidJUnit4::class)
@LargeTest
@FixMethodOrder(JVM)
class PatternTest : BaseTest() {

    companion object {
        const val NAMES = "@HendraAnggrian\n" +
                "@jeffersonlicet\n" +
                "@sfucko\n\n" +

                // https://github.com/HendraAnggrian/socialview/issues/8
                "@Mañana\n" +
                "@CreaciónDivina\n" +
                "@TúVendrás\n" +
                "@CuatroArtículos\n" +
                "@MásNadaQueda\n" +
                "@SeFueÉste\n" +
                "@ÉsteNoEra\n" +
                "@Ñame\n\n" +

                // https://github.com/HendraAnggrian/socialview/issues/13
                "@Андрей\n" +
                "@Владимир\n" +
                "@Дмитрий\n" +
                "@Вова\n" +
                "@Саша"
    }

    @Rule @JvmField var rule = ActivityTestRule(InstrumentedActivity::class.java)

    @Test
    @Throws(Exception::class)
    fun default() {
        // SocialView.setMentionPattern(SocialView.PATTERN_DEFAULT)
        onView(withId(R.id.editText)).perform(
                replaceText(NAMES),
                toast("default"),
                closeSoftKeyboard())
        onView(withId(R.id.progressBar)).perform(delay())
    }

    @Test
    @Throws(Exception::class)
    fun spanish() {
        // SocialView.setMentionPattern(SocialView.PATTERN_SPANISH)
        onView(withId(R.id.editText)).perform(
                replaceText(NAMES),
                toast("spanish"),
                closeSoftKeyboard())
        onView(withId(R.id.progressBar)).perform(delay())
    }

    @Test
    @Throws(Exception::class)
    fun russian() {
        // SocialView.setMentionPattern(SocialView.PATTERN_RUSSIAN)
        onView(withId(R.id.editText)).perform(
                replaceText(NAMES),
                toast("russian"),
                closeSoftKeyboard())
        onView(withId(R.id.progressBar)).perform(delay())
    }

    fun toast(text: CharSequence) = object : ViewAction {
        override fun getDescription() = "toast($text)"
        override fun getConstraints() = isDisplayed()
        override fun perform(uiController: UiController?, view: View) {
            view.context.toast(text)
        }
    }
}