package com.skierski.github.applause

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupActionBarWithNavController(findNavController(R.id.nav_host))
    }

    override fun onOptionsItemSelected(item: MenuItem?) =
        if (item?.itemId == android.R.id.home) {
            findNavController(R.id.nav_host).navigateUp()
        } else {
            super.onOptionsItemSelected(item)
        }
}