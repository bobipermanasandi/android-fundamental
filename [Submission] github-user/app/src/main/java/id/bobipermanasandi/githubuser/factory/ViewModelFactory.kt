package id.bobipermanasandi.githubuser.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.bobipermanasandi.githubuser.data.DataRepository
import id.bobipermanasandi.githubuser.di.Injection
import id.bobipermanasandi.githubuser.ui.detail.DetailViewModel
import id.bobipermanasandi.githubuser.ui.detail.follow.FollowViewModel
import id.bobipermanasandi.githubuser.ui.favorite.FavoriteViewModel
import id.bobipermanasandi.githubuser.ui.main.MainViewModel
import id.bobipermanasandi.githubuser.ui.setting.SettingViewModel

class ViewModelFactory private constructor(private val dataRepository: DataRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dataRepository) as T
        }
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(dataRepository) as T
        }
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(dataRepository) as T
        }
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(dataRepository) as T
        }
        if (modelClass.isAssignableFrom(FollowViewModel::class.java)) {
            return FollowViewModel(dataRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}