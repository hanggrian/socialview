socialview
==========
Android TextView and EditText with hashtag, mention, and hyperlink support.

![demo](/art/demo.gif)

Download
--------
#### Core
Comes with `SocialTextView`, `SocialEditText`, and `SocialViewAttacher` to attach any TextView.
```gradle
dependencies {
    compile 'com.hendraanggrian:socialview-core:0.7.0'
}
```

#### Commons
Comes with all core features and `SocialAutoCompleteTextView` to display suggestions as you type.
```gradle
dependencies {
    compile 'com.hendraanggrian:socialview-commons:0.7.0'
}
```

Core
----
![SocialTextView](/art/screenshot_core1.jpg) ![SocialEditText](/art/screenshot_core2.jpg)

Put the view in xml.
```xml
<com.hendraanggrian.widget.SocialTextView
    android:id="@+id/socialtextview"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="#hashtag and @mention."/>
```

Modify its state and set listeners in java.
```java
SocialTextView textView = (SocialTextView) findViewById(R.id.socialtextview);
textView.setHashtagColor(ContextCompat.getColor(this, R.color.red));
textView.setMentionColorRes(R.color.blue);
textView.setHashtagEnabled(false);
textView.setOnSocialClickListener(new SocialView.OnSocialClickListener() {
    @Override
    public void(View v, SocialView.Type type, CharSequence text) {
        // TODO: do something
    }
});
```

Any TextView or subclasses of TextView can be attached.
```java
CustomTextView tv = ...;
SocialView socialView = SocialViewAttacher.attach(tv);
socialView.setHashtagColorRes(R.color.red);
socialView.setMentionEnabled(false);
```

Full list of available attributes:
 * typeEnabled - types to enable, by default all types are enabled.
 * typeUnderlined - types to underlined, by default only hyperlink are underlined.
 * hashtagColor - by default color accent of current app theme is used.
 * mentionColor - by default color accent of current app theme is used.
 * hyperlinkColor - by default color accent of current app theme is used.

Commons
-------
![SocialAutoCompleteTextView hashtag suggestions](/art/screenshot_commons1.jpg) ![SocialAutoCompleteTextView mention suggestions](/art/screenshot_commons2.jpg)

Comes with `SocialAutoCompleteTextView` and all the interfaces, models, and adapters necessary to use it.
```xml
<com.hendraanggrian.widget.SocialAutoCompleteTextView
    android:id="@+id/socialsuggestionedittext"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="What's on your mind?"/>
```

To display suggestions, it is required to `setHashtagAdapter()` and `setMentionAdapter()`.
```java
SocialAutoCompleteTextView<Hashtag, Mention> textView = (SocialAutoCompleteTextView) findViewById(R.id.socialsuggestionedittext);
textView.setHashtagAdapter(new HashtagAdapter(getContext())); // or use custom adapter
textView.setMentionAdapter(new MentionAdapter(getContext())); // or use custom adapter

textView.getHashtagAdapter().add(new Hashtag("follow"));
textView.getHashtagAdapter().add(new Hashtag("followme", 1000));
textView.getHashtagAdapter().add(new Interest("followmeorillkillyou", 500));

textView.getMentionAdapter().add(new Mention("dirtyhobo"));
textView.getMentionAdapter().add(new Mention("hobo", "Regular Hobo", R.mipmap.ic_launcher));
textView.getMentionAdapter().add(new Mention("hendraanggrian", "Hendra Anggrian", "https://avatars0.githubusercontent.com/u/11507430?v=3&s=460"));
```
