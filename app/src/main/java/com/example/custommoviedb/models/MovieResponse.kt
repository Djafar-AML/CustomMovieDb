package com.example.custommoviedb.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class MovieResponse(
                val page : Int ,
                @Json(name = "results")
                val movieList : List<Movie> ,
                @Json(name = "total_pages")
                val totalPages : Int ,
                @Json(name = "total_results")
                val totalResults : Int
                        ) {
    
    
    @JsonClass(generateAdapter = true)
    data class Movie(
                    val id : Int ,
                    @Json(name = "poster_path")
                    val posterPath : String? ,
                    @Json(name = "release_date")
                    val releaseDate : String? ,
                    val title : String?
                    )
}