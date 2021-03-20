package hr.mfllipovic.github.screens.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hr.mfllipovic.github.R
import hr.mfllipovic.github.databinding.FragmentDetailsBinding
import hr.mfllipovic.github.entities.Repository

class DetailsFragment : Fragment() {

    private lateinit var _binding: FragmentDetailsBinding

    private lateinit var repository: Repository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            repository = it.get("repository") as Repository
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        _binding.repository = repository
    }
}