package com.example.custommoviedb.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.custommoviedb.models.MovieResponse.Movie
import kotlinx.coroutines.CoroutineScope

class MovieDataSourceFactory(private val scope : CoroutineScope) : DataSource.Factory<Int , Movie>() {
    
    private val _moviesLiveDataSource = MutableLiveData<MovieDataSource>()
    val moviesLiveDataSource : LiveData<MovieDataSource>
        get() = _moviesLiveDataSource
    
    override fun create() : DataSource<Int , Movie> {
        
        val movieDataSource = MovieDataSource(scope)
        
        _moviesLiveDataSource.postValue(movieDataSource)
        
        return movieDataSource
        
    }
}