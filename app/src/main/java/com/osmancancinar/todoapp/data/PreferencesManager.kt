package com.osmancancinar.todoapp.data

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

//Instead of using shared preferences, we are using data store of the jetPack libraries.

//We create an enum class to specify and access the types of sorts that we can use.
enum class SortOrder { BY_NAME, BY_DATE }

//In order to hold the "sort order" and "hide completed tasks" preferences, we created another data class.
data class FilterPreferences(val sortOrder: SortOrder, val hideCompleted: Boolean)

//We use Singleton because the scope must be Application Level.
@Singleton
//We use Hilt to inject data. We made sure that these preferences could be accessed all over the project files.
class PreferencesManager @Inject constructor(@ApplicationContext context: Context){ // Constructor Injection

    //we declare and initialize our data store.
    private val dataStore = context.createDataStore("user_preferences")

    //If there is an I/O Exception we try to catch it and if the exception is smt else we throw a general exception to prevent the app from crushing.
    val preferencesFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        //this is the place that we store the data to data store.
        .map {
            //we get the latest status of the "sort order".
            val sortOrder = SortOrder.valueOf(
                it[PreferenceKeys.SORT_ORDER] ?: SortOrder.BY_DATE.name
            )

            //we get the latest status of the "hide completed".
            val hideCompleted = it[PreferenceKeys.HIDE_COMPLETED] ?: false

            //we store these status in the instance of a custom object that we created.
            FilterPreferences(sortOrder,hideCompleted)
        }

    //It updates the status of the "Sort Order" and we declared it as suspend function so it does not block other threads. suspend fun belongs to coroutines.
    suspend fun updateSortOrder(sortOrder: SortOrder) {
        dataStore.edit {
            it[PreferenceKeys.SORT_ORDER] = sortOrder.name
        }
    }

    //It updates the status of the "hide the completed tasks" and we declared it as suspend function so it does not block other threads. suspend fun belongs to coroutines.
    suspend fun updateHideCompleted(hideCompleted: Boolean) {
        dataStore.edit {
            it[PreferenceKeys.HIDE_COMPLETED] = hideCompleted
        }
    }

    //Creating a special object to store the status of "Sorting order" and the "Hide the completed tasks".
    private object PreferenceKeys {
        val SORT_ORDER = preferencesKey<String>("sort_order")
        val HIDE_COMPLETED = preferencesKey<Boolean>("hide_completed")
    }
}