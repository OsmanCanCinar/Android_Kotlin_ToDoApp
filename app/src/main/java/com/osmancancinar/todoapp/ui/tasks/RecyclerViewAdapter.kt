package com.osmancancinar.todoapp.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.osmancancinar.todoapp.data.Task
import com.osmancancinar.todoapp.databinding.RecyclerRowTaskBinding

//We are using List Adapter because our list is dynamic not static. Our class takes onItemClickListener as a parameter and returns a ListAdapter,
// which consists of Task object and ViewHolder that takes DiffUtil.CallBack.
class RecyclerViewAdapter(private val listener: OnItemClickListener) : ListAdapter<Task, RecyclerViewAdapter.TaskViewHolder>(DiffCallback()) {

    //we used view binding so our view holder's constructor will take binding as parameter and returns a ViewHolder which takes binding.root as a parameter.
    inner class TaskViewHolder(private val binding: RecyclerRowTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        //init block will be firstly executed when the class is instantiated.
        init {
            binding.apply {
                //If we click on a item that is on the list and the id of the item is not -1 we go to the TasksFragment and there, we go to the view model to edit existing task.
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) { // No_position is a constant value which is -1.
                        val task = getItem(position)
                        listener.onItemClick(task)
                    }
                }

                //if the item position is not -1, we check for the checkbox's check
                checkboxCompleted.setOnClickListener {
                    val position = adapterPosition
                    if( position != RecyclerView.NO_POSITION) { // No_position is a constant value which is -1.
                        val task = getItem(position)
                        listener.onCheckBoxClick(task,checkboxCompleted.isChecked)
                    }
                }
            }
        }

        //custom bind function to get the information from database table and assign it to list item.
        fun bind(task: Task) {
            binding.apply {
                checkboxCompleted.isChecked = task.isCompleted
                taskName.text = task.taskName
                taskName.paint.isStrikeThruText = task.isCompleted
                priorityImg.isVisible = task.isImportant
            }
        }
    }

    //regular view binding for onCreateViewHolder Method.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = RecyclerRowTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TaskViewHolder(binding)
    }

    //we are using our custom bind function that we wrote in the holder and passing the position of the item as a parameter.
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    //Interface to handle item Click and CheckBox Click. We will write their bodies in TasksFragment by using methods from viewModel. this way we will keep the adapter and view model loosely coupled.
    interface OnItemClickListener {
        fun onItemClick(task: Task)
        fun onCheckBoxClick(task: Task, isChecked: Boolean)
    }

    //This is a must since we are using list adapter. This compares old and new items by their ids and contents.
        class DiffCallback : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Task, newItem: Task) =
                oldItem == newItem
        }
}