package com.osmancancinar.todoapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

//this activates Hilt and we added this class to the Android Manifest file.
@HiltAndroidApp
//Class has to extend Application()
class ActivatorClass : Application() {

}