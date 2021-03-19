package hr.mfllipovic.github.screens.search

import androidx.lifecycle.MutableLiveData
import hr.mfllipovic.github.entities.Repository
import hr.mfllipovic.github.entities.SearchFilter
import hr.mfllipovic.github.network.GitHubNetwork
import hr.mfllipovic.github.network.errors.SearchRepositoriesError

class SearchRepository(private val network: GitHubNetwork) {

    private val _repositories = MutableLiveData<List<Repository>>()

    val repositories
        get() = _repositories

    suspend fun searchRepositories(filter: SearchFilter) {
        try {
            val queryMap = filter.toMap()
            _repositories.value = network.searchRepositories(queryMap).items
        } catch (cause: Throwable) {
            throw SearchRepositoriesError("Error fetching repositories", cause)
        }
    }
}