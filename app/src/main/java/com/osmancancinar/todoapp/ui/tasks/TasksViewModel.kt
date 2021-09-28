package com.osmancancinar.todoapp.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.osmancancinar.todoapp.data.TaskDao

class TasksViewModel @ViewModelInject constructor( //@HiltViewModel before the class and @Inject before the constructor. For the latest version of Hilt!
    private val taskDao : TaskDao
) : ViewModel() {

    val tasks = taskDao.getTasks().asLiveData()
}