package com.samsad.mvvvmnewsapp.features.breakingnews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samsad.mvvvmnewsapp.data.NewsArticle
import com.samsad.mvvvmnewsapp.data.NewsRepository
import com.samsad.mvvvmnewsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreakingNewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val refreshingTriggerChannel = Channel<Unit>()
    private val refreshTrigger = refreshingTriggerChannel.receiveAsFlow()

    lateinit var breakingNews: StateFlow<Resource<List<NewsArticle>>?>

    fun getNews() {
        //stateIn turns flow into mutable stateFlow
        /* breakingNews = repository.getBreakingNews()
             .stateIn(viewModelScope, SharingStarted.Lazily, null)*/

        breakingNews = refreshTrigger.flatMapLatest {
            repository.getBreakingNews()
        }.stateIn(viewModelScope, SharingStarted.Lazily, null)
    }

    fun onStart() {
        if (breakingNews.value !is Resource.Loading) {
            viewModelScope.launch {
                refreshingTriggerChannel.send(Unit)
            }
        }
    }

    fun onManualRefresh() {
        if (breakingNews.value !is Resource.Loading) {
            viewModelScope.launch {
                refreshingTriggerChannel.send(Unit)
            }
        }
    }

    /*private val breakingNewsFlow = MutableStateFlow<List<NewsArticle>>(emptyList())
    val breakingNews: Flow<List<NewsArticle>> = breakingNewsFlow

    init {
        //Coroutines advantages
        //1.Remove callbacks
        //2.structured concurrency -
        viewModelScope.launch {
            val news = repository.getBreakingNews()
            breakingNewsFlow.value = news
        }
    }*/
}