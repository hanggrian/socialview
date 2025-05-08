[![CircleCI](https://img.shields.io/circleci/build/gh/hanggrian/socialview)](https://app.circleci.com/pipelines/github/hanggrian/socialview/)
[![Codecov](https://img.shields.io/codecov/c/gh/hanggrian/socialview)](https://app.codecov.io/gh/hanggrian/socialview/)
[![Maven Central](https://img.shields.io/maven-central/v/com.hanggrian.socialview/socialview)](https://central.sonatype.com/artifact/com.hanggrian.socialview/socialview/)
[![Android SDK](https://img.shields.io/badge/android-21%2B-34a853)](https://developer.android.com/tools/releases/platforms/#5.0) \
[![Figma](https://img.shields.io/badge/design-figma-f24e1e)](https://www.figma.com/community/file/1502318475869385725/)
[![Layers](https://img.shields.io/badge/showcase-layers-000)](https://layers.to/layers/cmahkfidf0019lb0d8aok3ahl/)
[![Pinterest](https://img.shields.io/badge/pin-pinterest-bd081c)](https://www.pinterest.com/pin/1107322627133689218/)

# SocialView

![](https://github.com/hanggrian/socialview/raw/assets/preview_hashtag.png "Hashtag preview")
![](https://github.com/hanggrian/socialview/raw/assets/preview_mention.png "Mention preview")

TextView and EditText with hashtag, mention, and hyperlink support.

- Pre-loaded with default views, but also installable to any custom view.
- Display hashtag and mention suggestions as you type.

## Download

```gradle
repositories {
    mavenCentral()
    google()
}
dependencies {
    // base widgets
    implementation "com.hanggrian.socialview:socialview:$version"

    // auto-complete widgets
    implementation "com.hanggrian.socialview:socialview-autocomplete:$version"
}
```

## Usage

Base library contains `SocialTextView`, `SocialEditText` and helper class
to apply such behavior in any `TextView`.

```xml
<com.hanggrian.socialview.SocialTextView
  android:id="@+id/textView"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"
  android:text="#hashtag and @mention."
  app:socialFlags="hashtag|mention"
  app:hashtagColor="@color/blue"
  app:mentionColor="@color/red"/>
```

See [attrs.xml](https://github.com/hanggrian/socialview/blob/master/socialview/res/values/attrs.xml)
for full list of available attributes.

Modify its state and set listeners programmatically.

```java
textView.setMentionEnabled(false);
textView.setHashtagColor(Color.RED);
textView.setOnHashtagClickListener((view, s) -> {
    // ...
});
```

### Auto-complete

Extended library comes with `SocialAutoCompleteTextView`.

```xml
<com.hanggrian.socialview.autocomplete.SocialAutoCompleteTextView
  android:id="@+id/textView"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"
  android:hint="What's on your mind?"
  app:socialFlags="hyperlink"
  app:hyperlinkColor="@color/red"/>
```

To display suggestions, it is required to `setHashtagAdapter()`
and `setMentionAdapter()`.

```java
ArrayAdapter<Hashtag> hashtagAdapter = new HashtagAdapter(getContext());
hashtagAdapter.add(new Hashtag("isell"));
hashtagAdapter.add(new Hashtag("isellpropane", 500));
textView.setHashtagAdapter(hashtagAdapter);

ArrayAdapter<Mention> mentionAdapter = new MentionAdapter(getContext());
mentionAdapter.add(new Mention("hank"));
mentionAdapter.add(new Mention("hankhill", "Hank Hill", R.drawable.ic_person));
textView.setMentionAdapter(mentionAdapter);
```

To customize hashtag or mention adapter, create a custom adapter using
customized `SocialAdapter` or write your own `ArrayAdapter`.

> Custom adapters are experimental, see sample for example.

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
        // ...
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
