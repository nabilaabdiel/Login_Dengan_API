package com.example.coreandroid.ui.home

import androidx.lifecycle.viewModelScope
import com.example.coreandroid.base.viewModel.BaseViewModel
import com.example.coreandroid.data.room.user.UserDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val userDao: UserDao): BaseViewModel() {
    fun logout(logout: () ->Unit) = viewModelScope.launch {
        userDao.deleteAll()
        logout()
    }
}