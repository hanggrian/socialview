package com.hendraanggrian.socialview

import android.os.Build.VERSION.SDK_INT
import android.os.CountDownTimer
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions.closeSoftKeyboard
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.hendraanggrian.widget.HashtagAdapter
import com.hendraanggrian.widget.MentionAdapter
import com.hendraanggrian.widget.SocialAutoCompleteTextView
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters.JVM

@RunWith(AndroidJUnit4::class)
@LargeTest
@FixMethodOrder(JVM)
class InstrumentedTest {

    @Rule @JvmField var rule = ActivityTestRule(InstrumentedActivity::class.java)

    @Test
    @Throws(Exception::class)
    fun hashtag() {
        onView(ViewMatchers.withId(com.hendraanggrian.socialview.commons.test.R.id.textView)).perform(
                object : ViewAction {
                    override fun getConstraints() = isAssignableFrom(SocialAutoCompleteTextView::class.java)
                    override fun getDescription() = SocialAutoCompleteTextView::class.java.name
                    override fun perform(uiController: UiController, view: View) {
                        val adapter = HashtagAdapter(view.context)
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

    @Test
    @Throws(Exception::class)
    fun mention() {
        onView(ViewMatchers.withId(com.hendraanggrian.socialview.commons.test.R.id.textView)).perform(
                object : ViewAction {
                    override fun getConstraints() = isAssignableFrom(SocialAutoCompleteTextView::class.java)
                    override fun getDescription() = SocialAutoCompleteTextView::class.java.name
                    override fun perform(uiController: UiController, view: View) {
                        val adapter = MentionAdapter(view.context)
                        adapter.addAll(
                                Mention("dirtyhobo"),
                                Mention("hobo", "Regular Hobo", android.R.drawable.ic_input_add),
                                Mention("hendraanggrian", "Hendra Anggrian", "https://avatars0.githubusercontent.com/u/11507430?v=3&s=460"))
                        (view as SocialAutoCompleteTextView).mentionAdapter = adapter
                        view.threshold = 1
                    }
                },
                typeText("Mention someone with avatar picture from local file or network, like @h"),
                closeSoftKeyboard(),
                delay())
    }

    @Test
    @Throws(Exception::class)
    fun custom() {
        onView(ViewMatchers.withId(com.hendraanggrian.socialview.commons.test.R.id.textView)).perform(
                object : ViewAction {
                    override fun getConstraints() = isAssignableFrom(SocialAutoCompleteTextView::class.java)
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

    fun delay(): ViewAction {
        return object : ViewAction {
            override fun getConstraints() = isDisplayed()
            override fun getDescription() = "delay for $DELAY_COUNTDOWN"
            override fun perform(uiController: UiController, view: View) {
                val progressBar = rule.activity.progressBar
                object : CountDownTimer(DELAY_COUNTDOWN, 100) {
                    override fun onTick(millisUntilFinished: Long) = when {
                        SDK_INT >= 24 -> progressBar.setProgress((progressBar.max * millisUntilFinished / DELAY_COUNTDOWN).toInt(), true)
                        else -> progressBar.progress = (progressBar.max * millisUntilFinished / DELAY_COUNTDOWN).toInt()
                    }

                    override fun onFinish() {
                        progressBar.isVisible = false
                    }
                }.start()
                uiController.loopMainThreadForAtLeast(DELAY_COUNTDOWN)
            }
        }
    }

    companion object {
        private val DELAY_COUNTDOWN: Long = 5000
    }
}