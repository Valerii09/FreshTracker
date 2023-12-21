package com.example.freshtracker

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application(){

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private lateinit var instance: MyApp

        fun getContext(): Context {
            return instance.applicationContext
        }
    }
}
