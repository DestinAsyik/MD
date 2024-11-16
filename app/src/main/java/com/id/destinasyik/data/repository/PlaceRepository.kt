package com.id.destinasyik.data.repository

import com.id.destinasyik.data.local.mock.LikedPlace
import com.id.destinasyik.data.local.mock.Place

class PlaceRepository {
    fun getRecommendedPlaces(): List<Place> = Place.createSampleList()

    fun getLikedPlaces(): List<LikedPlace> = LikedPlace.createSampleList()

    fun searchPlaces(query: String): List<Place> {
        return Place.createSampleList().filter { place ->
            place.name.contains(query, ignoreCase = true) ||
                    place.location.contains(query, ignoreCase = true) ||
                    place.description.contains(query, ignoreCase = true)
        }
    }
}
