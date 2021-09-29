package com.osmancancinar.todoapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

//data access object and its an interface that can be implemented to use when we need it.
@Dao
interface TaskDao {

    //this function decides calling the right sorting function according to sort type and it returns a flow of list that consists of Task object.(db table)
    //flow lets suspend functions to return multiple values.(coroutine-room)
    fun getTasks(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Task>> =
        when(sortOrder) {
            SortOrder.BY_DATE -> getTasksSortedByDate(query,hideCompleted)
            SortOrder.BY_NAME -> getTasksSortedByName(query,hideCompleted)
        }

    //This is a standard room query to insert data. We specify it with @Insert Annotation. onConflictStrategy resolves the problems related to insertion.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    //we use coroutines to make this task not to block other thread bc it's a database operation. Should not work on UI thread.
    suspend fun insert(task: Task)

    //standard build-in update query. same as above.
    @Update
    suspend fun update(task: Task)

    //standard build-in delete query. same as above.
    @Delete
    suspend fun delete(task: Task)

    /*
        Thanks to Room it warns us, if there are any errors in the sql query.
        First, we check if we need to hide the completed tasks by if it is false(0) or hideCompleted parameter.
        Second, we look for the not the exact but included places of out given string from the start and the end.
        Third, we let the tasks that are marked as important to be listed first.
    */

    //as the last step, we sort the result according to task's name.
    @Query("SELECT * FROM tasks WHERE (isCompleted != :hideCompleted OR isCompleted = 0 ) AND taskName LIKE '%' || :searchQuery || '%' ORDER BY isImportant DESC, taskName")
    fun getTasksSortedByName(searchQuery : String, hideCompleted : Boolean): Flow<List<Task>>

    //as the last step, we sort the result according to task's creation date and time.
    @Query("SELECT * FROM tasks WHERE (isCompleted != :hideCompleted OR isCompleted = 0 ) AND taskName LIKE '%' || :searchQuery || '%' ORDER BY isImportant DESC, creationTime")
    fun getTasksSortedByDate(searchQuery : String, hideCompleted : Boolean): Flow<List<Task>>
}