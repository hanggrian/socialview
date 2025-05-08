package com.example.static

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.R

class ShopActivity : StaticActivity() {
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        ShopFragment().show(supportFragmentManager, ShopFragment.TAG)
    }
}
