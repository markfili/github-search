package hr.mfllipovic.github.screens.search

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import hr.mfllipovic.github.R
import hr.mfllipovic.github.databinding.SearchFragmentBinding
import hr.mfllipovic.github.entities.OrderParam
import hr.mfllipovic.github.entities.Repository
import hr.mfllipovic.github.entities.SortByParam
import hr.mfllipovic.github.network.getNetworkService
import hr.mfllipovic.github.screens.search.results.OnRepositoryClickListener
import hr.mfllipovic.github.screens.search.results.SearchResultsListAdapter
import hr.mfllipovic.github.screens.search.utils.OnSearchQueryChange
import hr.mfllipovic.github.screens.search.utils.SearchQueryChangeListener

class SearchFragment : Fragment() {

    private lateinit var _binding: SearchFragmentBinding
    private val searchResultsListAdapter: SearchResultsListAdapter =
        SearchResultsListAdapter(object : OnRepositoryClickListener {
            override fun onClick(repository: Repository) {
                val bundle = Bundle()
                bundle.putParcelable("repository", repository)
                findNavController().navigate(R.id.action_search_fragment_to_details_fragment, bundle)
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.searchResultsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchResultsListAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_repositories, menu)
        val searchItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(SearchQueryChangeListener(lifecycle, object :
            OnSearchQueryChange {
            override fun onChange(query: String?) {
                mViewModel.onSearchQueryChanged(query)
            }
        }))
        super.onCreateOptionsMenu(menu, inflater)
    }

    private var sortMenuItemTitle: String? = null
    private var orderMenuItemTitle: String? = null
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if (sortMenuItemTitle != null) menu.findItem(R.id.app_bar_sort).title = sortMenuItemTitle
        if (orderMenuItemTitle != null) menu.findItem(R.id.app_bar_order).title = orderMenuItemTitle
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_forks -> {
                mViewModel.onSortByChanged(SortByParam.forks)
                true
            }
            R.id.sort_stars -> {
                mViewModel.onSortByChanged(SortByParam.stars)
                true
            }
            R.id.sort_updated -> {
                mViewModel.onSortByChanged(SortByParam.updated)
                true
            }
            R.id.sort_none -> {
                mViewModel.onSortByChanged(SortByParam.none)
                true
            }
            R.id.order_asc -> {
                mViewModel.onOrderChanged(OrderParam.asc)
                true
            }
            R.id.order_desc -> {
                mViewModel.onOrderChanged(OrderParam.desc)
                true
            }
            else -> super.onOptionsItemSelected(item)
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
                it?.let {
                    searchResultsListAdapter.submitList(
                        it.items
                    ) {
                        _binding.searchResults = it
                        setupEmptyListVisibility(false)
                    }
                }
            }
            sortValueText.observe(viewLifecycleOwner) {
                it?.let {
                    sortMenuItemTitle = it
                    requireActivity().invalidateOptionsMenu()
                }
            }
            orderValueText.observe(viewLifecycleOwner) {
                it?.let {
                    orderMenuItemTitle = it
                    requireActivity().invalidateOptionsMenu()
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
}
