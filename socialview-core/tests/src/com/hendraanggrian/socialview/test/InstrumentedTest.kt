package com.hendraanggrian.socialview.test

import android.os.Build
import android.os.CountDownTimer
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import org.hamcrest.Matcher
import org.jetbrains.anko.toast
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class InstrumentedTest {

    @Rule
    var rule = ActivityTestRule(InstrumentedActivity::class.java)

    @Test
    fun test1_introduction() {
        onView(withId(R.id.editText)).perform(
                typeText("This is a standard TextView with #hashtag, @mention, and http://some.url support."),
                closeSoftKeyboard(),
                delay())
    }

    @Test
    fun test2_withoutMention() {
        rule.activity.editText!!.isMentionEnabled = false
        onView(withId(R.id.editText)).perform(
                typeText("You can disable @mention to only have #hashtag."),
                closeSoftKeyboard(),
                delay())
    }

    @Test
    fun test3_customColors() {
        rule.activity.editText!!.setHashtagColorRes(android.R.color.holo_red_light)
        rule.activity.editText!!.setMentionColorRes(android.R.color.holo_green_light)
        rule.activity.editText!!.setHyperlinkColorRes(android.R.color.holo_blue_light)
        onView(withId(R.id.editText)).perform(
                typeText("Accent color of current app theme is used by default. But you can also have separate color for #hashtag, @mention, and http://hyperlink.com."),
                closeSoftKeyboard(),
                delay())
    }

    @Test
    fun test4_clickable() {
        rule.activity.editText!!.setOnHashtagClickListener { _, charSequence ->
            rule.activity.toast(charSequence.toString())
        }
        onView(withId(R.id.editText)).perform(
                typeText("Oh, they are also #clickable!"),
                closeSoftKeyboard(),
                delay())
    }

    /**
     * https://github.com/HendraAnggrian/socialview/issues/8
     */
    @Test
    fun test5_englishSpanishCustom() {
        onView(withId(R.id.editText)).perform(
                replaceText("English:\n@Manana | @CreacionDivina | @TuVendras | @CuatroArticulos | @MasNadaQueda | @SeFueEste | @EsteNoEra | @Name\n\n" + "Spanish:\n@Mañana | @CreaciónDivina | @TúVendrás | @CuatroArtículos | @MásNadaQueda | @SeFueÉste | @ÉsteNoEra | @Ñame"),
                closeSoftKeyboard(),
                delay())
    }

    fun delay(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isDisplayed()
            }

            override fun getDescription(): String {
                return "delay for " + DELAY_COUNTDOWN
            }

            override fun perform(uiController: UiController, view: View) {
                val progressBar = rule.activity.progressBar!!
                object : CountDownTimer(DELAY_COUNTDOWN, 100) {
                    override fun onTick(millisUntilFinished: Long) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            progressBar.setProgress((progressBar.max * millisUntilFinished / DELAY_COUNTDOWN).toInt(), true)
                        } else {
                            progressBar.progress = (progressBar.max * millisUntilFinished / DELAY_COUNTDOWN).toInt()
                        }
                    }

                    override fun onFinish() {
                        progressBar.visibility = View.GONE
                    }
                }.start()
                uiController.loopMainThreadForAtLeast(DELAY_COUNTDOWN)
            }
        }
    }

    companion object {

        private val DELAY_COUNTDOWN: Long = 3000
    }
}