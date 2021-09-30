package com.osmancancinar.todoapp.ui.detail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.osmancancinar.todoapp.data.Task
import com.osmancancinar.todoapp.data.TaskDao
import com.osmancancinar.todoapp.ui.ADD_TASK_RESULT_OK
import com.osmancancinar.todoapp.ui.EDIT_TASK_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

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

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()


    fun onSaveClick() {
        if (taskName.isBlank()) {
            showInvalidInputMessage("Name cannot be empty")
            return
        }

        /*
        if (taskDescription.isBlank()) {
            showInvalidInputMessage("Description cannot be empty")
            return
        }
         */

        if (task != null) {
            val updatedTask = task.copy(
                taskName = taskName,
                taskDescription = taskDescription,
                isImportant = taskImportance
            )
            updateTask(updatedTask)
        } else {
            val newTask = Task(taskName = taskName,
                taskDescription = taskDescription,
                isImportant = taskImportance
            )
            createTask(newTask)
        }
    }

    private fun showInvalidInputMessage(msg: String) = viewModelScope.launch{
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(msg))
    }

    private fun createTask(task: Task) = viewModelScope.launch{
        taskDao.insert(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
    }

    private fun updateTask(task: Task) = viewModelScope.launch{
        taskDao.update(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_TASK_RESULT_OK))
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditTaskEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
    }
}