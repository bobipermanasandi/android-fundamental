package id.bobipermanasandi.githubuser.data

import androidx.lifecycle.LiveData
import id.bobipermanasandi.githubuser.data.local.entity.FavoriteEntity
import id.bobipermanasandi.githubuser.data.local.room.FavoriteDao
import id.bobipermanasandi.githubuser.data.remote.api.ApiService
import id.bobipermanasandi.githubuser.data.remote.response.FollowResponseItem
import id.bobipermanasandi.githubuser.data.remote.response.UserDetailResponse
import id.bobipermanasandi.githubuser.data.remote.response.UserSearchResponse
import id.bobipermanasandi.githubuser.ui.setting.SettingPreferences
import kotlinx.coroutines.flow.Flow
import retrofit2.Call

class DataRepository private constructor(
    private val apiService: ApiService,
    private val favoriteDao: FavoriteDao,
    private val settingPreferences: SettingPreferences,

    ) {
    suspend fun insertFavorite(favoriteEntity: FavoriteEntity) {
        favoriteDao.insert(favoriteEntity)
    }
    suspend fun deleteFavorite(favoriteEntity: FavoriteEntity) {
        favoriteDao.delete(favoriteEntity)
    }

    fun getAllFavorite(): LiveData<List<FavoriteEntity>> {
        return favoriteDao.getAllFavorite()
    }

    fun getFavoriteByUsername(username: String) : LiveData<FavoriteEntity> {
        return favoriteDao.getFavoriteByUsername(username)
    }

    fun getThemeSetting(): Flow<Boolean> {
        return settingPreferences.getThemeSetting()
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        settingPreferences.saveThemeSetting(isDarkModeActive)
    }

    fun searchUser(query: String) : Call<UserSearchResponse> {
        return apiService.searchUser(query)
    }

    fun getDetailUser(username: String) : Call<UserDetailResponse> {
        return apiService.getDetailUser(username)
    }


    fun getFollowers(username: String): Call<List<FollowResponseItem>> {
        return apiService.getFollowers(username)
    }

    fun getFollowing(username: String): Call<List<FollowResponseItem>> {
        return apiService.getFollowing(username)
    }

    companion object {
        private const val TAG = "DataRepository"

        @Volatile
        private var instance: DataRepository? = null
        fun getInstance(
            apiService: ApiService,
            favoriteDao: FavoriteDao,
            settingPreferences: SettingPreferences,
            ): DataRepository =
            instance ?: synchronized(this) {
                instance ?: DataRepository(apiService, favoriteDao, settingPreferences)
            }.also { instance = it }
    }
}