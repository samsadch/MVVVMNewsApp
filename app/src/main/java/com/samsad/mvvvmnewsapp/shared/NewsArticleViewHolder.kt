package com.samsad.mvvvmnewsapp.shared

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.samsad.mvvvmnewsapp.R
import com.samsad.mvvvmnewsapp.data.NewsArticle
import com.samsad.mvvvmnewsapp.databinding.ItemNewsArticleBinding

class NewsArticleViewHolder(
    private val binding: ItemNewsArticleBinding,
    private val onItemClick: (Int) -> Unit,
    private val onBookmarkClick: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(article: NewsArticle) {
        binding.apply {
            Glide.with(itemView)
                .load(article.thumbnailUrl)
                .error(R.drawable.image_placeholder)
                .into(imageView)

            textViewTitle.text = article.title ?: ""

            bookmarkImage.setImageResource(
                when {
                    article.isBookmarked -> R.drawable.ic_bookmark_selected
                    else -> R.drawable.ic_bookmark_unselected
                }
            )
        }
    }

    init {
        binding.apply {
            root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(position)
                }
            }
            bookmarkImage.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onBookmarkClick(position)
                }
            }
        }
    }
}