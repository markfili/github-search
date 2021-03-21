package hr.mfllipovic.github.screens.search.results

import android.widget.ImageView
import com.airbnb.epoxy.TypedEpoxyController
import hr.mfllipovic.github.entities.Repository
import hr.mfllipovic.github.item

/**
 * Custom Epoxy RecyclerView controller for displaying search results.
 */
class SearchResultsEpoxyController(private val onRepositoryClickListener: OnRepositoryClickListener) :
    TypedEpoxyController<List<Repository>>() {
    override fun buildModels(data: List<Repository>?) {
        data?.forEach { repository ->
            addRepositoryInfo(repository)
        }
    }

    private fun addRepositoryInfo(repository: Repository) {
        item {
            id(repository.id)
            repository(repository)
            onClickListener(onRepositoryClickListener)

        }
    }
}

/**
 * Handles tapping on a search result, with owner's image transition.
 */
interface OnRepositoryClickListener {
    fun onRepositoryClick(imageView: ImageView, repository: Repository)
}