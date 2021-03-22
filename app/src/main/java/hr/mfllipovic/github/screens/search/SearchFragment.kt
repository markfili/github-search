package hr.mfllipovic.github.screens.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import hr.mfllipovic.github.R
import hr.mfllipovic.github.databinding.SearchFragmentBinding
import hr.mfllipovic.github.entities.OrderParam
import hr.mfllipovic.github.entities.Owner
import hr.mfllipovic.github.entities.Repository
import hr.mfllipovic.github.entities.SortByParam
import hr.mfllipovic.github.screens.search.results.OnRepositoryClickListener
import hr.mfllipovic.github.screens.search.results.SearchResultsEpoxyController
import hr.mfllipovic.github.screens.search.utils.OnSearchQueryChange
import hr.mfllipovic.github.screens.search.utils.SearchQueryChangeListener
import hr.mfllipovic.github.utils.launchInBrowser
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment() : Fragment() {

    private val searchResultsListController =
        SearchResultsEpoxyController(onRepositoryClickListener())
    private lateinit var _binding: SearchFragmentBinding

    @Inject
    lateinit var factory: SearchViewModel.SearchViewModelFactory
    private val mViewModel: SearchViewModel by viewModels { factory }

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
            searchResultsList.setController(searchResultsListController)
            sortSpinner.onItemSelectedListener = onSortItemSelectedListener()
            orderSpinner.onItemSelectedListener = onOrderItemSelectedListener()
        }
        setHasOptionsMenu(true)
        mViewModel.apply {
            results.observe(viewLifecycleOwner) {
                postponeEnterTransition()
                it?.let {
                    searchResultsListController.setData(it.items)
                    _binding.searchResults = it
                    (view.parent as? ViewGroup)?.doOnPreDraw {
                        startPostponedEnterTransition()
                    }
                }
            }
            loader.observe(viewLifecycleOwner) {
                _binding.loader = it
            }
        }
    }

    private fun onSortItemSelectedListener() = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            mViewModel.onSortByChanged(
                SortByParam.valueOf(
                    parent?.getItemAtPosition(
                        position
                    ) as String
                )
            )
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            mViewModel.onSortByChanged(SortByParam.match)
        }
    }

    private fun onOrderItemSelectedListener() = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            val value = parent?.getItemAtPosition(
                position
            ) as String
            Log.i("Spinner", value)
            mViewModel.onOrderChanged(
                OrderParam.valueOf(
                    value
                )
            )
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            mViewModel.onOrderChanged(OrderParam.original)
        }
    }

    private fun searchQueryChangeListener() = SearchQueryChangeListener(
        lifecycle,
        object : OnSearchQueryChange {
            override fun onChange(query: String?) {
                mViewModel.onSearchQueryChanged(query)
            }
        })

    private fun onRepositoryClickListener() = object : OnRepositoryClickListener {
        override fun onRepositoryClick(imageView: ImageView, repository: Repository) {
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

        override fun onOwnerImageClick(owner: Owner) {
            launchInBrowser(requireContext(), owner.htmlUrl)
        }
    }
}
