package hr.mfllipovic.github.screens.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import hr.mfllipovic.github.entities.OrderParam
import hr.mfllipovic.github.entities.SearchFilter
import hr.mfllipovic.github.entities.SortByParam
import hr.mfllipovic.github.network.errors.SearchRepositoriesError
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: SearchRepository) : ViewModel() {
    init {
        search(SearchFilter(query = "hello", sort = SortByParam.none, order = OrderParam.none))
    }

    val repositories = repository.repositories

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

