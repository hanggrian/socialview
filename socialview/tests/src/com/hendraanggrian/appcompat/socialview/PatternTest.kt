package com.hendraanggrian.appcompat.socialview

import android.view.View
import android.widget.Toast
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.hendraanggrian.appcompat.socialview.activity.InstrumentedActivity
import com.hendraanggrian.appcompat.socialview.test.R
import org.junit.Rule
import org.junit.runner.RunWith
import kotlin.test.Test

@RunWith(AndroidJUnit4::class)
@LargeTest
class PatternTest {
    @Rule @JvmField var rule = ActivityTestRule(InstrumentedActivity::class.java)

    private val names = buildString {
        appendLine("@HendraAnggrian")
        appendLine("@jeffersonlicet")
        appendLine("@sfucko")
        // https://github.com/HendraAnggrian/socialview/issues/8
        appendLine()
        appendLine("@Mañana")
        appendLine("@CreaciónDivina")
        appendLine("@TúVendrás")
        appendLine("@CuatroArtículos")
        appendLine("@MásNadaQueda")
        appendLine("@SeFueÉste")
        appendLine("@ÉsteNoEra")
        appendLine("@Ñame")
        // https://github.com/HendraAnggrian/socialview/issues/13
        appendLine()
        appendLine("@Андрей")
        appendLine("@Владимир")
        appendLine("@Дмитрий")
        appendLine("@Вова")
        appendLine("@Саша")
    }

    @Test
    fun default() {
        onView(withId(R.id.editText)).perform(
            replaceText(names),
            toast("default")
        )
    }

    private fun toast(text: CharSequence) = object : ViewAction {
        override fun getDescription() = "toast($text)"
        override fun getConstraints() = isDisplayed()
        override fun perform(uiController: UiController?, view: View) {
            Toast.makeText(view.context, text, Toast.LENGTH_SHORT).show()
        }
    }
}
