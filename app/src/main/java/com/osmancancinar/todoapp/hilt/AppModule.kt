package com.osmancancinar.todoapp.hilt

import android.app.Application
import androidx.room.Room
import com.osmancancinar.todoapp.data.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class) //SingletonComponent instead of Application Component for the newer versions of the hilt!
object AppModule {

    //we create our room database on the application level.
    @Provides
    @Singleton
    fun provideDatabase( app : Application, callback: TaskDatabase.Callback) = Room.databaseBuilder(app, TaskDatabase::class.java, "task_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()

    /*
    //They are the same.
    @Provides
    @Singleton
    fun provideDatabase( app : Application, callback: TaskDatabase.Callback) {
        Room.databaseBuilder(app, TaskDatabase::class.java, "task_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }
     */

    //returns the database access object of the given database.
    @Provides
    fun provideTaskDao(db: TaskDatabase) = db.taskDao()

    //we use our annotation to specify where to create dependency from.
    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

    //we create our own annotation here.
    @Retention(AnnotationRetention.RUNTIME)
    @Qualifier
    annotation class ApplicationScope
}