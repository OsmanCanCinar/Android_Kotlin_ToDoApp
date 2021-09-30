package com.osmancancinar.todoapp.ui.deletion

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.osmancancinar.todoapp.data.TaskDao
import com.osmancancinar.todoapp.hilt.AppModule.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DeleteViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    @ApplicationScope private val applicationScope: CoroutineScope
): ViewModel(){

    fun onConfirmClick() = applicationScope.launch {
        taskDao.deleteCompletedTasks()
    }
}