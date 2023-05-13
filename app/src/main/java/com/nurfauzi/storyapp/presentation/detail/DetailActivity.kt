package com.nurfauzi.storyapp.presentation.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.nurfauzi.storyapp.databinding.ActivityDetailBinding
import com.nurfauzi.storyapp.domain.Story

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailStory = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_DETAIL, Story::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DETAIL)
        }

        if (detailStory != null) {
            setDetailStory(detailStory)
        }
    }

    private fun setDetailStory(detailStory: Story) {
        with(binding) {
            tvDetailName.text = detailStory.name
            tvDateCreated.text = detailStory.createdAt
            tvDetailDescription.text = detailStory.description
        }
        Glide.with(this)
            .load(detailStory.photoUrl)
            .into(binding.ivDetailPhoto)
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }
}