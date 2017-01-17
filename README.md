![logo](/art/logo.png) SocialView
=================================

![demo](/art/demo.gif)

Download
--------

#### Core

The `core` module comes with basic `SocialTextView` and `SocialEditText`.
It also contains `SocialView` to implement hashtag and mention support to any view extending `TextView`.

```gradle
compile 'io.github.hendraanggrian:socialview-core:0.2.7'
```

#### Commons

The `commons` module has all `core` module components plus `SocialSuggestionEditText` that displays hashtag and mention suggestions as you type.
You can use custom model extending `Hashtagable` and `Mentionable`, or even use your adapter extending `SuggestionAdapter<Hashtagable>` and `SuggestionAdapter<Mentionable>`.

```gradle
compile 'io.github.hendraanggrian:socialview-commons:0.2.7'
```

Core
----

<img src="/art/ss_core1.png" width="256">
<img src="/art/ss_core2.png" width="256">

Comes with `SocialTextView` and `SocialEditText`.

```xml
<io.github.hendraanggrian.socialview.SocialTextView
    android:id="@+id/socialtextview"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="#hashtag and @mention."
    app:hashtagColor="@color/blue" // if not set, color accent of current app theme is used
    app:mentionColor="@color/red"  // if not set, color accent of current app theme is used
    app:hashtagEnabled="true"      // true by default
    app:mentionEnabled="false"     // true by default
    />
```

You can also change them programatically.

```java
SocialTextView socialTextView = (SocialTextView) findViewById(R.id.socialtextview);
socialTextView.setHashtagColor(ContextCompat.getColor(this, R.color.red));
socialTextView.setMentionColorRes(R.color.blue);
socialTextView.setHashtagEnabled(false);

// set listener
socialTextView.setOnHashtagClickListener(new SocialView.OnSocialClickListener() {
    @Override
    public void(View v, String clicked) {
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

<img src="/art/ss_commons1.png" width="256">
<img src="/art/ss_commons2.png" width="256">

Comes with `SocialSuggestionEditText` and all the interfaces, models, and adapters necessary to use it.

```xml
<io.github.hendraanggrian.socialview.SocialSuggestionEditText
    android:id="@+id/socialsuggestionedittext"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="What's on your mind?"
    app:hashtagColor="@color/blue" // if not set, color accent of current app theme is used
    app:mentionColor="@color/red"  // if not set, color accent of current app theme is used
    app:hashtagEnabled="true"      // true by default
    app:mentionEnabled="false"     // true by default
/>
```

To display suggestions, it is required to `setHashtagAdapter()` and `setMentionAdapter()`.

```java
SocialSuggestionEditText editText = (SocialSuggestionEditText) view.findViewById(R.id.socialsuggestionedittext);
editText.setHashtagAdapter(new HashtagAdapter(getContext())); // or use custom adapter extending SuggestionAdapter<Hashtagable>
editText.setMentionAdapter(new MentionAdapter(getContext())); // or use custom adapter extending SuggestionAdapter<Mentionable>

// use default item Hashtag
editText.getHashtagAdapter().add(new Hashtag("follow"));
// a hashtag can have count
editText.getHashtagAdapter().add(new Hashtag("followme", 1000));
// or use custom model implementing Hashtagable
editText.getHashtagAdapter().add(new Interest("followmeorillkillyou", 500));

// use default item Mention
editText.getMentionAdapter().add(new Mention("dirtyhobo"));
// a mention can have display name and avatar (can be drawable or string url)
editText.getMentionAdapter().add(new Mention("hobo", "Regular Hobo", R.mipmap.ic_launcher));
// or use custom model implementing Mentionable
editText.getMentionAdapter().add(new User("hendraanggrian", "Hendra Anggrian", "https://avatars0.githubusercontent.com/u/11507430?v=3&s=460"));
```
