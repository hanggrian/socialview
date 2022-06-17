package com.hendraanggrian.appcompat.socialview

import android.text.method.LinkMovementMethod
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.hendraanggrian.appcompat.socialview.activity.InstrumentedActivity
import com.hendraanggrian.appcompat.socialview.test.R
import com.hendraanggrian.appcompat.widget.SocialEditText
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MovementMethodTest {
    @Rule @JvmField var rule = ActivityTestRule(InstrumentedActivity::class.java)

    @Test
    fun setOnClickListener() {
        onView(withId(R.id.editText)).perform(object : ViewAction {
            override fun getConstraints() = ViewMatchers.isAssignableFrom(SocialEditText::class.java)
            override fun getDescription() = "Setting click listener"
            override fun perform(uiController: UiController, view: View) {
                view as SocialEditText
                assertFalse(view.movementMethod is LinkMovementMethod)

                view.setOnHashtagClickListener { _, _ -> }
                assertTrue(view.movementMethod is LinkMovementMethod)

                view.setOnHashtagClickListener(null)
                assertFalse(view.movementMethod is LinkMovementMethod)
            }
        })
    }
}
