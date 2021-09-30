package com.osmancancinar.todoapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

//We specify the name of the table in the Room(SQLite database).
@Entity(tableName = "tasks")
//Parcelize lets us to wrap the class's data and pass it to another Activity/Class.
@Parcelize
//It's meta data so we use it as "data" class.
data class Task(
    //the attributes of the class and their default values.(Columns of the table)
    val taskName: String,
    val taskDescription: String,
    val isImportant: Boolean = false,
    val isCompleted: Boolean = false,
    val creationTime: Long = System.currentTimeMillis(),

    //We specify that our primary key will be id and we want it to start from 0 and increase by default.
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {
    //this is where we dynamically convert creation time to creation date as a time stamp.
    val creationDate: String
        get() = DateFormat.getDateTimeInstance().format(creationTime)
}