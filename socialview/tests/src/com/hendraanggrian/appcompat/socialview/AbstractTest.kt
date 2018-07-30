package com.hendraanggrian.appcompat.socialview

import android.os.Build.VERSION.SDK_INT
import android.os.CountDownTimer
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom

abstract class AbstractTest {

    protected fun delay(): ViewAction = object : ViewAction {
        override fun getConstraints() = isAssignableFrom(ProgressBar::class.java)
        override fun getDescription() = "delay for $DELAY_COUNTDOWN"
        override fun perform(uiController: UiController, view: View) {
            val progressBar = view as ProgressBar
            progressBar.progress = 100
            progressBar.visibility = VISIBLE
            object : CountDownTimer(DELAY_COUNTDOWN, 100) {
                override fun onTick(millisUntilFinished: Long) = (progressBar.max *
                    millisUntilFinished / DELAY_COUNTDOWN).toInt().let { progress ->
                    when {
                        SDK_INT >= 24 -> progressBar.setProgress(progress, true)
                        else -> progressBar.progress = progress
                    }
                }

                override fun onFinish() {
                    progressBar.visibility = GONE
                }
            }.start()
            uiController.loopMainThreadForAtLeast(DELAY_COUNTDOWN)
        }
    }

    private companion object {
        const val DELAY_COUNTDOWN = 3000L
    }
}