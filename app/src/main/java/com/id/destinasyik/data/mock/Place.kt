package com.id.destinasyik.data.mock

import com.id.destinasyik.R
import java.util.UUID

data class Place(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val location: String,
    val price: String,
    val duration: String,
    val imageResource: Int,
    val rating: Float = 0f,
    val description: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null
) {
    companion object {
        fun createSampleList(): List<Place> = listOf(
            Place(
                name = "City Rome",
                location = "Italy",
                price = "$750",
                duration = "5 Days",
                imageResource = R.drawable.baseline_image_24,
                rating = 4.5f,
                description = "Experience the eternal city's ancient ruins, art, and culture."
            ),
            Place(
                name = "Venice",
                location = "Italy",
                price = "$850",
                duration = "4 Days",
                imageResource = R.drawable.baseline_image_24,
                rating = 4.7f,
                description = "Explore the romantic canals and historic architecture."
            ),
            Place(
                name = "Paris",
                location = "France",
                price = "$900",
                duration = "6 Days",
                imageResource = R.drawable.baseline_image_24,
                rating = 4.6f,
                description = "Discover the city of love and its iconic landmarks."
            ),
            Place(
                name = "New York",
                location = "USA",
                price = "$950",
                duration = "7 Days",
                imageResource = R.drawable.baseline_image_24,
                rating = 4.8f,
                description = "Visit the city that never sleeps and its diverse neighborhoods."
            ),
            Place(
                name = "Tokyo",
                location = "Japan",
                price = "$1000",
                duration = "8 Days",
                imageResource = R.drawable.baseline_image_24,
                rating = 4.9f,
                description = "Experience the bustling metropolis and its unique culture."
            )
        )
    }

    fun toDisplayPrice(): String = price.takeIf { it.startsWith("$") } ?: "$$price"

    fun getFormattedDuration(): String = duration.takeIf { it.contains("Days") } ?: "$duration Days"
}