package com.adopet.app.ui.history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.adopet.app.R
import com.adopet.app.data.model.PostsItem
import com.adopet.app.databinding.FragmentHistoryBinding
import com.adopet.app.ui.adapter.HistoryAdapter
import com.adopet.app.ui.login.LoginActivity
import com.adopet.app.utils.Result
import com.adopet.app.utils.ViewModelFactory

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HistoryViewModel
    private lateinit var historyAdapter: HistoryAdapter
    private var menuProvider: MenuProvider? = null

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

        menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.app_bar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_logout -> {
                        viewModel.deleteSession()
                        Log.d(TAG, "onMenuItemSelected: DeleteSession")
                        startActivity(
                            Intent(requireActivity(), LoginActivity::class.java)
                        )
                        true
                    }

                    else -> false
                }
            }
        }

        requireActivity().addMenuProvider(menuProvider!!, viewLifecycleOwner)

        historyAdapter = HistoryAdapter()
        binding.rvHistory.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.rvHistory.adapter = historyAdapter

        viewModel.getHistory().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> showToast(result.error)
                Result.Loading -> {}
                is Result.Success -> setupData(result.data.posts)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        menuProvider?.let {
            requireActivity().removeMenuProvider(it)
        }
        menuProvider = null
    }

    private fun setupData(data: List<PostsItem?>?) {
        historyAdapter.submitList(data)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "HistoryFragment"
    }
}
