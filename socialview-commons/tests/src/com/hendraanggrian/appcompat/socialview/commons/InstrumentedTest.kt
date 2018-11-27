package com.hendraanggrian.appcompat.socialview.commons

import android.os.Build.VERSION.SDK_INT
import android.os.CountDownTimer
import android.view.View
import android.view.View.GONE
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.hendraanggrian.appcompat.socialview.Hashtag
import com.hendraanggrian.appcompat.socialview.Mention
import com.hendraanggrian.appcompat.socialview.commons.activity.InstrumentedActivity
import com.hendraanggrian.appcompat.socialview.commons.test.R
import com.hendraanggrian.appcompat.widget.HashtagArrayAdapter
import com.hendraanggrian.appcompat.widget.MentionArrayAdapter
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class InstrumentedTest {

    @Rule @JvmField var rule = ActivityTestRule(InstrumentedActivity::class.java)

    @Test fun hashtag() {
        onView(withId(R.id.textView))
            .perform(
                object : ViewAction {
                    override fun getConstraints() =
                        isAssignableFrom(SocialAutoCompleteTextView::class.java)

                    override fun getDescription() = SocialAutoCompleteTextView::class.java.name
                    override fun perform(uiController: UiController, view: View) {
                        val adapter = HashtagArrayAdapter<Hashtag>(view.context)
                        adapter.addAll(
                            Hashtag("follow"),
                            Hashtag("followme", 1000),
                            Hashtag("followmeorillkillyou", 500))
                        (view as SocialAutoCompleteTextView).hashtagAdapter = adapter
                    }
                },
                typeText("Suggestions can popup with SocialAutoCompleteTextView, like #foll"),
                closeSoftKeyboard(),
                delay())
    }

    @Test fun mention() {
        onView(withId(R.id.textView))
            .perform(
                object : ViewAction {
                    override fun getConstraints() =
                        isAssignableFrom(SocialAutoCompleteTextView::class.java)

                    override fun getDescription() = SocialAutoCompleteTextView::class.java.name
                    override fun perform(uiController: UiController, view: View) {
                        val adapter = MentionArrayAdapter<Mention>(view.context)
                        adapter.addAll(
                            Mention("dirtyhobo"),
                            Mention("hobo", "Regular Hobo", android.R.drawable.ic_btn_speak_now),
                            Mention("hendraanggrian", "Hendra Anggrian",
                                "https://avatars1.githubusercontent.com/u/11507430?s=460&v=4"))
                        (view as SocialAutoCompleteTextView).mentionAdapter = adapter
                        view.threshold = 1
                    }
                },
                typeText("Mention someone with avatar picture from local file or network, like @h"),
                closeSoftKeyboard(),
                delay())
    }

    @Test fun custom() {
        onView(withId(R.id.textView))
            .perform(
                object : ViewAction {
                    override fun getConstraints() =
                        isAssignableFrom(SocialAutoCompleteTextView::class.java)

                    override fun getDescription() = SocialAutoCompleteTextView::class.java.name
                    override fun perform(uiController: UiController, view: View) {
                        val adapter = PersonAdapter(view.context)
                        adapter.addAll(
                            Person("dirtyhobo"),
                            Person("hobo"),
                            Person("hendraanggrian"))
                        (view as SocialAutoCompleteTextView).mentionAdapter = adapter
                        view.threshold = 1
                    }
                },
                typeText("Customize your adapter, like @h"),
                closeSoftKeyboard(),
                delay())
    }

    private fun delay(): ViewAction = object : ViewAction {
        override fun getConstraints() = isDisplayed()
        override fun getDescription() = "delay for $DELAY_COUNTDOWN"
        override fun perform(uiController: UiController, view: View) {
            val progressBar = rule.activity.progressBar
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
        const val DELAY_COUNTDOWN: Long = 5000
    }
}