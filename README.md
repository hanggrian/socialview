socialview
==========
Android TextView and EditText with hashtag, mention, and hyperlink support.
Comes in 2 packages:
 * **core** - comes with `SocialTextView`, `SocialEditText` and `SocialViewImpl` to attach any TextView.
 * **commons** - extended core library with `SocialAutoCompleteTextView` to display suggestions as you type.

![demo][demo]

Download
--------
```gradle
repositories {
    google()
    jcenter()
}

dependencies {
    compile 'com.hendraanggrian:socialview-core:0.17'
    compile 'com.hendraanggrian:socialview-commons:0.17' // optional
}
```

Core
----
![demo_core1][demo_core1] ![demo_core2][demo_core2] ![demo_core3][demo_core3]

Write `SocialTextView` or `SocialEditText` in xml.
```xml
<com.hendraanggrian.widget.SocialTextView
    android:id="@+id/textView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="#hashtag and @mention."
    app:socialEnabled="hashtag|mention"
    app:hashtagColor="@color/blue"
    app:mentionColor="@color/red"/>
```

See [attrs.xml][attrs] for full list of available attributes.

Modify its state and set listeners programmatically.
```java
textView.setMentionEnabled(false);
textView.setHashtagColor(Color.RED);
textView.setOnHashtagClickListener(new Function2<SocialView, String, Unit>() {
    @Override
    public Unit invoke(SocialView socialView, String s) {
        // do something
        return null;
    }
});
```

Any TextView or subclasses of TextView can be made social, see [SocialTextView.kt][SocialTextView] for example.

Commons
-------
![demo_commons1][demo_commons1] ![demo_commons2][demo_commons2] ![demo_commons3][demo_commons3]

Write `SocialAutoCompleteTextView` in xml.
```xml
<com.hendraanggrian.widget.SocialAutoCompleteTextView
    android:id="@+id/textView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="What's on your mind?"
    app:socialEnabled="hyperlink"
    app:hyperlinkColor="@color/green"/>
```

To display suggestions, it is required to `setHashtagAdapter()` and `setMentionAdapter()`.
```java
ArrayAdapter<Hashtag> hashtagAdapter = new HashtagAdapter(getContext());
hashtagAdapter.add(new Hashtag("follow"));
hashtagAdapter.add(new Hashtag("followme", 1000));
hashtagAdapter.add(new Hashtag("followmeorillkillyou", 500));
textView.setHashtagAdapter(hashtagAdapter);

ArrayAdapter<Mention> mentionAdapter = new MentionAdapter(getContext());
mentionAdapter.add(new Mention("dirtyhobo"));
mentionAdapter.add(new Mention("hobo", "Regular Hobo", R.mipmap.ic_launcher));
mentionAdapter.add(new Mention("hendraanggrian", "Hendra Anggrian", "https://avatars0.githubusercontent.com/u/11507430?v=3&s=460"));
textView.setMentionAdapter(mentionAdapter);
```

To customize hashtag or mention adapter, create a custom adapter using customized `SocialAdapter` or write your own `ArrayAdapter`.
```java
public class Person {
    public final String name;
    
    public Person(String name) {
        this.name = name;
    }
}

// easier
public class PersonAdapter extends SocialAdapter<Person> {
    
    public PersonAdapter(@NonNull Context context) {
        super(context, R.layout.item_person, R.id.textview_person);
    }
    
    @Override
    public String convertToString(Person $receiver) {
        return $receiver.name;
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ...
    }
}

// this works too
public class PersonAdapter extends ArrayAdapter<Person> {
    // your own adapter layout, view holder, data binding
    // and of course, filtering logic
}
```

Then, use the custom adapter.
```java
ArrayAdapter<Person> adapter = new PersonAdapter(getContext());
adapter.add(personA);
adapter.add(personB);
textView.setMentionAdapter(adapter);
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
    
[demo]: /art/demo.png
[demo_core1]: /art/demo_core1.gif
[demo_core2]: /art/demo_core2.gif
[demo_core3]: /art/demo_core3.gif
[demo_commons1]: /art/demo_commons1.gif
[demo_commons2]: /art/demo_commons2.gif
[demo_commons3]: /art/demo_commons3.gif
[attrs]: https://github.com/HendraAnggrian/socialview/blob/master/socialview/res/values/attrs.xml
[SocialTextView]: https://github.com/HendraAnggrian/socialview/blob/master/socialview/src/com/hendraanggrian/socialview/widget/SocialTextView.kt