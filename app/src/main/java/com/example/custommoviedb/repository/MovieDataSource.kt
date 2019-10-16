package com.example.custommoviedb.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.custommoviedb.models.MovieResponse.Movie
import com.example.custommoviedb.network.FIRST_PAGE
import com.example.custommoviedb.network.MovieNetwork
import com.example.custommoviedb.network.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDataSource(private val scope : CoroutineScope) : PageKeyedDataSource<Int , Movie>() {
    
    private var page = FIRST_PAGE
    private var lastPage : Int = 1
    
    private val _networkState = MutableLiveData<NetworkState>()
    val networkState : LiveData<NetworkState>
        get() = _networkState
    
    override fun loadInitial(params : LoadInitialParams<Int> , callback : LoadInitialCallback<Int , Movie>) {
        
        _networkState.postValue(NetworkState.LOADING)
        
        scope.launch(Dispatchers.IO) {
            
            try {
                
                val response = MovieNetwork.movieNetService.getPopularMovieAsync(497).await()
                lastPage = response.totalPages
                Log.d("dddd**** loadInit" , response.toString())
                
                when {
                    
                    !response.movieList.isNullOrEmpty() -> {
                        
                        val movieList = response.movieList
                        callback.onResult(movieList , null , response.page)
                        _networkState.postValue(NetworkState.LOADED)
                        
                    }
                    
                }
                
            }
            catch (ex : Exception) {
                ex.printStackTrace()
                Log.d("dddd**** ---- loadInit" , ex.toString())
                _networkState.postValue(NetworkState.ERROR)
                
            }
            
        }
    }
    
    override fun loadAfter(params : LoadParams<Int> , callback : LoadCallback<Int , Movie>) {
        
        _networkState.postValue(NetworkState.LOADING)
        
        scope.launch(Dispatchers.IO) {
            
            try {
                
                if (params.key <= lastPage) {
                    
                    Log.d("dddd**** loadAfter" , "BeforeNetCall")
                    val response = MovieNetwork.movieNetService.getPopularMovieAsync(params.key).await()
                    Log.d("dddd**** loadAfter" , response.toString())
                    
                    
                    when {
                        
                        !response.movieList.isNullOrEmpty() -> {
                            
                            val movieList = response.movieList
                            callback.onResult(movieList , params.key + 1)
                            _networkState.postValue(NetworkState.LOADED)
                            
                        }
                    }
                }
                else {
                    _networkState.postValue(NetworkState.ENDOFLIST)
                    
                }
                
            }
            catch (ex : Exception) {
                ex.printStackTrace()
                Log.d("dddd**** ---- loadAfter" , ex.toString())
                _networkState.postValue(NetworkState.ERROR)
                
            }
            
        }
    }
    
    override fun loadBefore(params : LoadParams<Int> , callback : LoadCallback<Int , Movie>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    
}