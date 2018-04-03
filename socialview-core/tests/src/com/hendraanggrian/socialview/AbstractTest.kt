package com.hendraanggrian.socialview

import android.os.Build.VERSION.SDK_INT
import android.os.CountDownTimer
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible

abstract class AbstractTest {

    protected fun delay(): ViewAction = object : ViewAction {
        override fun getConstraints() = isAssignableFrom(ProgressBar::class.java)
        override fun getDescription() = "delay for $DELAY_COUNTDOWN"
        override fun perform(uiController: UiController, view: View) {
            val progressBar = view as ProgressBar
            progressBar.progress = 100
            progressBar.isVisible = true
            object : CountDownTimer(DELAY_COUNTDOWN, 100) {
                override fun onTick(millisUntilFinished: Long) = (progressBar.max *
                    millisUntilFinished / DELAY_COUNTDOWN).toInt().let { progress ->
                    when {
                        SDK_INT >= 24 -> progressBar.setProgress(progress, true)
                        else -> progressBar.progress = progress
                    }
                }

                override fun onFinish() {
                    progressBar.isVisible = false
                }
            }.start()
            uiController.loopMainThreadForAtLeast(DELAY_COUNTDOWN)
        }
    }

    private companion object {
        const val DELAY_COUNTDOWN = 3000L
    }
}