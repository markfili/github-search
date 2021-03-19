package hr.mfllipovic.github.screens.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import hr.mfllipovic.github.databinding.SearchFragmentBinding
import hr.mfllipovic.github.network.getNetworkService
import hr.mfllipovic.github.screens.search.results.SearchResultsListAdapter

class SearchFragment : Fragment() {

    private lateinit var _binding: SearchFragmentBinding
    private val searchResultsListAdapter: SearchResultsListAdapter =
        SearchResultsListAdapter()

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val gitHubNetwork = getNetworkService()
        val gitHubRepository = SearchRepository(gitHubNetwork);
        val viewModelFactory = SearchViewModel.SearchViewModelFactory(gitHubRepository);
        val mViewModel: SearchViewModel by viewModels { viewModelFactory }
        mViewModel.repositories.observe(viewLifecycleOwner) {
            it?.let {
                searchResultsListAdapter.submitList(it)
            }
        }
    }
}