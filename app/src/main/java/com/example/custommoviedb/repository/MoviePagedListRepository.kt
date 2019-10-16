package com.example.custommoviedb.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.custommoviedb.models.MovieResponse.Movie
import com.example.custommoviedb.network.NetworkState
import com.example.custommoviedb.network.POST_PER_PAGE
import kotlinx.coroutines.CoroutineScope

class MoviePagedListRepository {
    
    private lateinit var moviePagedList : LiveData<PagedList<Movie>>
    private lateinit var moviesDataSourceFactory : MovieDataSourceFactory
    private val config = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(POST_PER_PAGE)
        .build()
    
    fun fetchLiveMoviePagedList(scope : CoroutineScope) : LiveData<PagedList<Movie>> {
        
        moviesDataSourceFactory = MovieDataSourceFactory(scope)
        
        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory , config).build()
        
        Log.d("ddd*** moviePagedList" , moviePagedList.toString())
        
        return moviePagedList
    }
    
    fun getNetworkState() : LiveData<NetworkState> {
        return Transformations.switchMap<MovieDataSource , NetworkState>(
                        moviesDataSourceFactory.moviesLiveDataSource , MovieDataSource::networkState
                                                                        )
    }
}