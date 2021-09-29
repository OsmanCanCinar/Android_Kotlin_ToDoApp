package com.osmancancinar.todoapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.osmancancinar.todoapp.R
import dagger.hilt.android.AndroidEntryPoint

//It means Hilt will be used to inject to this Activity.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}