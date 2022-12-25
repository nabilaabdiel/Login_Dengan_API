package com.example.coreandroid.ui.profile

import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiCode
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.extension.toObject
import com.example.coreandroid.api.ApiService
import com.example.coreandroid.base.viewModel.BaseViewModel
import com.example.coreandroid.data.room.user.User
import com.example.coreandroid.data.room.user.UserDao
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val apiService: ApiService,
    private val gson: Gson,
    private val userDao: UserDao
): BaseViewModel() {
    fun update (name: String, school: String, description: String) = viewModelScope.launch {
        _apiResponse.send(ApiResponse().responseLoading())
        ApiObserver(
            {apiService.update(name,school,description)},
            false,
            object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject){

                    val status = response.getInt(ApiCode.STATUS)
                    if (status == ApiCode.SUCCESS) {
                        val data = response.getJSONObject(ApiCode.DATA).toObject<User>(gson)
                        userDao.insert(data.copy(idRoom=1))
                        _apiResponse.send(ApiResponse().responseSuccess())
                    } else {
                        val message = response.getString(ApiCode.MESSAGE)
                        _apiResponse.send(ApiResponse(status = ApiStatus.ERROR, message = message))
                    }
                }
            }
        )
    }
}