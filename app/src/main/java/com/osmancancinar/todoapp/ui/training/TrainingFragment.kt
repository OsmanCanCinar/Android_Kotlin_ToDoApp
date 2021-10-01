package com.osmancancinar.todoapp.ui.training

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.osmancancinar.todoapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrainingFragment : DialogFragment(){

    private val viewModel: TrainingViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.features))
            .setMessage(getString(R.string.instructions))
            .setNegativeButton("Dismiss",null)
            .create()

}