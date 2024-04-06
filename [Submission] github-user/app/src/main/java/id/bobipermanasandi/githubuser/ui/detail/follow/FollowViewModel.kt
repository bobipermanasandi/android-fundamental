package id.bobipermanasandi.githubuser.ui.detail.follow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.bobipermanasandi.githubuser.data.DataRepository
import id.bobipermanasandi.githubuser.data.remote.response.FollowResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel(private val repository: DataRepository) : ViewModel() {
    private val _follow = MutableLiveData<List<FollowResponseItem>>()
    val follow: LiveData<List<FollowResponseItem>> = _follow

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getDataFollow(username: String, position: Int) {
        _isLoading.value = true
        val client = when(position) {
            1 -> repository.getFollowers(username)
            else -> repository.getFollowing(username)
        }

        client.enqueue(object : Callback<List<FollowResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowResponseItem>>,
                response: Response<List<FollowResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _follow.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<FollowResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        const val TAG = "FollowViewModel"
    }
}