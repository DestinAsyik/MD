package com.id.destinasyik.data.mock

import com.id.destinasyik.R
import java.util.UUID
import kotlin.random.Random

data class LikedPlace(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val location: String,
    val isJoined: Boolean,
    val imageResource: Int? = null,
    val joinedDate: Long = System.currentTimeMillis(),
    val totalJoined: Int = Random.nextInt(50, 500)
) {
    companion object {
        fun createSampleList(): List<LikedPlace> = listOf(
            LikedPlace(
                name = "Mount Albrus",
                location = "Italy",
                isJoined = true,
                imageResource = R.drawable.baseline_image_24
            ),
            LikedPlace(
                name = "Burj Khalifa",
                location = "UAE",
                isJoined = false,
                imageResource = R.drawable.baseline_image_24
            ),
            LikedPlace(
                name = "Eiffel Tower",
                location = "France",
                isJoined = true,
                imageResource = R.drawable.baseline_image_24
            ),
            LikedPlace(
                name = "Taj Mahal",
                location = "India",
                isJoined = false,
                imageResource = R.drawable.baseline_image_24
            ),
            LikedPlace(
                name = "Great Wall",
                location = "China",
                isJoined = true,
                imageResource = R.drawable.baseline_image_24
            )
        )
    }

    fun getJoinedTimeAgo(): String {
        val diffMillis = System.currentTimeMillis() - joinedDate
        val diffHours = diffMillis / (1000 * 60 * 60)
        val diffDays = diffHours / 24

        return when {
            diffDays > 0 -> "$diffDays days ago"
            diffHours > 0 -> "$diffHours hours ago"
            else -> "Just now"
        }
    }

    fun getJoinedCountText(): String = "$totalJoined people joined"
}