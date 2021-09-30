package com.osmancancinar.todoapp.ui.detail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.osmancancinar.todoapp.R
import com.osmancancinar.todoapp.databinding.FragmentTaskDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_task_detail){

    private val viewModel: DetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTaskDetailBinding.bind(view)

        binding.apply {
            editTextTaskName.setText(viewModel.taskName)
            editTextTaskDetail.setText(viewModel.taskDescription)
            checkboxImportant.isChecked = viewModel.taskImportance
            checkboxImportant.jumpDrawablesToCurrentState()
            textViewDateCreated.isVisible = viewModel.task != null
            textViewDateCreated.text = """${getString(R.string.created)}${viewModel.task?.creationDate}"""
        }
    }
}