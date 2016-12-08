SocialView: Coming Soon
=======================

![demo](/art/demo.gif)

Download
--------

### Core

The `core` module comes with basic `SocialTextView` and `SocialEditText`.
It also contains `SocialView` to implement hashtag and mention support to any view extending `TextView`.

```gradle
coming soon
```

### Commons

The `commons` module contains `SocialSuggestionEditText` that displays hashtag and mention suggestions as you type.
You can use custom model extending `Hashtagable` and `Mentionable`, or even use your adapter extending `SuggestionAdapter<Hashtagable>` and `SuggestionAdapter<Mentionable>`.

```gradle
coming soon
```

Core
----

<img src="/art/core1.png" width="256">
<img src="/art/core2.png" width="256">

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
SocialView socialView = SocialView.attach(textView);
socialView.setHashtagColorRes(R.color.red);
socialView.setMentionEnabled(false);
socialView.setOnHashtagClickListener((view, clicked) -> {});
// etc.
```

Commons
-------

<img src="/art/commons1.png" width="256">
<img src="/art/commons2.png" width="256">
