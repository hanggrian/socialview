package com.hendraanggrian.appcompat.socialview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/** Simple optional hashtag data class, use when there is no custom hashtag class. */
public class Hashtag implements Hashtagable {
  private final CharSequence id;
  private final int count;

  public Hashtag(@NonNull CharSequence hashtag) {
    this(hashtag, -1);
  }

  public Hashtag(@NonNull CharSequence hashtag, int count) {
    this.id = hashtag;
    this.count = count;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    return obj instanceof Hashtag && ((Hashtag) obj).id == id;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @NonNull
  @Override
  public String toString() {
    return id.toString();
  }

  /** {@inheritDoc} */
  @NonNull
  @Override
  public CharSequence getId() {
    return id;
  }

  /** {@inheritDoc} */
  @Override
  public int getCount() {
    return count;
  }
}
