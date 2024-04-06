package id.bobipermanasandi.githubuser.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import id.bobipermanasandi.githubuser.data.DataRepository
import id.bobipermanasandi.githubuser.data.ResultData
import id.bobipermanasandi.githubuser.data.remote.response.UserItem
import id.bobipermanasandi.githubuser.data.remote.response.UserSearchResponse
import id.bobipermanasandi.githubuser.utils.Event
import retrofit2.*

class MainViewModel(private val repository: DataRepository) : ViewModel() {

    private val _resultData = MutableLiveData<ResultData<List<UserItem>>>()
    val resultData: LiveData<ResultData<List<UserItem>>> = _resultData

    private val _snackbarText = MutableLiveData<Event<String>>()
    val snackbarText: LiveData<Event<String>> = _snackbarText

    fun getThemeSetting(): LiveData<Boolean> {
        return repository.getThemeSetting().asLiveData()
    }
    init {
        searchUser(INITIAL_QUERY)
    }

    fun searchUser(query: String) {
        _resultData.value = ResultData.Loading
        val client = repository.searchUser(query)
        client.enqueue(object : Callback<UserSearchResponse> {
            override fun onResponse(
                call: Call<UserSearchResponse>,
                response: Response<UserSearchResponse>
            ) {
                if (response.isSuccessful) {
                    _resultData.value = ResultData.Success(response.body()?.items as List<UserItem>)
                } else {
                    _snackbarText.value = Event("onFailure: ${response.message()}")
                    _resultData.value = ResultData.Error(response.message())
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<UserSearchResponse>, t: Throwable) {
                _snackbarText.value = Event("onFailure: ${t.message.toString()}")
                _resultData.value = ResultData.Error(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        const val TAG = "MainViewModel"
        const val INITIAL_QUERY = "arif"
    }
}