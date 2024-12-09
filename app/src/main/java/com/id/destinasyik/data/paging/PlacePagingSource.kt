package com.id.destinasyik.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.id.destinasyik.data.remote.response.ReccomPlace
import com.id.destinasyik.data.remote.response.ResultsItem
import com.id.destinasyik.data.remote.retrofit.ApiConfig

class PlacePagingSource(private val keyword: String, private val authToken: String): PagingSource<Int, ReccomPlace>() {
    override fun getRefreshKey(state: PagingState<Int, ReccomPlace>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ReccomPlace> {
        val page = params.key ?: 1 // Halaman pertama
        return try {
            val response = ApiConfig.getApiService().searchDestinations(authToken, keyword ,page)
            Log.d("PlacePagingSource", "Response for page $page: $response")
            val data = response.data?.results
            val totalPages = response.data?.totalPages ?: 1

            LoadResult.Page(
                data = data?.filterNotNull() ?: emptyList(), // Pastikan data tidak null
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (page < totalPages) page + 1 else null
            )
        } catch (e: Exception) {
            Log.d("LOAD RESULT","$e")
            LoadResult.Error(e)
        }
    }

}