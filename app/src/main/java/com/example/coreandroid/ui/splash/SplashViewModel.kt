package com.example.coreandroid.ui.splash

import androidx.lifecycle.viewModelScope
import com.example.coreandroid.base.viewModel.BaseViewModel
import com.example.coreandroid.data.room.user.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val userDao: UserDao): BaseViewModel() {
    fun splash(done: (Boolean) -> Unit) = viewModelScope.launch {
        delay(3000)
        done(userDao.isLogin())
    }
}