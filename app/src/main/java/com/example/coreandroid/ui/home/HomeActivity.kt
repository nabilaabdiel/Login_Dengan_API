package com.example.coreandroid.ui.home

import android.os.Bundle
import com.crocodic.core.extension.openActivity
import com.example.coreandroid.R
import com.example.coreandroid.base.activity.BaseActivity
import com.example.coreandroid.data.room.user.UserDao
import com.example.coreandroid.databinding.ActivityHomeBinding
import com.example.coreandroid.ui.login.LoginActivity
import com.example.coreandroid.ui.setting.SettingActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(R.layout.activity_home) {

    @Inject
    lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userDao.getUser().observe(this){
            binding.user = it
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout {
                openActivity<LoginActivity>()
                finish()
            }
        }

        binding.btnSetting.setOnClickListener {
            openActivity<SettingActivity>() {
                finish()
            }
        }
    }
}