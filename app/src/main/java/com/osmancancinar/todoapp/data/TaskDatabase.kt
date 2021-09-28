package com.osmancancinar.todoapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.osmancancinar.todoapp.hilt.AppModule.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().taskDao()

            //delete later
            applicationScope.launch {
                dao.insert(Task("Repair the sink",isCompleted = true))
                dao.insert(Task("Call Buse", isImportant = true))
                dao.insert(Task("Study Mvvm Concept"))
                dao.insert(Task("Develop your application", isImportant = true))
                dao.insert(Task("Go to grocery shopping"))
                dao.insert(Task("Meet with your friends",isCompleted = true))
            }
        }
    }
}