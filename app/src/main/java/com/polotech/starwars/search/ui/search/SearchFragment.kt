package com.polotech.starwars.search.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.polotech.starwars.search.databinding.SearchFragmentBinding
import com.polotech.starwars.search.models.Results
import com.polotech.starwars.search.ui.search.SearchRecyclerAdapter.OnClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModels()

    private lateinit var binding: SearchFragmentBinding
    private lateinit var adapter: SearchRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchFragmentBinding.inflate(inflater, container, false).apply {
            layoutSearch.textSearch.doOnTextChanged { text, start, before, count ->
                viewModel.searchCharacter(text.toString())

            }
        }

        setUpDisplay()

        return binding.root
    }

    private fun setUpDisplay() {
        adapter = SearchRecyclerAdapter(OnClickListener { character ->
            Toast.makeText(requireContext(), "Name : ${character.name}", Toast.LENGTH_SHORT).show()
        })

        binding.recyclerViewSearch.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.character.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Results.Failed -> {
                    Toast.makeText(
                        requireContext(),
                        "Failed ${result.throwable.localizedMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Results.Success -> {
                    adapter.submitList(result.data)
                }

                is Results.Loading -> {
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

}