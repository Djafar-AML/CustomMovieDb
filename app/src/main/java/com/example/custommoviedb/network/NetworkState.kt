package com.example.custommoviedb.network

import com.example.custommoviedb.network.Status.*

enum class Status {
    RUNNING ,
    SUCCESS ,
    FAILED
}

class NetworkState(private val status : Status ,  val msg : String) {
    
    companion object {
        val LOADED : NetworkState = NetworkState(SUCCESS , "Success")
        val LOADING : NetworkState = NetworkState(RUNNING , "Running")
        val ERROR : NetworkState = NetworkState(FAILED , "Something went wrong")
        val ENDOFLIST : NetworkState = NetworkState(FAILED , "You have reached the end")
    }
}