package com.osmancancinar.todoapp.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.text.DateFormat

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