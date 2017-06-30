package com.hendraanggrian.socialview.commons.test;

import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.hendraanggrian.socialview.commons.Hashtag;
import com.hendraanggrian.socialview.commons.HashtagAdapter;
import com.hendraanggrian.socialview.commons.Mention;
import com.hendraanggrian.socialview.commons.MentionAdapter;
import com.hendraanggrian.widget.SocialAutoCompleteTextView;

import org.hamcrest.Matcher;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InstrumentedTest {

    private static final long DELAY_COUNTDOWN = 5000;

    @Rule
    public ActivityTestRule<InstrumentedActivity> rule = new ActivityTestRule<>(InstrumentedActivity.class);

    @Test
    public void test1_hashtag() {
        onView(withId(R.id.textView)).perform(
                new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return isAssignableFrom(SocialAutoCompleteTextView.class);
                    }

                    @Override
                    public String getDescription() {
                        return SocialAutoCompleteTextView.class.getName();
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        ArrayAdapter<Hashtag> adapter = new HashtagAdapter(view.getContext());
                        adapter.addAll(
                                new Hashtag("follow"),
                                new Hashtag("followme", 1000),
                                new Hashtag("followmeorillkillyou", 500));
                        ((SocialAutoCompleteTextView) view).setHashtagAdapter(adapter);
                    }
                },
                typeText("Suggestions can popup with SocialAutoCompleteTextView, like #foll"),
                closeSoftKeyboard(),
                delay());
    }

    @Test
    public void test2_mention() {
        onView(withId(R.id.textView)).perform(
                new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return isAssignableFrom(SocialAutoCompleteTextView.class);
                    }

                    @Override
                    public String getDescription() {
                        return SocialAutoCompleteTextView.class.getName();
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        ArrayAdapter<Mention> adapter = new MentionAdapter(view.getContext());
                        adapter.addAll(
                                new Mention("dirtyhobo"),
                                new Mention("hobo", "Regular Hobo", android.R.drawable.ic_input_add),
                                new Mention("hendraanggrian", "Hendra Anggrian", "https://avatars0.githubusercontent.com/u/11507430?v=3&s=460"));
                        ((SocialAutoCompleteTextView) view).setMentionAdapter(adapter);
                        ((SocialAutoCompleteTextView) view).setThreshold(1);
                    }
                },
                typeText("Mention someone with avatar picture from local file or network, like @h"),
                closeSoftKeyboard(),
                delay());
    }

    @Test
    public void test3_custom() {
        onView(withId(R.id.textView)).perform(
                new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return isAssignableFrom(SocialAutoCompleteTextView.class);
                    }

                    @Override
                    public String getDescription() {
                        return SocialAutoCompleteTextView.class.getName();
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        ArrayAdapter<Person> adapter = new PersonAdapter(view.getContext());
                        adapter.addAll(
                                new Person("dirtyhobo"),
                                new Person("hobo"),
                                new Person("hendraanggrian"));
                        ((SocialAutoCompleteTextView) view).setMentionAdapter(adapter);
                        ((SocialAutoCompleteTextView) view).setThreshold(1);
                    }
                },
                typeText("Customize your adapter, like @h"),
                closeSoftKeyboard(),
                delay());
    }

    @NonNull
    public ViewAction delay() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "delay for " + DELAY_COUNTDOWN;
            }

            @Override
            public void perform(UiController uiController, View view) {
                final ProgressBar progressBar = rule.getActivity().progressBar;
                new CountDownTimer(DELAY_COUNTDOWN, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            progressBar.setProgress((int) (progressBar.getMax() * millisUntilFinished / DELAY_COUNTDOWN), true);
                        } else {
                            progressBar.setProgress((int) (progressBar.getMax() * millisUntilFinished / DELAY_COUNTDOWN));
                        }
                    }

                    @Override
                    public void onFinish() {
                        progressBar.setVisibility(View.GONE);
                    }
                }.start();
                uiController.loopMainThreadForAtLeast(DELAY_COUNTDOWN);
            }
        };
    }
}