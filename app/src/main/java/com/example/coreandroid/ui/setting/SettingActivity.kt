package com.example.coreandroid.ui.setting

import android.os.Bundle
import androidx.biometric.BiometricManager
import com.crocodic.core.data.CoreSession
import com.crocodic.core.extension.openActivity
import com.example.coreandroid.R
import com.example.coreandroid.base.activity.BaseActivity
import com.example.coreandroid.data.constant.Const
import com.example.coreandroid.databinding.ActivitySettingBinding
import com.example.coreandroid.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingActivity :BaseActivity<ActivitySettingBinding, SettingViewModel>(R.layout.activity_setting) {

    @Inject
    lateinit var session: CoreSession

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.hasBiometric = hasBiometricCapability()
        binding.enableBiometric = session.getBoolean(Const.BIOMETRIC)

        binding.btnBiometric.setOnCheckedChangeListener { buttonView, isChecked ->
        session.setValue(Const.BIOMETRIC, isChecked)

        binding.btnLogout.setOnClickListener {
            viewModel.logout {
                openActivity<LoginActivity>()
                finish()
            }
        }
        }
    }
    private fun hasBiometricCapability(): Boolean {
        val biometricManager = BiometricManager.from(this)
        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
    }

//    override fun onClick(v: View?) {
//        when(v) {
//            binding.btnLogout -> {
//                authLogoutSuccess()
//                openActivity<LoginActivity>
//                finishAffinity()
//            }
//        }
//        super.onClick(v)
//    }
}