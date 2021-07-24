package com.samsad.mvvvmnewsapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsArticleDao {

    //Whenever db get updated flow will emit news list
    @Query("SELECT * FROM breaking_news  INNER JOIN news_articles ON articleUrl = url")
    fun getAllBreakingNewsArticles(): Flow<List<NewsArticle>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<NewsArticle>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBreakingNews(breakingNews: List<BreakingNews>)

    @Query("DELETE FROM breaking_news")
    suspend fun deleteAllBreakingNews()
}