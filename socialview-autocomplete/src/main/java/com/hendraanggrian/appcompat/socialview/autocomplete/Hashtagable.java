package com.hendraanggrian.appcompat.socialview.autocomplete;

import androidx.annotation.NonNull;
import com.hendraanggrian.appcompat.socialview.widget.HashtagArrayAdapter;

/**
 * Abstract hashtag to be used with {@link HashtagArrayAdapter}.
 */
public interface Hashtagable {
  /** Unique id of this hashtag. */
  @NonNull
  CharSequence getId();

  /** Optional count, located right to hashtag name. */
  int getCount();
}
