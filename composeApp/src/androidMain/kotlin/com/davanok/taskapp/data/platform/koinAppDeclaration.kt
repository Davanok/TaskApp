package com.davanok.taskapp.data.platform

import com.davanok.taskapp.appContext
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication

actual fun KoinApplication.appDeclaration() {
    androidContext(appContext.applicationContext)
}