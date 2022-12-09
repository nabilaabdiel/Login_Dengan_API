package com.example.coreandroid.ui.splash

import android.os.Bundle
import com.crocodic.core.extension.openActivity
import com.example.coreandroid.R
import com.example.coreandroid.base.activity.BaseActivity
import com.example.coreandroid.databinding.ActivitySplashBinding
import com.example.coreandroid.ui.home.HomeActivity
import com.example.coreandroid.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(R.layout.activity_splash) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.splash {
            if (it) {
                openActivity<HomeActivity>()
            } else {
                openActivity<LoginActivity>()
            }
            finish()
        }
    }
}