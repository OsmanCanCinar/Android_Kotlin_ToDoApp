package com.osmancancinar.todoapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/*
    This is where we write our data access object, they execute the standard and custom queries.
 */

@Dao
interface TaskDao {

    fun getTasks(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Task>> =
        when(sortOrder) {
            SortOrder.BY_DATE -> getTasksSortedByDate(query,hideCompleted)
            SortOrder.BY_NAME -> getTasksSortedByName(query,hideCompleted)
        }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM tasks WHERE (isCompleted != :hideCompleted OR isCompleted = 0 ) AND taskName LIKE '%' || :searchQuery || '%' ORDER BY isImportant DESC, taskName")
    fun getTasksSortedByName(searchQuery : String, hideCompleted : Boolean): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE (isCompleted != :hideCompleted OR isCompleted = 0 ) AND taskName LIKE '%' || :searchQuery || '%' ORDER BY isImportant DESC, creationTime")
    fun getTasksSortedByDate(searchQuery : String, hideCompleted : Boolean): Flow<List<Task>>
}