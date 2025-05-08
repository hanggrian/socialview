package com.example.static

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat

open class StaticActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat
            .getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = true
    }
}
