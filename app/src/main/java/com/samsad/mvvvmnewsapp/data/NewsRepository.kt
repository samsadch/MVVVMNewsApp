package com.samsad.mvvvmnewsapp.data

import androidx.room.withTransaction
import com.samsad.mvvvmnewsapp.api.NewsApi
import com.samsad.mvvvmnewsapp.util.Resource
import com.samsad.mvvvmnewsapp.util.networkBoundResource
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

//When its time to instantiate this class, injects the dependencies from the DI Module
class NewsRepository @Inject constructor(
    private val newsApi: NewsApi,
    private val newsArticleDb: NewsArticleDatabase
) {
    private val newsArticleDao = newsArticleDb.newsArticleDao()

    fun getBreakingNews(
        forceRefresh: Boolean,
        onFetchSuccess: () -> Unit,
        onFetchFailed: (Throwable) -> Unit
    ): Flow<Resource<List<NewsArticle>>> =
        networkBoundResource(
            query = {
                newsArticleDao.getAllBreakingNewsArticles()
            },
            fetch = {
                val response = newsApi.getBreakingNews()
                response.articles
            },
            saveFetchResult = { serverBreakingNewsArticles ->
                val breakingNewsArticles =
                    serverBreakingNewsArticles.map { serverBreakingNewsArticle ->
                        NewsArticle(
                            title = serverBreakingNewsArticle.title,
                            url = serverBreakingNewsArticle.url,
                            thumbnailUrl = serverBreakingNewsArticle.urlToImage,
                            isBookmarked = false
                        )
                    }
                val breakingNews = breakingNewsArticles.map { article ->
                    BreakingNews(articleUrl = article.url)
                }

                newsArticleDb.withTransaction {
                    newsArticleDao.deleteAllBreakingNews()
                    newsArticleDao.insertArticles(breakingNewsArticles)
                    newsArticleDao.insertBreakingNews(breakingNews)
                }
            },
            shouldFetch = { cachedArticles ->
                if (forceRefresh) {
                    true
                } else {
                    val sortedArticles = cachedArticles.sortedBy { article ->
                        article.updatedAt
                    }
                    val oldestTimeStamp = sortedArticles.firstOrNull()?.updatedAt
                    val needsRefresh = oldestTimeStamp == null ||
                            oldestTimeStamp < System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(
                        5
                    )
                    needsRefresh
                }
            },
            onFetchSuccess = onFetchSuccess,
            onFetchFailed = { t ->
                if (t !is HttpException && t !is IOException) {
                    throw t
                }
                onFetchFailed(t)
            }
        )

    suspend fun deleteNonBookmarkedArticlesOlderThan(timeStamp: Long) {
        newsArticleDao.deleteNonBookmarkedArticlesOlderThan(timeStamp)
    }
}