package hr.mfllipovic.github.screens.search

import android.util.Log
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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

    val results = repository.results
    val sortValueText = MutableLiveData<String>()
    val orderValueText = MutableLiveData<String>()

    private val _emptyListReceived = MutableLiveData<Boolean>()
    val emptyListReceived
        get() = _emptyListReceived

    fun onSearchQueryChanged(query: String?) {
        filter.query = query ?: ""
    }

    fun onSortByChanged(sortByParam: SortByParam) {
        filter.sort = sortByParam
        sortValueText.value = sortByParam.name
    }

    fun onOrderChanged(orderParam: OrderParam) {
        filter.order = orderParam
        orderValueText.value = orderParam.name
    }

    fun search(filter: SearchFilter) {
        if (filter.query.isNotBlank()) {
            fetchRepositories(filter)
            _emptyListReceived.value = false
        } else {
            _emptyListReceived.value = true
        }
    }

    private fun fetchRepositories(filter: SearchFilter) {
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

