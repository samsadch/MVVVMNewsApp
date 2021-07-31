package com.samsad.mvvvmnewsapp.features.breakingnews

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.samsad.mvvvmnewsapp.R
import com.samsad.mvvvmnewsapp.databinding.FragmentBreakingNewsBinding
import com.samsad.mvvvmnewsapp.shared.NewsArticleAdapter
import com.samsad.mvvvmnewsapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {
    private val viewModel: BreakingNewsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentBreakingNewsBinding.bind(view)
        val newsArticleAdapter = NewsArticleAdapter()
        binding.apply {
            recyclerView.apply {
                adapter = newsArticleAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.breakingNews.collect {
                    //return from this if the result is null
                    val result = it ?: return@collect

                    it.let {
                        swipeRefreshLayout.isRefreshing = result is Resource.Loading
                        recyclerView.isVisible = !result.data.isNullOrEmpty()
                        textViewError.isVisible =
                            result.error != null && result.data.isNullOrEmpty()
                        buttonRetry.isVisible =
                            result.error != null && result.data.isNullOrEmpty()
                        textViewError.text = getString(
                            R.string.could_not_refresh,
                            result.error?.localizedMessage
                                ?: getString(R.string.unknown_error_occurred)
                        )
                        newsArticleAdapter.submitList(result.data)
                    }
                    //newsArticleAdapter.submitList(it)
                }
            }

            swipeRefreshLayout.setOnRefreshListener {
                viewModel.onManualRefresh()
            }

            buttonRetry.setOnClickListener {
                viewModel.onManualRefresh()
            }
        }
        viewModel.getNews()
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }
}