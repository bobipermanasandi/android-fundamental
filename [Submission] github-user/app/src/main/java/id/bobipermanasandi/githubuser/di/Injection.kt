package id.bobipermanasandi.githubuser.di

import android.content.Context
import id.bobipermanasandi.githubuser.data.DataRepository
import id.bobipermanasandi.githubuser.data.local.room.FavoriteDatabase
import id.bobipermanasandi.githubuser.data.remote.api.ApiConfig
import id.bobipermanasandi.githubuser.ui.setting.SettingPreferences
import id.bobipermanasandi.githubuser.ui.setting.dataStore

object Injection {
    fun provideRepository(context: Context): DataRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteDatabase.getInstance(context)
        val dao = database.favoriteDao()
        val pref = SettingPreferences.getInstance(context.dataStore)
        return DataRepository.getInstance(apiService, dao, pref)
    }
}