package com.osmancancinar.todoapp.util

import androidx.appcompat.widget.SearchView

//crossLine marker is used to mark lambdas that must not allow non-local returns.
//we add a new method to SearchView, so that we can use it like its build-in method.
inline fun SearchView.onQueryTextChanged(crossinline listener : (String) -> Unit) {

    //we are overriding onQueryTextSubmit and onQueryTextChange
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

        //Reason we disable this function is, we don't need a submit button to do the search.
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        //we check if the new entered text is empty or not.
        override fun onQueryTextChange(newText: String?): Boolean {
            listener(newText.orEmpty())
            return true
        }
    })
}

//turns statement into an expression
val <T> T.exhaustive: T
    get() = this