package com.ddr1.storyapp.ui.main

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ddr1.storyapp.data.local.entity.StoriesEntity
import com.ddr1.storyapp.databinding.ItemStoryBinding
import com.ddr1.storyapp.utils.setImageFromUrl
import com.ddr1.storyapp.utils.setLocalDateFormat

class StoriesAdapter(private val listener: OnAdapterListener) :
    PagingDataAdapter<StoriesEntity, StoriesAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
        }
    }

    class ViewHolder(
        private val binding: ItemStoryBinding,
        private val listener: OnAdapterListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: StoriesEntity) {
            binding.apply {
                tvName.text = story.name
                tvDesc.text = story.description
                tvDate.setLocalDateFormat(story.createdAt)
                imgStory.setImageFromUrl(story.photoUrl)

                itemView.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(tvName, "name"),
                            Pair(tvDesc, "description"),
                            Pair(tvDate, "date"),
                            Pair(imgStory, "image"),
                        )
                    listener.onClick(story, optionsCompat.toBundle())
                }
            }
        }
    }

    interface OnAdapterListener {
        fun onClick(story: StoriesEntity, toBundle: Bundle?)
    }

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<StoriesEntity>() {
            override fun areItemsTheSame(
                oldItem: StoriesEntity,
                newItem: StoriesEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: StoriesEntity,
                newItem: StoriesEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}