[![CircleCI](https://img.shields.io/circleci/build/gh/hanggrian/socialview)](https://app.circleci.com/pipelines/github/hanggrian/socialview/)
[![Codecov](https://img.shields.io/codecov/c/gh/hanggrian/socialview)](https://app.codecov.io/gh/hanggrian/socialview/)
[![Maven Central](https://img.shields.io/maven-central/v/com.hendraanggrian.appcompat/socialview)](https://search.maven.org/artifact/com.hendraanggrian.appcompat/socialview/)
[![Nexus Snapshot](https://img.shields.io/nexus/s/com.hendraanggrian.appcompat/socialview?server=https%3A%2F%2Fs01.oss.sonatype.org)](https://s01.oss.sonatype.org/content/repositories/snapshots/com/hendraanggrian/appcompat/socialview/)
[![Android SDK](https://img.shields.io/badge/android-21%2B-34a853)](https://developer.android.com/tools/releases/platforms#5.0)

# SocialView

![Hashtag preview.](https://github.com/hendraanggrian/socialview/raw/assets/preview_hashtag.png)
![Mention preview.](https://github.com/hendraanggrian/socialview/raw/assets/preview_mention.png)

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
    implementation "com.hendraanggrian.appcompat:socialview:$version"

    // auto-complete widgets
    implementation "com.hendraanggrian.appcompat:socialview-autocomplete:$version"
}
```

## Usage

### Core

Core library contains `SocialTextView`, `SocialEditText` and helper class
applies these behavior into any `TextView`.

```xml
<com.hendraanggrian.appcompat.socialview.widget.SocialTextView
  android:id="@+id/textView"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"
  android:text="#hashtag and @mention."
  app:socialFlags="hashtag|mention"
  app:hashtagColor="@color/blue"
  app:mentionColor="@color/red"/>
```

See [attrs.xml](https://github.com/HendraAnggrian/socialview/blob/master/socialview/res/values/attrs.xml)
for full list of available attributes.

Modify its state and set listeners programmatically.

```java
textView.setMentionEnabled(false);
textView.setHashtagColor(Color.RED);
textView.setOnHashtagClickListener(
    new SocialView.OnClickListener() {
        @Override
        public void invoke(SocialView socialView, String s) {
          // do something
        }
    }
);
```

### Auto-complete

Extended library comes with `SocialAutoCompleteTextView`.

```xml
<com.hendraanggrian.appcompat.socialview.widget.SocialAutoCompleteTextView
  android:id="@+id/textView"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"
  android:hint="What's on your mind?"
  app:socialFlags="hyperlink"
  app:hyperlinkColor="@color/green"/>
```

To display suggestions, it is required to `setHashtagAdapter()`
and `setMentionAdapter()`.

```java
ArrayAdapter<Hashtag> hashtagAdapter = new HashtagAdapter(getContext());
hashtagAdapter.add(new Hashtag("yolo"));
hashtagAdapter.add(new Hashtag("swag", 500));
textView.setHashtagAdapter(hashtagAdapter);

ArrayAdapter<Mention> mentionAdapter = new MentionAdapter(getContext());
mentionAdapter.add(new Mention("man"));
mentionAdapter.add(new Mention("woman", "Alice", R.drawable.ic_person));
mentionAdapter.add(
    new Mention(
        "johndoe",
        "John Doe",
        "url://to.profile"
    )
);
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
