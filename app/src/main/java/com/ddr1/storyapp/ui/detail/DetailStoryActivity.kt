package com.ddr1.storyapp.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ddr1.storyapp.R
import com.ddr1.storyapp.data.local.entity.StoriesEntity
import com.ddr1.storyapp.databinding.ActivityDetailStoryBinding
import com.ddr1.storyapp.utils.AppConstants.EXTRA_DETAIL
import com.ddr1.storyapp.utils.setImageFromUrl
import com.ddr1.storyapp.utils.setLocalDateFormat

class DetailStoryActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailStoryBinding.inflate(layoutInflater) }
    private val detail by lazy {
        intent.getParcelableExtra<StoriesEntity>(EXTRA_DETAIL)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setToolbar()
        setData(detail)
    }

    private fun setToolbar() {
        setSupportActionBar(binding.include.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.detail_story)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setData(story: StoriesEntity?) {
        if (story != null) {
            binding.apply {
                tvName.text = story.name
                tvDesc.text = story.description
                tvDate.setLocalDateFormat(story.createdAt)
                imgStory.setImageFromUrl(story.photoUrl)
            }
        }
    }
}