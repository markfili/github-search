package hr.mfllipovic.github.screens.search

import androidx.lifecycle.MutableLiveData
import hr.mfllipovic.github.entities.SearchFilter
import hr.mfllipovic.github.entities.SearchResults
import hr.mfllipovic.github.network.GitHubNetwork
import hr.mfllipovic.github.network.errors.SearchRepositoriesError

class SearchRepository(private val network: GitHubNetwork) {

    private val _results = MutableLiveData<SearchResults>()

    val results
        get() = _results

    suspend fun searchRepositories(filter: SearchFilter) {
        try {
            val queryMap = filter.toMap()
            _results.value = network.searchRepositories(queryMap)
        } catch (cause: Throwable) {
            throw SearchRepositoriesError("Error fetching repositories", cause)
        }
    }
}