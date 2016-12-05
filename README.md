Android Extra
=============

![logo](/art.png)

Bind Android Intent extras using annotation:

 * Eliminate constant extra message declarations (e.g.: `public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE"`) with `@BindExtra`.
 * Eliminate `getIntent()`, `getExtras()`, and `getStringExtra()` calls with `Extra.collect()`.
 * Convert `JSONObject` to `Bundle` and vice-versa with `Extras`.

```java
public class MainActivity {

    @BindExtra("EXTRA_MESSAGE_A") String messageA;
    @BindExtra("EXTRA_MESSAGE_B") String messageB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Extra.collect(this);
    }
}
```

Download
--------

```gradle
compile 'io.github.hendraanggrian:extra:0.4.0'
```

Usage
-----

Annotate intent extras with string key or using field name as the key.

```java
public class MainActivity extends Activity {

    @BindExtra("EXTRA_STRING") String extraString;
    @BindExtra("EXTRA_INT") Integer extraInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Extra.collect(this);
    }
}
```

Send these extras with `Bundle`, use default `Bundle` or build it using `Extras`:

```java
Bundle bundle = new Bundle();
bundle.putString("EXTRA_STRING", "Hello world!");
bundle.putInt("EXTRA_INT", 1234);
startActivity(intent.putExtras(bundle));

// or

startActivity(intent.putExtras(new Extras()
    .putString("EXTRA_STRING", "Hello world!")
    .putInt("EXTRA_INT", 1234)
    .toBundle()));
```

To-Do
-----

 1. Bind extras from other apps.
