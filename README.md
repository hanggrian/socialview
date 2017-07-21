socialview
==========
Android TextView and EditText with hashtag, mention, and hyperlink support.
Comes in 2 packages:
 * **core** - comes with `SocialTextView`, `SocialEditText` and `SocialViewImpl` to attach any TextView.
 * **commons** - extended core library with `SocialAutoCompleteTextView` to display suggestions as you type.

![commons1][commons1]

Core
----
![core1][core1] ![core2][core2] ![core3][core3]

Write `SocialTextView` or `SocialEditText` in xml.
```xml
<com.hendraanggrian.widget.SocialTextView
    android:id="@+id/textView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="#hashtag and @mention."/>
```

Modify its state and set listeners in java.
```java
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
![commons1][commons1] ![commons2][commons2] ![commons3][commons3]

Write `SocialAutoCompleteTextView` in xml.
```xml
<com.hendraanggrian.widget.SocialAutoCompleteTextView
    android:id="@+id/textView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="What's on your mind?"/>
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
    private Filter filter = new SocialFilter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Person) resultValue).name;
        }
    };
    
    public PersonAdapter(@NonNull Context context) {
        super(context, R.layout.item_person, R.id.textview_person);
    }

    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ...
    }
    
    public Filter getFilter() {
        return filter;
    }
    
    ...
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

Download
--------
```gradle
repositories {
    jcenter()
    maven { url "https://maven.google.com" }
}

dependencies {
    // core only
    compile 'com.hendraanggrian:socialview-core:0.15.1'
    // core and commons
    compile 'com.hendraanggrian:socialview-commons:0.15.1'
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
    
[core1]: /art/core1.gif
[core2]: /art/core2.gif
[core3]: /art/core3.gif
[commons1]: /art/commons1.gif
[commons2]: /art/commons2.gif
[commons3]: /art/commons3.gif
