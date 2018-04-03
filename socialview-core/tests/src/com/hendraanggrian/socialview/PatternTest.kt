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
import androidx.core.widget.toast
import com.hendraanggrian.socialview.activity.InstrumentedActivity
import com.hendraanggrian.socialview.test.R
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters.JVM

@RunWith(AndroidJUnit4::class)
@LargeTest
@FixMethodOrder(JVM)
class PatternTest : AbstractTest() {

    @Rule @JvmField var rule = ActivityTestRule(InstrumentedActivity::class.java)

    private val names = StringBuilder()
        .appendln("@HendraAnggrian")
        .appendln("@jeffersonlicet")
        .appendln("@sfucko")
        // https://github.com/HendraAnggrian/socialview/issues/8
        .appendln()
        .appendln("@Mañana")
        .appendln("@CreaciónDivina")
        .appendln("@TúVendrás")
        .appendln("@CuatroArtículos")
        .appendln("@MásNadaQueda")
        .appendln("@SeFueÉste")
        .appendln("@ÉsteNoEra")
        .appendln("@Ñame")
        // https://github.com/HendraAnggrian/socialview/issues/13
        .appendln()
        .appendln("@Андрей")
        .appendln("@Владимир")
        .appendln("@Дмитрий")
        .appendln("@Вова")
        .appendln("@Саша")
        .toString()

    @Test fun default() {
        onView(withId(R.id.editText)).perform(
            replaceText(names),
            toast("default"),
            closeSoftKeyboard())
        onView(withId(R.id.progressBar)).perform(delay())
    }

    private fun toast(text: CharSequence) = object : ViewAction {
        override fun getDescription() = "toast($text)"
        override fun getConstraints() = isDisplayed()
        override fun perform(uiController: UiController?, view: View) {
            view.context.toast(text)
        }
    }
}