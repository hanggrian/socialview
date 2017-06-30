package com.hendraanggrian.socialview.test;

import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.ProgressBar;

import com.hendraanggrian.socialview.OnSocialClickListener;
import com.hendraanggrian.socialview.SociableView;
import com.hendraanggrian.support.utils.widget.Toasts;

import org.hamcrest.Matcher;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * @author Hendra Anggrian (hendraanggrian@gmail.com)
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InstrumentedTest {

    private static final long DELAY_COUNTDOWN = 3000;

    @Rule
    public ActivityTestRule<InstrumentedActivity> rule = new ActivityTestRule<>(InstrumentedActivity.class);

    @Test
    public void test1_introduction() {
        onView(withId(R.id.editText)).perform(
                typeText("This is a standard TextView with #hashtag, @mention, and http://some.url support."),
                closeSoftKeyboard(),
                delay());
    }

    @Test
    public void test2_withoutMention() {
        rule.getActivity().editText.setMentionEnabled(false);
        onView(withId(R.id.editText)).perform(
                typeText("You can disable @mention to only have #hashtag."),
                closeSoftKeyboard(),
                delay());
    }

    @Test
    public void test3_customColors() {
        rule.getActivity().editText.setHashtagColorRes(android.R.color.holo_red_light);
        rule.getActivity().editText.setMentionColorRes(android.R.color.holo_green_light);
        rule.getActivity().editText.setHyperlinkColorRes(android.R.color.holo_blue_light);
        onView(withId(R.id.editText)).perform(
                typeText("Accent color of current app theme is used by default. But you can also have separate color for #hashtag, @mention, and http://hyperlink.com."),
                closeSoftKeyboard(),
                delay());
    }

    @Test
    public void test4_clickable() {
        rule.getActivity().editText.setOnHashtagClickListener(new OnSocialClickListener() {
            @Override
            public void onSocialClick(@NonNull SociableView v, @NonNull CharSequence text) {
                Toasts.showShort(rule.getActivity(), text.toString());
            }
        });
        onView(withId(R.id.editText)).perform(
                typeText("Oh, they are also #clickable!"),
                closeSoftKeyboard(),
                delay());
    }

    /**
     * https://github.com/HendraAnggrian/socialview/issues/8
     */
    @Test
    public void test5_englishSpanishCustom() {
        onView(withId(R.id.editText)).perform(
                replaceText("English:\n@Manana | @CreacionDivina | @TuVendras | @CuatroArticulos | @MasNadaQueda | @SeFueEste | @EsteNoEra | @Name\n\n" +
                        "Spanish:\n@Mañana | @CreaciónDivina | @TúVendrás | @CuatroArtículos | @MásNadaQueda | @SeFueÉste | @ÉsteNoEra | @Ñame"),
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