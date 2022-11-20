package com.ddr1.storyapp.data.remote.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class StoriesResponse(
    val error: Boolean,
    val listStory: List<Story>,
    val message: String
) {
    @Parcelize
    data class Story(
        val id: String,
        val name: String,
        val description: String,
        val lat: Double?,
        val lon: Double?,
        val photoUrl: String,
        val createdAt: String
    ) : Parcelable
}