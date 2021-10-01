package com.osmancancinar.todoapp.ui.deletion

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.osmancancinar.todoapp.R
import dagger.hilt.android.AndroidEntryPoint

//Injection will be used.
@AndroidEntryPoint
//this is a fragment without a layout. we use it as a DialogFragment
class DeleteFragment  : DialogFragment() {

    //Instance of viewModel
    private val viewModel: DeleteViewModel by viewModels()

    //we create the dialog and let the view model handle the functionality. we just direct it.
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.confirm))
            .setMessage(getString(R.string.are_you_sure))
            .setNegativeButton(getString(R.string.cancel), null)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.onConfirmClick()
            }
            .create()
}