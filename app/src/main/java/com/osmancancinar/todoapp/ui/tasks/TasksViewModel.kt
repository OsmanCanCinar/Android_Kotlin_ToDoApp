package com.osmancancinar.todoapp.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.osmancancinar.todoapp.data.PreferencesManager
import com.osmancancinar.todoapp.data.SortOrder
import com.osmancancinar.todoapp.data.Task
import com.osmancancinar.todoapp.data.TaskDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

//ViewModelInject means This class has dependencies on TaskDao and Preferences Objects. While Creating TaskViewModel, these initializations will ve handled(injected) by hilt.
class TasksViewModel @ViewModelInject constructor( //@HiltViewModel before the class and @Inject before the constructor. For the latest version of Hilt!
    private val taskDao : TaskDao,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    //MutableStateFlow is like live data this is how we will hold our search query and we assign an initial value.
    val searchQuery = MutableStateFlow("")
    //this is where we retrieve and save our sort preferences to data store.
    val preferencesFlow = preferencesManager.preferencesFlow

    //channels allow coroutines to communicate with each other.
    private val taskEventChannel = Channel<TasksEvent>()
    val tasksEvent = taskEventChannel.receiveAsFlow()

    //we can use combine while receiving multiple types of data from a flow.
    private val tasksFlow = combine(
        searchQuery,
        preferencesFlow
    ) {
        query, filterPreferences ->
        //to use multiple vector values, we use Pair method
        Pair(query,filterPreferences)
    }.flatMapLatest { (query,filterPreferences) ->
        taskDao.getTasks(query, filterPreferences.sortOrder, filterPreferences.hideCompleted)
    }

    //taskFragment observes tasks by using the code below.
    val tasks = tasksFlow.asLiveData()

    //when the sort order change, we update status of it in the data store.
    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    //when the hide completed task change, we update status of it in the data store.
    fun onHideCompletedClick(hideCompleted: Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    //this is how we edit an existing task when its selected. by navigation to edit fragment.
    fun onTaskSelected(task : Task) {

    }

    //if a task is checked/unchecked.
    fun onTaskCheckedChanged(task : Task, isChecked: Boolean) = viewModelScope.launch {
        //copy() is allowing us to alter some of its properties while keeping the rest unchanged.
        taskDao.update(task.copy(isCompleted = isChecked))
    }

    //swipe to delete functionality.
    fun onTaskSwiped(task: Task) = viewModelScope.launch {
        taskDao.delete(task)
        taskEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
    }

    //cannot be reproduced as a class.
    sealed class TasksEvent {
        data class ShowUndoDeleteTaskMessage(val task: Task) : TasksEvent()
    }

    //after deleting a task we save it to TaskEvent and re-insert it.
    fun onUndoDeleteClicked(task: Task) = viewModelScope.launch{
        taskDao.insert(task)
    }
}

