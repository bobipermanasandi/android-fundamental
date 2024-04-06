package id.bobipermanasandi.githubuser.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import id.bobipermanasandi.githubuser.data.DataRepository
import id.bobipermanasandi.githubuser.data.local.entity.FavoriteEntity

class FavoriteViewModel(private val repository: DataRepository) : ViewModel()  {
    fun getAllFavorite(): LiveData<List<FavoriteEntity>> {
        return repository.getAllFavorite()
    }
}