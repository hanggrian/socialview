package com.hendraanggrian.appcompat.widget;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/** Simple optional mention data class, use when there is no custom mention class. */
public class Mention implements Mentionable {
  private final CharSequence username;
  private final CharSequence displayname;
  private final Object avatar;

  public Mention(@NonNull CharSequence username) {
    this(username, null);
  }

  public Mention(@NonNull CharSequence username, @Nullable CharSequence displayname) {
    this(username, displayname, null);
  }

  public Mention(
      @NonNull CharSequence username, @Nullable CharSequence displayname, @Nullable Object avatar) {
    this.username = username;
    this.displayname = displayname;
    this.avatar = avatar;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    return obj instanceof Mention && ((Mention) obj).username == username;
  }

  @Override
  public int hashCode() {
    return username.hashCode();
  }

  @NonNull
  @Override
  public String toString() {
    return username.toString();
  }

  /** {@inheritDoc} */
  @NonNull
  @Override
  public CharSequence getUsername() {
    return username;
  }

  /** {@inheritDoc} */
  @Nullable
  @Override
  public CharSequence getDisplayname() {
    return displayname;
  }

  /** {@inheritDoc} */
  @Nullable
  @Override
  public Object getAvatar() {
    return avatar;
  }
}
