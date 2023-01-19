package com.hendraanggrian.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import com.hendraanggrian.appcompat.internal.SocialViewHelper;
import java.util.List;
import java.util.regex.Pattern;

/**
 * {@link android.widget.EditText} with hashtag, mention, and hyperlink support.
 *
 * @see SocialView
 */
public class SocialEditText extends AppCompatEditText implements SocialView {
  private final SocialView helper;

  public SocialEditText(Context context) {
    this(context, null);
  }

  public SocialEditText(Context context, AttributeSet attrs) {
    this(context, attrs, androidx.appcompat.R.attr.editTextStyle);
  }

  public SocialEditText(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    helper = SocialViewHelper.install(this, attrs);
  }

  /** {@inheritDoc} */
  @NonNull
  @Override
  public Pattern getHashtagPattern() {
    return helper.getHashtagPattern();
  }

  /** {@inheritDoc} */
  @NonNull
  @Override
  public Pattern getMentionPattern() {
    return helper.getMentionPattern();
  }

  /** {@inheritDoc} */
  @NonNull
  @Override
  public Pattern getHyperlinkPattern() {
    return helper.getHyperlinkPattern();
  }

  /** {@inheritDoc} */
  @Override
  public void setHashtagPattern(@Nullable Pattern pattern) {
    helper.setHashtagPattern(pattern);
  }

  /** {@inheritDoc} */
  @Override
  public void setMentionPattern(@Nullable Pattern pattern) {
    helper.setMentionPattern(pattern);
  }

  /** {@inheritDoc} */
  @Override
  public void setHyperlinkPattern(@Nullable Pattern pattern) {
    helper.setHyperlinkPattern(pattern);
  }

  /** {@inheritDoc} */
  @Override
  public boolean isHashtagEnabled() {
    return helper.isHashtagEnabled();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isMentionEnabled() {
    return helper.isMentionEnabled();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isHyperlinkEnabled() {
    return helper.isHyperlinkEnabled();
  }

  /** {@inheritDoc} */
  @Override
  public void setHashtagEnabled(boolean enabled) {
    helper.setHashtagEnabled(enabled);
  }

  /** {@inheritDoc} */
  @Override
  public void setMentionEnabled(boolean enabled) {
    helper.setMentionEnabled(enabled);
  }

  /** {@inheritDoc} */
  @Override
  public void setHyperlinkEnabled(boolean enabled) {
    helper.setHyperlinkEnabled(enabled);
  }

  /** {@inheritDoc} */
  @NonNull
  @Override
  public ColorStateList getHashtagColors() {
    return helper.getHashtagColors();
  }

  /** {@inheritDoc} */
  @NonNull
  @Override
  public ColorStateList getMentionColors() {
    return helper.getMentionColors();
  }

  /** {@inheritDoc} */
  @NonNull
  @Override
  public ColorStateList getHyperlinkColors() {
    return helper.getHyperlinkColors();
  }

  /** {@inheritDoc} */
  @Override
  public void setHashtagColors(@NonNull ColorStateList colors) {
    helper.setHashtagColors(colors);
  }

  /** {@inheritDoc} */
  @Override
  public void setMentionColors(@NonNull ColorStateList colors) {
    helper.setMentionColors(colors);
  }

  /** {@inheritDoc} */
  @Override
  public void setHyperlinkColors(@NonNull ColorStateList colors) {
    helper.setHyperlinkColors(colors);
  }

  /** {@inheritDoc} */
  @ColorInt
  @Override
  public int getHashtagColor() {
    return helper.getHashtagColor();
  }

  /** {@inheritDoc} */
  @ColorInt
  @Override
  public int getMentionColor() {
    return helper.getMentionColor();
  }

  /** {@inheritDoc} */
  @ColorInt
  @Override
  public int getHyperlinkColor() {
    return helper.getHyperlinkColor();
  }

  /** {@inheritDoc} */
  @Override
  public void setHashtagColor(@ColorInt int color) {
    helper.setHashtagColor(color);
  }

  /** {@inheritDoc} */
  @Override
  public void setMentionColor(@ColorInt int color) {
    helper.setMentionColor(color);
  }

  /** {@inheritDoc} */
  @Override
  public void setHyperlinkColor(@ColorInt int color) {
    helper.setHyperlinkColor(color);
  }

  /** {@inheritDoc} */
  @Override
  public void setOnHashtagClickListener(@Nullable SocialView.OnClickListener listener) {
    helper.setOnHashtagClickListener(listener);
  }

  /** {@inheritDoc} */
  @Override
  public void setOnMentionClickListener(@Nullable SocialView.OnClickListener listener) {
    helper.setOnMentionClickListener(listener);
  }

  /** {@inheritDoc} */
  @Override
  public void setOnHyperlinkClickListener(@Nullable SocialView.OnClickListener listener) {
    helper.setOnHyperlinkClickListener(listener);
  }

  /** {@inheritDoc} */
  @Override
  public void setHashtagTextChangedListener(@Nullable OnChangedListener listener) {
    helper.setHashtagTextChangedListener(listener);
  }

  /** {@inheritDoc} */
  @Override
  public void setMentionTextChangedListener(@Nullable OnChangedListener listener) {
    helper.setMentionTextChangedListener(listener);
  }

  /** {@inheritDoc} */
  @NonNull
  @Override
  public List<String> getHashtags() {
    return helper.getHashtags();
  }

  /** {@inheritDoc} */
  @NonNull
  @Override
  public List<String> getMentions() {
    return helper.getMentions();
  }

  /** {@inheritDoc} */
  @NonNull
  @Override
  public List<String> getHyperlinks() {
    return helper.getHyperlinks();
  }
}
