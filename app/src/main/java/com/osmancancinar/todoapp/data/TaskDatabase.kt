package com.osmancancinar.todoapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.osmancancinar.todoapp.hilt.AppModule.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

//This is where we create our database, and specify the data it will hold with the version of it.
@Database(entities = [Task::class], version = 1)
//It must be abstract class and implement the Room Database
abstract class TaskDatabase : RoomDatabase() {

    //we create an abstract instance of our database access object.
    abstract fun taskDao(): TaskDao

    //This callback class will be injected where it is a dependency.
    class Callback @Inject constructor( // Constructor Injection
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope //ApplicationScope Annotation is our custom annotation, and it specifies which scope to create.
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().taskDao()

            //delete later
            applicationScope.launch {
                dao.insert(Task("Repair the sink","decs random things",isCompleted = true))
                dao.insert(Task("Call Buse", "decs random things",isImportant = true))
                dao.insert(Task("Study Mvvm Concept","decs random things"))
                dao.insert(Task("Develop your application", "decs random things",isImportant = true))
                dao.insert(Task("Go to grocery shopping","decs random things"))
                dao.insert(Task("Meet with your friends","decs random things",isCompleted = true))
            }
        }
    }
}