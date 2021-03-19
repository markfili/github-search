package hr.mfllipovic.github.screens.search.results

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hr.mfllipovic.github.R
import hr.mfllipovic.github.databinding.SearchResultItemBinding
import hr.mfllipovic.github.entities.Repository

class SearchResultsListAdapter :
    ListAdapter<Repository, SearchResultsListAdapter.SearchResultViewHolder>(
        SearchResultsDiffUtilCallback()
    ) {

    inner class SearchResultViewHolder(var binding: SearchResultItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<SearchResultItemBinding>(
            inflater,
            R.layout.search_result_item,
            parent,
            false
        )
        return SearchResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.binding.repository = getItem(position)
    }
}

