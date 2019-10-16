package com.example.custommoviedb.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.custommoviedb.R.layout
import com.example.custommoviedb.network.NetworkState
import com.example.custommoviedb.repository.MoviePagedListRepository
import com.example.custommoviedb.ui.MainActivityViewModel.MainActivityViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    
    private lateinit var movieRepository : MoviePagedListRepository
    private val viewModel : MainActivityViewModel by lazy {
        ViewModelProvider(this , MainActivityViewModelFactory(movieRepository)).get(MainActivityViewModel::class.java)
    }
    
    
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        movieRepository = MoviePagedListRepository()
        
        val movieAdapter = MoviesPagedListAdapter(this)
        
        val gridLayoutManager = GridLayoutManager(this , 3)
        
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position : Int) : Int {
                val viewType = movieAdapter.getItemViewType(position)
                return if (viewType == movieAdapter.MOVIE_VIEW_TYPE) 1
                else 3
            }
        }
        
        rec_view_movie_list.layoutManager = gridLayoutManager
        rec_view_movie_list.setHasFixedSize(true)
        rec_view_movie_list.adapter = movieAdapter
        
        viewModel.moviePagedList.observe(this , Observer {
            movieAdapter.submitList(it)
        })
        
        viewModel.networkState.observe(this , Observer {
            progress_bar_popular.visibility = if (viewModel.moviesListIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility = if (viewModel.moviesListIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE
            
            if (!viewModel.moviesListIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })
    }
}
