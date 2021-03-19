package hr.mfllipovic.github.screens.search.results

import androidx.recyclerview.widget.DiffUtil
import hr.mfllipovic.github.entities.Repository

class SearchResultsDiffUtilCallback : DiffUtil.ItemCallback<Repository>() {
    override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return oldItem == newItem
    }
}