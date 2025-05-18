package com.adopet.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.adopet.app.R
import com.adopet.app.data.model.DataItem
import com.adopet.app.databinding.FragmentHomeBinding
import com.adopet.app.ui.adapter.PostAdapter
import com.adopet.app.utils.Result
import com.adopet.app.utils.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewModel: HomeViewModel
    private val binding get() = _binding!!
    private lateinit var newestAdapter: PostAdapter
    private lateinit var dogAdapter: PostAdapter
    private lateinit var catAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        binding.tvWelcome.text = getString(R.string.hint_welcome_user)

        setupRecyclerViews()

        viewModel.getNewestPosts().observe(requireActivity()) { result ->
            when(result) {
                is Result.Error -> {
                    showToast(result.error)
                }
                Result.Loading -> {

                }
                is Result.Success -> {
                    val posts = result.data.data?.filterNotNull() ?: emptyList()
                    setData(posts)
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setData(posts: List<DataItem>) {
        newestAdapter.submitList(posts)

        val dogPosts = posts.filter { it.petType.equals("Dog", ignoreCase = true) }
        val catPosts = posts.filter { it.petType.equals("Cat", ignoreCase = true) }

        dogAdapter.submitList(dogPosts)
        catAdapter.submitList(catPosts)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setupRecyclerViews() {
        newestAdapter = PostAdapter()
        binding.rvNewestPosts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = newestAdapter
        }

        dogAdapter = PostAdapter()
        binding.rvDogPosts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = dogAdapter
        }

        catAdapter = PostAdapter()
        binding.rvCatPosts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = catAdapter
        }
    }

    companion object {
        var name: String = ""
    }

}