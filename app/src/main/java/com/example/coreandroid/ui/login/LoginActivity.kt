package com.example.coreandroid.ui.login

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.extension.isEmptyRequired
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.textOf
import com.example.coreandroid.R
import com.example.coreandroid.base.activity.BaseActivity
import com.example.coreandroid.databinding.ActivityLoginBinding
import com.example.coreandroid.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnLogin.setOnClickListener {
            if(binding.etPhone.isEmptyRequired(R.string.label_must_fill) ||
                binding.etPassword.isEmptyRequired(R.string.label_must_fill)) {
                return@setOnClickListener
            }
            val phone = binding.etPhone.textOf()
            val password = binding.etPassword.textOf()

            viewModel.login(phone, password)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.apiResponse.collect{
                        when (it.status) {
                            ApiStatus.LOADING -> loadingDialog.show("Logging in...")
                            ApiStatus.SUCCESS -> {
                                loadingDialog.dismiss()
                                openActivity<HomeActivity> ()
                                finish()
                            }
                            else -> loadingDialog.setResponse(it.message ?:return@collect)
                        }
                    }
                }
            }
        }
    }
}