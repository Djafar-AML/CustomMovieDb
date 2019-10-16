package com.example.custommoviedb.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import com.example.custommoviedb.models.MovieResponse.Movie
import com.example.custommoviedb.network.NetworkState
import com.example.custommoviedb.repository.MoviePagedListRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class MainActivityViewModel(private val moviePagedListRepository : MoviePagedListRepository) : ViewModel() {
    
    private val viewModelJob = SupervisorJob()
    
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    
    
    val moviePagedList : LiveData<PagedList<Movie>> by lazy {
        moviePagedListRepository.fetchLiveMoviePagedList(viewModelScope)
    }
    
    val networkState : LiveData<NetworkState> by lazy {
        moviePagedListRepository.getNetworkState()
    }
    
    fun moviesListIsEmpty() = moviePagedList.value.isNullOrEmpty()
    
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
    
    class MainActivityViewModelFactory(private val moviePagedListRepository : MoviePagedListRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass : Class<T>) : T {
            if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(moviePagedListRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewModel")
        }
    }
    
}