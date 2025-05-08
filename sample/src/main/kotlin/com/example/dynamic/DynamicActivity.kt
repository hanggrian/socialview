package com.example.dynamic

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.WindowCompat
import com.example.R
import com.google.android.material.snackbar.Snackbar
import com.hanggrian.socialview.autocomplete.Hashtag
import com.hanggrian.socialview.autocomplete.HashtagArrayAdapter
import com.hanggrian.socialview.autocomplete.Mention
import com.hanggrian.socialview.autocomplete.MentionArrayAdapter
import com.hanggrian.socialview.autocomplete.SocialAutoCompleteTextView
import com.squareup.picasso.Picasso

class DynamicActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var card: CardView
    private lateinit var image: ImageView
    private lateinit var textView: SocialAutoCompleteTextView
    private lateinit var button: Button

    private lateinit var defaultHashtagAdapter: ArrayAdapter<Hashtag>
    private lateinit var defaultMentionAdapter: ArrayAdapter<Mention>
    private lateinit var customHashtagAdapter: ArrayAdapter<Person>
    private lateinit var customMentionAdapter: ArrayAdapter<Person>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic)
        toolbar = findViewById(R.id.toolbar)
        card = findViewById(R.id.card)
        image = findViewById(R.id.image)
        button = findViewById(R.id.button)
        textView = findViewById(R.id.textView)
        setSupportActionBar(toolbar)

        defaultHashtagAdapter = HashtagArrayAdapter(this)
        defaultHashtagAdapter.addAll(Hashtags)

        defaultMentionAdapter = MentionArrayAdapter(this)
        defaultMentionAdapter.addAll(Mentions)

        customHashtagAdapter = PersonAdapter(this)
        customHashtagAdapter.addAll(Hashtags.map { Person(it.id) })

        customMentionAdapter = PersonAdapter(this)
        customMentionAdapter.addAll(Mentions.map { Person(it.username) })

        textView.hashtagAdapter = defaultHashtagAdapter
        textView.mentionAdapter = defaultMentionAdapter
        textView.setHashtagTextChangedListener { _, text -> Log.d("hashtag", text.toString()) }
        textView.setMentionTextChangedListener { _, text -> Log.d("mention", text.toString()) }
        textView.setOnHyperlinkClickListener { _, text -> Log.d("hyperlink", text.toString()) }

        Picasso
            .get()
            .load(
                "https://static.wikia.nocookie.net/mrmsco/images/d/d9/Hank_Hill.jpg/" +
                    "revision/latest/",
            ).transform(CropCircleTransformation())
            .into(image)

        button.setOnClickListener {
            Snackbar
                .make(card, textView.text, Snackbar.LENGTH_LONG)
                .setAction(android.R.string.ok) { textView.setText("") }
                .show()
        }

        WindowCompat
            .getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK !=
            Configuration.UI_MODE_NIGHT_YES
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_dynamic, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_customAdapter -> {
                when {
                    !item.isChecked -> {
                        textView.hashtagAdapter = customHashtagAdapter
                        textView.mentionAdapter = customMentionAdapter
                    }
                    else -> {
                        textView.hashtagAdapter = defaultHashtagAdapter
                        textView.mentionAdapter = defaultMentionAdapter
                    }
                }
            }
            R.id.item_enableHashtag -> textView.isHashtagEnabled = !item.isChecked
            R.id.item_enableMention -> textView.isMentionEnabled = !item.isChecked
            R.id.item_enableHyperlink -> textView.isHyperlinkEnabled = !item.isChecked
            else -> return super.onOptionsItemSelected(item)
        }
        item.isChecked = !item.isChecked
        return true
    }
}
