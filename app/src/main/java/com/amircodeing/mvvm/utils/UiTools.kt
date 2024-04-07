package com.amircodeing.mvvm.utils


import androidx.appcompat.widget.SearchView
import com.google.android.material.animation.AnimatableView.Listener

/**
 * @inline function => When we use this inline function,
 * no object is created from the function,
 * but data is passed to this function directly.
 *
 */
//Implement Search 1 Step
inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean = true

        override fun onQueryTextChange(newText: String?): Boolean {
            listener.invoke(newText.orEmpty())
            return true
        }
    })
}