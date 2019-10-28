package com.example.custommoviedb.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.custommoviedb.R
import com.example.custommoviedb.models.MovieResponse.Movie
import com.example.custommoviedb.network.NetworkState
import com.example.custommoviedb.network.POSTER_BASE_URL
import kotlinx.android.synthetic.main.movie_list_item.view.*
import kotlinx.android.synthetic.main.net_state_item.view.*

class MoviesPagedListAdapter(private val context : Context) : PagedListAdapter<Movie , RecyclerView.ViewHolder>(MovieDiffCallback()) {
    
    
    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2
    
    private var networkState : NetworkState? = null
    
    override fun onCreateViewHolder(parent : ViewGroup , viewType : Int) : ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view : View
        
        return if (viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.movie_list_item , parent , false)
            MovieItemViewHolder(view)
        }
        else {
            view = layoutInflater.inflate(R.layout.net_state_item , parent , false)
            NetworkStateItemViewHolder(view)
            
        }
    }
    
    override fun onBindViewHolder(holder : ViewHolder , position : Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position) , context)
        }
        else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }
    
    private fun hasExtraRow() : Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }
    
    override fun getItemCount() : Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }
    
    override fun getItemViewType(position : Int) : Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        }
        else {
            MOVIE_VIEW_TYPE
        }
    }
    
    fun setNetworkState(newNetworkState : NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            }
            else {
                notifyItemInserted(super.getItemCount())
            }
        }
        else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }
    
    class MovieItemViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(movie : Movie? , context : Context) {
            
            
            itemView.cv_movie_title.text = movie?.title
            itemView.cv_movie_release_date.text = movie?.releaseDate
            
            val moviePosterUrl = POSTER_BASE_URL + movie?.posterPath
            
            // set anim for movie_poster
            itemView.cv_iv_movie_poster.animation = AnimationUtils.loadAnimation(context , R.anim.fade_transition_animation)
            // set anim for whole card view
            itemView.card_view.animation = AnimationUtils.loadAnimation(context , R.anim.fade_transition_animation)
            
            itemView.movie_item_container
            Glide.with(itemView.context)
                .load(moviePosterUrl)
                .fallback(R.drawable.poster_placeholder)
                .into(itemView.cv_iv_movie_poster)
            
            itemView.setOnClickListener {
                Toast.makeText(context , "movie with id ${movie?.id} is clicked!" , Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    class NetworkStateItemViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(networkState : NetworkState?) {
            
            if (networkState != null && networkState == NetworkState.LOADING) {
                itemView.progress_bar_item.visibility = View.VISIBLE
            }
            else {
                itemView.progress_bar_item.visibility = View.GONE
            }
            
            if (networkState != null && networkState == NetworkState.ERROR) {
                itemView.error_msg_item.visibility = View.VISIBLE
                itemView.error_msg_item.text = networkState.msg
            }
            else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                itemView.error_msg_item.visibility = View.VISIBLE
                itemView.error_msg_item.text = networkState.msg
            }
            else {
                itemView.error_msg_item.visibility = View.GONE
            }
        }
    }
    
    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem : Movie , newItem : Movie) : Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem : Movie , newItem : Movie) : Boolean {
            return oldItem == newItem
        }
    }
    
}