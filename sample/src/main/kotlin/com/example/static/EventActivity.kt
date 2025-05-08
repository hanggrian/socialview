package com.example.static

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import com.example.R

class EventActivity : StaticActivity() {
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_event, menu)
        return true
    }
}
