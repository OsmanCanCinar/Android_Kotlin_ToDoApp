package com.osmancancinar.todoapp.ui.detail

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.osmancancinar.todoapp.R
import com.osmancancinar.todoapp.databinding.FragmentTaskDetailBinding
import com.osmancancinar.todoapp.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

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
            textViewDateCreated.text = getString(R.string.created) + " " +viewModel.task?.creationDate

            editTextTaskName.addTextChangedListener {
                viewModel.taskName = it.toString()
            }

            editTextTaskDetail.addTextChangedListener {
                viewModel.taskDescription = it.toString()
            }

            checkboxImportant.setOnCheckedChangeListener { _, isChecked ->
                viewModel.taskImportance = isChecked
            }

            fabSave.setOnClickListener {
                viewModel.onSaveClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEvent.collect {
                when (it) {
                    is DetailViewModel.AddEditTaskEvent.NavigateBackWithResult -> {
                        binding.editTextTaskName.clearFocus()
                        binding.editTextTaskDetail.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to it.result)
                        )
                        findNavController().popBackStack()
                    }
                    is DetailViewModel.AddEditTaskEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), it.msg, Snackbar.LENGTH_LONG).show()
                    }
                }.exhaustive
            }
        }
    }
}