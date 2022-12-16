package com.example.coreandroid.ui.article

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.api.DataObserver
import com.crocodic.core.base.adapter.CoreListAdapter
import com.crocodic.core.base.adapter.CoreListAdapter.Companion.get
import com.crocodic.core.extension.initLoadMore
import com.crocodic.core.extension.tos
import com.crocodic.core.helper.list.EndlessScrollListener
import com.example.coreandroid.R
import com.example.coreandroid.base.activity.BaseActivity
import com.example.coreandroid.data.model.Article
import com.example.coreandroid.databinding.ActivityArticleBinding
import com.example.coreandroid.databinding.ItemArticleBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArticleActivity : BaseActivity<ActivityArticleBinding, ArticleViewModel>(R.layout.activity_article) {

    private val articles = ArrayList<Article?>()
    private lateinit var scrollListener: EndlessScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.rvArticle.adapter = CoreListAdapter<ItemArticleBinding, Article>(R.layout.item_article)
                .initItem(articles) { position, data ->
                    tos(data?.title ?: return@initItem)
                }
        scrollListener = binding.rvArticle.initLoadMore { page ->
            getData(page + 1)
            binding.rvArticle.adapter?.get()?.addNull()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.apiResponse.collect {
                        when (it.status) {
                            ApiStatus.SUCCESS -> {
                                val data = it.dataAs<DataObserver<Article>>()
                                val datas = data?.datas

                                binding.rvArticle.adapter?.get()?.removeNull()

                                if (data?.page == 1) {
                                    articles.clear()
                                    binding.rvArticle.adapter?.notifyDataSetChanged()
                                    scrollListener.resetState()
                                }

                                if (datas?.isNotEmpty() == true) {

                                    if (binding.rvArticle.adapter?.itemCount == 0) {
                                        articles.addAll(datas)
                                        binding.rvArticle.adapter?.notifyItemInserted(0)
                                    } else {
                                        articles.addAll(datas)
                                        binding.rvArticle.adapter?.notifyItemInserted(
                                            (binding.rvArticle.adapter?.itemCount ?: 1) - 1
                                        )
                                    }
                                }
                            }
                            else -> {
                            }
                        }
                    }
                }
            }
        }
        getData()
        }
    private fun getData(page: Int = 1) {
        viewModel.listArticle(page)
    }
}