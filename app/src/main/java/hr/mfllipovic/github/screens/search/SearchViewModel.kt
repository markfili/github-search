package hr.mfllipovic.github.screens.search

import android.util.Log
import androidx.databinding.Observable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import hr.mfllipovic.github.BR
import hr.mfllipovic.github.entities.OrderParam
import hr.mfllipovic.github.entities.SearchFilter
import hr.mfllipovic.github.entities.SortByParam
import hr.mfllipovic.github.network.errors.SearchRepositoriesError
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: SearchRepository) : ViewModel() {
    private val filter: SearchFilter

    init {
        SearchFilter().apply {
            filter = this
            filter.addOnPropertyChangedCallback(
                object : Observable.OnPropertyChangedCallback() {
                    override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                        search(filter)
                    }
                }
            )
        }
    }

    val repositories = repository.repositories

    fun onSearchQueryChanged(query: String?) {
        if (query == null || query.isBlank()) {
            emptySearchResults()
        } else {
            filter.query = query
        }
    }

    fun onSortByChanged(sortByParam: SortByParam) {
        filter.sort = sortByParam
    }

    fun onOrderChanged(orderParam: OrderParam) {
        filter.order = orderParam
    }

    fun emptySearchResults() {
        // TODO empty search list and show empty list field
    }

    fun search(filter: SearchFilter) {
        viewModelScope.launch {
            try {
                repository.searchRepositories(filter)
            } catch (error: SearchRepositoriesError) {
                error.printStackTrace()
                Log.e("SearchViewModel", error.message!!)
            }
        }
    }

    class SearchViewModelFactory(private val searchRepository: SearchRepository) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                return SearchViewModel(searchRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

