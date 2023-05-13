package com.nurfauzi.storyapp.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nurfauzi.storyapp.R
import com.nurfauzi.storyapp.databinding.ItemStoryBinding
import com.nurfauzi.storyapp.domain.Story
import com.nurfauzi.storyapp.presentation.detail.DetailActivity

class StoryPagingAdapter : PagingDataAdapter<Story,StoryPagingAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_story, viewGroup, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val data = getItem(position)
        if(data != null){
            viewHolder.bind(data)

            viewHolder.itemView.setOnClickListener {
                val dataDetailStory = Story(
                        data.id,
                        data.name,
                        data.description,
                        data.photoUrl,
                        data.createdAt,
                        data.lat,
                        data.lon
                    )
                    val intentDetail = Intent(viewHolder.itemView.context, DetailActivity::class.java)
                    intentDetail.putExtra(DetailActivity.EXTRA_DETAIL, dataDetailStory)
                    viewHolder.itemView.context.startActivity(intentDetail)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val storyBinding = ItemStoryBinding.bind(itemView)
        fun bind(data: Story) {
            with(storyBinding) {
                tvUsername.text = data.name
                tvDateCreated.text = data.createdAt
                Glide.with(itemView.context)
                    .load(data.photoUrl)
                    .into(ivStory)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
