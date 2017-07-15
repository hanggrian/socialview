package com.hendraanggrian.socialview.core.test

import android.os.Build
import android.os.CountDownTimer
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import org.hamcrest.Matcher
import org.junit.runner.RunWith

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
@RunWith(AndroidJUnit4::class)
abstract class BaseTest {

    companion object {
        private val DELAY_COUNTDOWN: Long = 3000
    }

    abstract fun getRule(): ActivityTestRule<InstrumentedActivity>

    protected fun delay(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isDisplayed()
            }

            override fun getDescription(): String {
                return "delay for " + DELAY_COUNTDOWN
            }

            override fun perform(uiController: UiController, view: View) {
                val progressBar = getRule().activity.progressBar
                progressBar.progress = 100
                progressBar.visibility = View.VISIBLE
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
}