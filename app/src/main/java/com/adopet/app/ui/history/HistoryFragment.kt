package com.adopet.app.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.adopet.app.data.model.PostsItem
import com.adopet.app.databinding.FragmentHistoryBinding
import com.adopet.app.ui.adapter.HistoryAdapter
import com.adopet.app.utils.Result
import com.adopet.app.utils.ViewModelFactory

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private lateinit var viewModel: HistoryViewModel
    private lateinit var historyAdapter: HistoryAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]

        historyAdapter = HistoryAdapter()
        val gridColumnCount = 2
        binding.rvHistory.layoutManager = GridLayoutManager(requireActivity(), gridColumnCount)
        binding.rvHistory.adapter = historyAdapter

        viewModel.getHistory().observe(requireActivity()) { result ->
            when (result) {
                is Result.Error -> {
                    showToast(result.error)
                }
                Result.Loading -> {

                }
                is Result.Success -> {
                    setupData(result.data.posts)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupData(data: List<PostsItem?>?) {
        historyAdapter.submitList(data)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}