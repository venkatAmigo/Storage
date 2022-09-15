package com.example.storage

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Prefs = getSharedPreferences("${this.packageName}", MODE_PRIVATE)
    }
}