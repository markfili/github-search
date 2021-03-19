package hr.mfllipovic.github.screens.search.utils

import android.util.Log
import android.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchQueryChangeListener(
    private val lifecycle: Lifecycle,
    private val listener: OnSearchQueryChange
) :
    SearchView.OnQueryTextListener {
    private var searchJob: Job? = null
    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.i("SearchFragment", "Query submitted: $query")
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.i("SearchFragment", "Query changed: $newText")
        searchJob?.cancel()
        searchJob = lifecycle.coroutineScope.launch {
            delay(400)
            listener.onChange(newText)
        }
        return false
    }
}

interface OnSearchQueryChange {
    fun onChange(query: String?)
}