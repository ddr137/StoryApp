package com.ddr1.storyapp.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "story")
data class StoriesEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val lat: Double?,
    val lon: Double?,
    val photoUrl: String,
    val createdAt: String,
): Parcelable
