package hr.mfllipovic.github.screens.search.utils

import android.util.Log
import androidx.appcompat.widget.SearchView
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
        searchJob?.cancel()
        searchJob = lifecycle.coroutineScope.launch {
            delay(1000)
            listener.onChange(newText)
        }
        return false
    }
}

interface OnSearchQueryChange {
    fun onChange(query: String?)
}