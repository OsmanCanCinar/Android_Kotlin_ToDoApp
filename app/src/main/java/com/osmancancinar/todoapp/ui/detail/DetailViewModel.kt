package com.osmancancinar.todoapp.ui.detail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.osmancancinar.todoapp.data.Task
import com.osmancancinar.todoapp.data.TaskDao

class DetailViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    val task = state.get<Task>("task")

    var taskName = state.get<String>("taskName") ?: task?.taskName ?: ""
        set(value) {
            field = value
            state.set("taskName",value)
        }

    var taskDescription = state.get<String>("taskDescription") ?: task?.taskDescription ?: ""
        set(value) {
            field = value
            state.set("taskDescription",value)
        }

    var taskImportance = state.get<Boolean>("taskImportance") ?: task?.isImportant ?: false
        set(value) {
            field = value
            state.set("taskImportance",value)
        }
}