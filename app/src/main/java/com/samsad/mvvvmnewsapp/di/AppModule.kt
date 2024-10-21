package com.samsad.mvvvmnewsapp.di

import android.app.Application
import androidx.room.Room
import com.samsad.mvvvmnewsapp.api.NewsApi
import com.samsad.mvvvmnewsapp.data.NewsArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()


    @Provides
    @Singleton
    fun provideRetrofit():Retrofit =
        Retrofit.Builder()
            .baseUrl(NewsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit):NewsApi =
        retrofit.create(NewsApi::class.java)


    @Provides
    @Singleton
    fun provideDatabase(app: Application):NewsArticleDatabase =
        Room.databaseBuilder(app,NewsArticleDatabase::class.java, "news_article_database")
            .fallbackToDestructiveMigration()
            .build()


}