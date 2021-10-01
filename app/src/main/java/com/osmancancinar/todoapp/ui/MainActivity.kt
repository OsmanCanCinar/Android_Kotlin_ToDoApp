package com.osmancancinar.todoapp.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.osmancancinar.todoapp.R
import dagger.hilt.android.AndroidEntryPoint

//It means Hilt will be used to inject to this Activity.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_ToDoApp)
        setContentView(R.layout.activity_main)

        //up navigation Implementation for navigation graph
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()

        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

//Our Constant Values that we can use all around the App
const val ADD_TASK_RESULT_OK = Activity.RESULT_FIRST_USER // value 1
const val EDIT_TASK_RESULT_OK = Activity.RESULT_FIRST_USER + 1 // 2