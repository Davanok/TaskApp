package com.davanok.taskapp

import android.app.Application

lateinit var appContext: Application
    private set

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}