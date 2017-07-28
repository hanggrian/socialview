package com.hendraanggrian.socialview.core.test

import android.os.Build
import android.os.CountDownTimer
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.view.View
import android.widget.ProgressBar

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
abstract class BaseTest {

    companion object {
        private val DELAY_COUNTDOWN = 3000L
    }

    protected fun delay(): ViewAction {
        return object : ViewAction {
            override fun getConstraints() = isAssignableFrom(ProgressBar::class.java)
            override fun getDescription() = "delay for $DELAY_COUNTDOWN"
            override fun perform(uiController: UiController, view: View) {
                val progressBar = view as ProgressBar
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