package com.samsad.mvvvmnewsapp.features.breakingnews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samsad.mvvvmnewsapp.data.NewsArticle
import com.samsad.mvvvmnewsapp.data.NewsRepository
import com.samsad.mvvvmnewsapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BreakingNewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {


    lateinit var breakingNews: StateFlow<Resource<List<NewsArticle>>?>

    fun getNews(){
        breakingNews = repository.getBreakingNews()
            .stateIn(viewModelScope, SharingStarted.Lazily, null)
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