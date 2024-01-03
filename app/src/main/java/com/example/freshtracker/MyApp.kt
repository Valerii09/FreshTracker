package com.example.freshtracker

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

class MyApp : Application() {

    companion object {
        private lateinit var instance: MyApp

        fun getContext(): Context {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
