package com.osmancancinar.todoapp.ui.tasks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.osmancancinar.todoapp.R
import com.osmancancinar.todoapp.databinding.FragmentTodoListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_todo_list.*

@AndroidEntryPoint
class TasksFragment : Fragment(R.layout.fragment_todo_list) {

    private val viewModel : TasksViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTodoListBinding.bind(view)
        val taskAdapter = RecyclerViewAdapter()
        binding.apply {
            recycler_view_todo_list.apply {
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }
        viewModel.tasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }
    }
}