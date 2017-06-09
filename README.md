socialview
==========
Android TextView and EditText with hashtag, mention, and hyperlink support.

![demo][demo]

Core
----
Lightweight library that comes with `SocialTextView`, `SocialEditText` and `SocialViewImpl` to attach any TextView.
```xml
<com.hendraanggrian.widget.SocialTextView
    android:id="@+id/socialtextview"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="#hashtag and @mention."/>
```

![SocialTextView][core1] ![SocialEditText][core2]

Modify its state and set listeners in java.
```java
SocialTextView textView = (SocialTextView) findViewById(R.id.socialtextview);
textView.setMentionEnabled(false);
textView.setHashtagColor(ContextCompat.getColor(this, R.color.red));
textView.setOnHashtagClickListener(new SocialView.OnSocialClickListener() {
    @Override
    public void(View v, String text) {
        // TODO: do something
    }
});
```

Any TextView or subclasses of TextView can be attached.
```java
CustomTextView tv = ...;
SocialView socialView = SocialViewImpl.attach(tv);
```

#### Attributes
| Attributes       | Description                      | Default value/behavior        |
|------------------|----------------------------------|-------------------------------|
| `typeEnabled`    | flags to enable span coloring    | `hashtag\|mention\|hyperlink` |
| `hashtagColor`   | color of hashtag items           | current theme's accent color  |
| `mentionColor`   | color of mention items           | current theme's accent color  |
| `hyperlinkColor` | color of hyperlink items         | current theme's accent color  |

Commons
-------
Extended core library with `SocialEditText` and `SocialAutoCompleteTextView` to display suggestions as you type.
```xml
<com.hendraanggrian.widget.SocialAutoCompleteTextView
    android:id="@+id/socialsuggestionedittext"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="What's on your mind?"/>
```

![SocialAutoCompleteTextView hashtag suggestions][commons1] ![SocialAutoCompleteTextView mention suggestions][commons2]

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

Download
--------
```gradle
repositories {
    jcenter()
}

dependencies {
    // core only
    compile 'com.hendraanggrian:socialview-core:0.11.1'
    // core and commons
    compile 'com.hendraanggrian:socialview-commons:0.11.1'
}
```

License
-------
    Copyright 2016 Hendra Anggrian

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
[demo]: /art/demo.gif
[core1]: /art/ss_core1.jpg
[core1]: /art/ss_core2.jpg
[commons1]: /art/ss_commons1.jpg
[commons2]: /art/ss_commons2.jpg