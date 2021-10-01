package com.osmancancinar.todoapp.ui.tasks

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.osmancancinar.todoapp.R
import com.osmancancinar.todoapp.data.SortOrder
import com.osmancancinar.todoapp.data.Task
import com.osmancancinar.todoapp.databinding.FragmentTodoListBinding
import com.osmancancinar.todoapp.util.exhaustive
import com.osmancancinar.todoapp.util.onQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_todo_list.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

//Hilt will be uses to Inject to this fragment
@AndroidEntryPoint
//this is not an automatically generated fragment so, we gave the layout in the constructor.
class TasksFragment : Fragment(R.layout.fragment_todo_list), RecyclerViewAdapter.OnItemClickListener {

    //Instance of our view model.
    private val viewModel : TasksViewModel by viewModels()
    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //view binding but since we used primary constructor to connect the layout, we don't need .inflate we only need .bind
        val binding = FragmentTodoListBinding.bind(view)

        val taskAdapter = RecyclerViewAdapter(this)
        binding.apply {
            recycler_view_todo_list.apply {
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task = taskAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onTaskSwiped(task)
                }
            }).attachToRecyclerView(recyclerViewTodoList)

            fabAdd.setOnClickListener {
                viewModel.navigateToDetailFragment()
            }

            fabInfo.setOnClickListener {
                viewModel.navigateToTrainingFragment()
            }
        }

        setFragmentResultListener("add_edit_request") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            val add= getString(R.string.added)
            val update = getString(R.string.updated)
            viewModel.onAddEditResult(result,add,update)

        }

        viewModel.tasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.tasksEvent.collect { event ->
                when(event) {
                    is TasksViewModel.TasksEvent.ShowUndoDeleteTaskMessage -> {
                        Snackbar.make(requireView(),getString(R.string.deleted),Snackbar.LENGTH_LONG).setAction(getString(R.string.undo)) {
                            viewModel.onUndoDeleteClicked(event.task)
                        }.show()
                    }
                    is TasksViewModel.TasksEvent.NavigateToAddNewTask -> {
                        val action = TasksFragmentDirections.actionTasksFragmentToDetailFragment2(null,getString(R.string.new_task))
                        findNavController().navigate(action)
                    }
                    is TasksViewModel.TasksEvent.NavigateToEditTask -> {
                        val action = TasksFragmentDirections.actionTasksFragmentToDetailFragment2(event.task,getString(R.string.edit_task))
                        findNavController().navigate(action)
                    }
                    is TasksViewModel.TasksEvent.ShowTaskSavedConfirmationMessage -> {
                        Snackbar.make(requireView(),event.msg,Snackbar.LENGTH_SHORT).show()
                    }
                    TasksViewModel.TasksEvent.OnDeleteAllClicked -> {
                        val action = TasksFragmentDirections.actionGlobalDeleteFragment22()
                        findNavController().navigate(action)
                    }
                    TasksViewModel.TasksEvent.NavigateToTraining -> {
                        val action = TasksFragmentDirections.actionGlobalTrainingFragment()
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }

        setHasOptionsMenu(true)
    }

    //In order to create our menu, we inflated the menu. Then we casted search view
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_menu,menu)

        val searchItem = menu.findItem(R.id.search)
        searchView = searchItem.actionView as SearchView

        val pendingQuery = viewModel.searchQuery.value
        if (pendingQuery != null && pendingQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery,false)
        }

        //we are calling our custom method by SearchView and passing the word that we are searching for as a string parameter.
        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }

        //It receives the status of the hide completed tasks from view model. the view model gets this from data store that we've created before.
        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.hide_completed).isChecked =
                viewModel.preferencesFlow.first().hideCompleted
        }
    }

    //This is where we handle the selected menu item's actions. Since we're applying MVVM Architecture, we let the view model handle the data.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.sort_by_name -> {
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }
            R.id.sort_by_date -> {
                viewModel.onSortOrderSelected(SortOrder.BY_DATE)
                true
            }
            R.id.hide_completed -> {
                item.isChecked = !item.isChecked
                viewModel.onHideCompletedClick(item.isChecked)
                true
            }
            R.id.delete_completed -> {
                viewModel.onDeleteAllClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //Handling the Item Click by using view model's method.
    override fun onItemClick(task: Task) {
        viewModel.onTaskSelected(task)
    }

    //We check the checkbox by using our view model and pass the task with the isChecked as parameter.
    override fun onCheckBoxClick(task: Task, isChecked: Boolean) {
        viewModel.onTaskCheckedChanged(task, isChecked)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
    }
}