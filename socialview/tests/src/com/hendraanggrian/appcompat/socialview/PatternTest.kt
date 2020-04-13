package com.hendraanggrian.appcompat.socialview

import android.view.View
import android.widget.Toast
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.hendraanggrian.appcompat.socialview.activity.InstrumentedActivity
import com.hendraanggrian.appcompat.socialview.test.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
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
            closeSoftKeyboard()
        )
        onView(withId(R.id.progressBar)).perform(delay())
    }

    private fun toast(text: CharSequence) = object : ViewAction {
        override fun getDescription() = "toast($text)"
        override fun getConstraints() = isDisplayed()
        override fun perform(uiController: UiController?, view: View) {
            Toast.makeText(view.context, text, Toast.LENGTH_SHORT).show()
        }
    }
}