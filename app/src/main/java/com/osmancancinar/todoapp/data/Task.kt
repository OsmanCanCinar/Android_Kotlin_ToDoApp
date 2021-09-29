package com.osmancancinar.todoapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

/*
    Task data class is an Entity for our Room database. It's the representation of the table and it's columns. We use Parcelable because we are not streaming
    data by using network, we are only using local storage. We specify the primary key to be auto generated. At last, we dynamically create our time stamp.
 */

@Entity(tableName = "tasks")
@Parcelize
data class Task(
    val taskName: String,
    val isImportant: Boolean = false,
    val isCompleted: Boolean = false,
    val creationTime: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {
    val creationDate: String
        get() = DateFormat.getDateTimeInstance().format(creationTime)
}