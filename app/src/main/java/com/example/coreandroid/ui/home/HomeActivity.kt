package com.example.coreandroid.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.base.adapter.ReactiveListAdapter
import com.crocodic.core.extension.hideSoftKeyboard
import com.crocodic.core.extension.openActivity
import com.example.coreandroid.R
import com.example.coreandroid.base.activity.BaseActivity
import com.example.coreandroid.data.room.user.User
import com.example.coreandroid.data.room.user.UserDao
import com.example.coreandroid.databinding.ActivityHomeBinding
import com.example.coreandroid.databinding.ItemFriendBinding
import com.example.coreandroid.ui.login.LoginActivity
import com.example.coreandroid.ui.setting.SettingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(R.layout.activity_home) {

    @Inject
    lateinit var userDao: UserDao
    private var filter: String? = null
    private var friend = ArrayList<User>()
    private var keyword: String? = null

    private val runnable by lazy {
        Runnable {
            refreshData()
        }
    }
    private val handler = Handler(Looper.getMainLooper())

    private val adapter by lazy {
        ReactiveListAdapter<ItemFriendBinding, User>(R.layout.item_friend)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.rvFriend.adapter = adapter

        userDao.getUser().observe(this) {
            binding.user = it
        }

        binding.btnSetting.setOnClickListener {
            openActivity<SettingActivity>()
        }
        binding.btnLogout.setOnClickListener {
            openActivity<LoginActivity>()
            finish()
        }

        binding.etSearch.doAfterTextChanged {
            keyword = if (it.toString().trim().isEmpty()) {
                null
            } else {
                it.toString()
            }

            handler.removeCallbacks(runnable)
            handler.postDelayed(runnable, 1500)
        }
        binding.spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                filter = if (p2 == 0) {
                    null
                } else {
                    binding.spFilter.selectedItem as String
                }
                refreshData()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                filter = null
            }
        }
        binding.cbSort.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                friend.sortByDescending {it?.likes}
            } else {
                friend.sortBy {it?.id}
            }
            binding.rvFriend.adapter?.notifyItemRangeChanged( 0, friend.size)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.friends.collect{
                        friend.clear()
                        binding.rvFriend.adapter?.notifyDataSetChanged()
                        friend.addAll(it)
                        binding.rvFriend.adapter?.notifyItemInserted(0)
                    }
                }
            }
        }
        refreshData()

        binding.etSearch.setOnEditorActionListener { textView, i, keyEvent ->
            textView.hideSoftKeyboard()
            true
        }

        refreshData()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.friends.collect {
                    adapter.submitList(it)
                }
            }
        }
    }
    private fun refreshData() {
        viewModel.getFriends(keyword, filter)
    }
}