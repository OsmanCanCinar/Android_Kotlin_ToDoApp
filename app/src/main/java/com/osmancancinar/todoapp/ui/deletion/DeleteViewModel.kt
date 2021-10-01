package com.osmancancinar.todoapp.ui.deletion

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.osmancancinar.todoapp.data.TaskDao
import com.osmancancinar.todoapp.hilt.AppModule.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//This model will be used as the part of Injection.
class DeleteViewModel @ViewModelInject constructor(

    //Instance of our database access object
    private val taskDao: TaskDao,
    //we make sure that its accessible from anywhere in the app by using our custom annotation tag.
    @ApplicationScope private val applicationScope: CoroutineScope
): ViewModel(){

    //we use coroutines to delete the completed tasks by calling the necessary method of the dao.
    fun onConfirmClick() = applicationScope.launch {
        taskDao.deleteCompletedTasks()
    }
}