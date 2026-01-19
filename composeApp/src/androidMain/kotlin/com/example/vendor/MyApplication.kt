package com.example.vendor

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.vendor.di.initKoin
import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin (
            config = {
                androidContext(this@MyApplication)
            }
        )
        Firebase.initialize(this)
    }
}