package hr.mfllipovic.github.screens.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hr.mfllipovic.github.R
import hr.mfllipovic.github.databinding.SearchFragmentBinding
import hr.mfllipovic.github.entities.Repository
import hr.mfllipovic.github.network.getNetworkService
import hr.mfllipovic.github.screens.search.results.OnRepositoryClickListener
import hr.mfllipovic.github.screens.search.results.SearchResultsListAdapter
import hr.mfllipovic.github.screens.search.utils.OnSearchQueryChange
import hr.mfllipovic.github.screens.search.utils.SearchQueryChangeListener

class SearchFragment : Fragment() {

    private lateinit var _binding: SearchFragmentBinding
    private val searchResultsListAdapter: SearchResultsListAdapter =
        SearchResultsListAdapter(onRepositoryClickListener())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(_binding)
        {
            toolbar.inflateMenu(R.menu.search_repositories)
            searchView.setOnQueryTextListener(searchQueryChangeListener())
            searchView.setMenuItem(toolbar.menu.findItem(R.id.action_search))
            searchResultsList.layoutManager = LinearLayoutManager(context)
            searchResultsList.adapter = searchResultsListAdapter
        }
    }

    private lateinit var mViewModel: SearchViewModel
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val gitHubNetwork = getNetworkService()
        val gitHubRepository = SearchRepository(gitHubNetwork)
        val viewModelFactory = SearchViewModel.SearchViewModelFactory(gitHubRepository)
        val viewModel: SearchViewModel by viewModels { viewModelFactory }
        mViewModel = viewModel.apply {
            results.observe(viewLifecycleOwner) {
                postponeEnterTransition()
                it?.let {
                    searchResultsListAdapter.submitList(
                        it.items
                    ) {
                        _binding.searchResults = it
                        setupEmptyListVisibility(false)
                        (view?.parent as? ViewGroup)?.doOnPreDraw {
                            startPostponedEnterTransition()
                        }
                    }
                }
            }
            emptyListReceived.observe(viewLifecycleOwner) {
                it?.let { listEmpty ->
                    if (listEmpty) {
                        searchResultsListAdapter.submitList(listOf()) {
                            setupEmptyListVisibility(listEmpty)
                        }
                    }
                }
            }
        }
    }

    private fun setupEmptyListVisibility(listEmpty: Boolean) {
        _binding.searchResultsInfoView.visibility = if (listEmpty) View.GONE else View.VISIBLE
        _binding.searchResultsList.visibility = if (listEmpty) View.GONE else View.VISIBLE
        _binding.searchResultsEmptyView.visibility = if (!listEmpty) View.GONE else View.VISIBLE

    }

    private fun searchQueryChangeListener() = SearchQueryChangeListener(
        lifecycle,
        object : OnSearchQueryChange {
            override fun onChange(query: String?) {
                mViewModel.onSearchQueryChanged(query)
            }
        })

    private fun onRepositoryClickListener() = object : OnRepositoryClickListener {
        override fun onClick(repository: Repository, imageView: ImageView) {
            val bundle = Bundle()
            bundle.putParcelable("repository", repository)
            val extras = FragmentNavigatorExtras(imageView to "owner_avatar_hero")
            findNavController().navigate(
                R.id.action_search_fragment_to_details_fragment,
                bundle,
                null,
                extras
            )
        }
    }
}
