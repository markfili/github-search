package hr.mfllipovic.github.screens.search.results

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hr.mfllipovic.github.R
import hr.mfllipovic.github.databinding.SearchResultItemBinding
import hr.mfllipovic.github.entities.Repository

class SearchResultsListAdapter(private val onRepositoryClickListener: OnRepositoryClickListener) :
    ListAdapter<Repository, SearchResultsListAdapter.SearchResultViewHolder>(
        SearchResultsDiffUtilCallback(),
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
        val repository = getItem(position)
        holder.binding.repository = repository
        holder.binding.root.setOnClickListener {
            onRepositoryClickListener.onClick(
                repository,
                holder.binding.repositoryOwnerAvatar
            )
        }
    }

    companion object {

        @JvmStatic
        @BindingAdapter("networkCircularImgSrc")
        fun ImageView.setNetworkImage(networkCircularImgSrc: String?) {
            if (networkCircularImgSrc != null) {
                Glide.with(this.context)
                    .load(networkCircularImgSrc)
                    .circleCrop()
                    .into(this)
            }
        }
    }
}

interface OnRepositoryClickListener {
    fun onClick(repository: Repository, imageView: ImageView)
}