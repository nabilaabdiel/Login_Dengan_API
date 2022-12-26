package com.example.coreandroid.ui.detail

import android.os.Bundle
import com.crocodic.core.base.activity.NoViewModelActivity
import com.example.coreandroid.R
import com.example.coreandroid.data.constant.Const
import com.example.coreandroid.data.model.Article
import com.example.coreandroid.databinding.ActivityArticleDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleDetailActivity : NoViewModelActivity<ActivityArticleDetailBinding>(R.layout.activity_article_detail) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val article: Article? = intent.getParcelableExtra(Const.BUNDLE.ARTICLE)

        binding.data = article
    }
}