package com.example.static

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import com.deepan.pieprogress.PieProgress
import com.example.R

class HomeActivity : StaticActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var spinner: Spinner
    private lateinit var pieProgress: PieProgress

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        toolbar = findViewById(R.id.toolbar)
        spinner = findViewById(R.id.spinner)
        pieProgress = findViewById(R.id.pieProgress)
        setSupportActionBar(toolbar)

        spinner.adapter =
            ArrayAdapter
                .createFromResource(
                    this,
                    R.array.options,
                    android.R.layout.simple_spinner_item,
                ).also {
                    it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }

        pieProgress.setProgress(85f)
    }
}
