package id.bobipermanasandi.githubuser.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.bobipermanasandi.githubuser.data.DataRepository
import id.bobipermanasandi.githubuser.data.local.entity.FavoriteEntity
import id.bobipermanasandi.githubuser.data.remote.response.UserDetailResponse
import id.bobipermanasandi.githubuser.utils.Event
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(private val repository: DataRepository) : ViewModel() {
    private val _user = MutableLiveData<UserDetailResponse>()
    val user: LiveData<UserDetailResponse> = _user

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    fun addFavorite(favoriteEntity: FavoriteEntity) {
        viewModelScope.launch {
            repository.insertFavorite(favoriteEntity)
        }
    }

    fun deleteFavorite(favoriteEntity: FavoriteEntity) {
        viewModelScope.launch {
            repository.deleteFavorite(favoriteEntity)
        }
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteEntity> {
        return repository.getFavoriteByUsername(username)
    }

    fun getDetailUser(username: String) {
        _isLoading.value = true
        val client = repository.getDetailUser(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _user.value = response.body()
                } else {
                    _snackbarText.value = Event("onFailure: ${response.message()}")
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
                _snackbarText.value = Event("onFailure: ${t.message.toString()}")
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }


    companion object {
        const val TAG = "DetailViewModel"
    }

}