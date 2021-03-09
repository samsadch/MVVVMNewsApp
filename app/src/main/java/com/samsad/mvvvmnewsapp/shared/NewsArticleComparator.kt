package com.samsad.mvvvmnewsapp.shared

import androidx.recyclerview.widget.DiffUtil
import com.samsad.mvvvmnewsapp.data.NewsArticle

class NewsArticleComparator : DiffUtil.ItemCallback<NewsArticle>() {

    override fun areItemsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean =
        oldItem.url == newItem.url


    override fun areContentsTheSame(oldItem: NewsArticle, newItem: NewsArticle): Boolean =
        oldItem == newItem
}