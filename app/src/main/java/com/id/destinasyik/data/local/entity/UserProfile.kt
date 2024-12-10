package com.id.destinasyik.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val userId: Int,
    val username: String,
    val email: String,
    // Add other fields as necessary
)

