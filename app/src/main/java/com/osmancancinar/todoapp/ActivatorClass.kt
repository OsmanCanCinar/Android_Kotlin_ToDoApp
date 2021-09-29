package com.osmancancinar.todoapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ActivatorClass : Application() {
    //this activates Hilt
}