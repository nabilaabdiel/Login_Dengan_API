package com.example.coreandroid.ui.login

import android.os.Bundle
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.data.CoreSession
import com.crocodic.core.extension.isEmptyRequired
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.textOf
import com.crocodic.core.extension.tos
import com.example.coreandroid.R
import com.example.coreandroid.base.activity.BaseActivity
import com.example.coreandroid.data.constant.Const
import com.example.coreandroid.databinding.ActivityLoginBinding
import com.example.coreandroid.ui.home.HomeActivity
import com.example.coreandroid.ui.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {

    @Inject
    lateinit var session: CoreSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnLoginWithBiometric.isVisible  = session.getBoolean(Const.BIOMETRIC)

        binding.btnLogin.setOnClickListener {
            if (binding.etPhone.isEmptyRequired(R.string.label_must_fill) ||
                binding.etPassword.isEmptyRequired(R.string.label_must_fill)){
                return@setOnClickListener
            }

            val phone = binding.etPhone.textOf()
            val password = binding.etPassword.textOf()

            viewModel.login(phone, password)
        }

        binding.btnSignIn.setOnClickListener {
            openActivity<RegisterActivity>() {
                finish()
            }
        }

        binding.btnLoginWithBiometric.setOnClickListener {
            showBiometricPrompt()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.apiResponse.collect {
                        when (it.status) {
                            ApiStatus.LOADING -> loadingDialog.show("Logging in...")
                            ApiStatus.SUCCESS -> {
                                loadingDialog.dismiss()
                                openActivity<HomeActivity>()
                                finish()
                            }
                            else -> loadingDialog.setResponse(it.message ?: return@collect)
                        }
                    }
                }
            }
        }
    }
    private fun showBiometricPrompt() {
        val builder = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Enter biometric credential to proceed")
            .setDescription("Input your Fingerprint or FaceID to ensure it's you!")
            .setNegativeButtonText("Cancel")

        val promptInfo = builder.build()

        val biometricPrompt = initBiometricPrompt {
            viewModel.login(session.getString(Const.PHONE), session.getString(Const.PASSWORD))
        }

        biometricPrompt.authenticate(promptInfo)
    }

    private fun initBiometricPrompt(listener: (Boolean) -> Unit): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(this)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                listener(true)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                listener(false)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                listener(false)
            }
        }
        return BiometricPrompt(this, executor, callback)
    }
}