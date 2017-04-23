socialview
==========
Android TextView and EditText with hashtag, mention, and hyperlink support.

![demo](/art/demo.gif)

Download
--------
#### Core
 * `SocialTextView` and `SocialEditText`.
 * Attach any `TextView` with `SocialViewAttacher`.
 * Create custom `TextView` by implementing `SocialView`
```gradle
dependencies {
    compile 'com.hendraanggrian:socialview-core:0.6.1'
}
```

#### Commons
 * All of core features
 * `SocialAutoCompleteTextView` for displaying suggestions as you type.
```gradle
dependencies {
    compile 'com.hendraanggrian:socialview-commons:0.6.1'
}
```

Core
----
![SocialTextView](/art/screenshot_core1.jpg) ![SocialEditText](/art/screenshot_core2.jpg)

Comes with `SocialTextView` and `SocialEditText`.
```xml
<com.hendraanggrian.widget.SocialTextView
    android:id="@+id/socialtextview"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="#hashtag and @mention."
    app:hashtagEnabled="true"      // true by default
    app:mentionEnabled="false"     // true by default
    app:hyperlinkEnabled="false"     // true by default
    app:hashtagColor="@color/blue" // if not set, color accent of current app theme is used
    app:mentionColor="@color/red"  // if not set, color accent of current app theme is used
    app:hyperlinkColor="@color/red"  // if not set, color accent of current app theme is used
    />
```

You can also change them programatically.
```java
SocialTextView textView = (SocialTextView) findViewById(R.id.socialtextview);
textView.setHashtagColor(ContextCompat.getColor(this, R.color.red));
textView.setMentionColorRes(R.color.blue);
textView.setHashtagEnabled(false);

// set listener
textView.setOnSocialClickListener(new SocialView.OnSocialClickListener() {
    @Override
    public void(View v, SocialView.Type type, String clicked) {
        // do something
    }
});
```

Note that `SocialTextView` and `SocialEditText` are basically just `TextView` and `EditText` that implement `SocialViewBase` of which overriden methods are passed for `SocialView` to process inside each of them. With this in mind, `SocialView` can be attached to any `TextView` or view extending `TextView`:
```java
CustomTextView textView = ...;
SocialViewAttacher attacher = SocialViewAttacher.attach(textView);
attacher.setHashtagColorRes(R.color.red);
attacher.setMentionEnabled(false);
attacher.setOnHashtagClickListener((view, clicked) -> {});
// etc.
```

Commons
-------
![SocialAutoCompleteTextView hashtag suggestions](/art/screenshot_commons1.jpg) ![SocialAutoCompleteTextView mention suggestions](/art/screenshot_commons2.jpg)

Comes with `SocialAutoCompleteTextView` and all the interfaces, models, and adapters necessary to use it.
```xml
<com.hendraanggrian.widget.SocialAutoCompleteTextView
    android:id="@+id/socialsuggestionedittext"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="What's on your mind?"
    app:hashtagEnabled="true"      // true by default
    app:mentionEnabled="false"     // true by default
    app:hyperlinkEnabled="false"     // true by default
    app:hashtagColor="@color/blue" // if not set, color accent of current app theme is used
    app:mentionColor="@color/red"  // if not set, color accent of current app theme is used
    app:hyperlinkColor="@color/red"  // if not set, color accent of current app theme is used
/>
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
