package com.example.static

import android.os.Bundle
import com.example.R

class MapActivity : StaticActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
    }
}
