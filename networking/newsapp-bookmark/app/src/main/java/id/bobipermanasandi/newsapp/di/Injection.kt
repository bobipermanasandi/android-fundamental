package id.bobipermanasandi.newsapp.di

import android.content.Context
import id.bobipermanasandi.newsapp.data.NewsRepository
import id.bobipermanasandi.newsapp.data.local.room.NewsDatabase
import id.bobipermanasandi.newsapp.data.remote.retrofit.ApiConfig
import id.bobipermanasandi.newsapp.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): NewsRepository {
        val apiService = ApiConfig.getApiService()
        val database = NewsDatabase.getInstance(context)
        val dao = database.newsDao()
        val appExecutors = AppExecutors()
        return NewsRepository.getInstance(apiService, dao, appExecutors)
    }
}