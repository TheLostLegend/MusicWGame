package com.example.restapiidemo.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitClient private constructor() {
    private val myApi: ApiInterface
    private val myApi2: ApiInterface

    companion object {
        @get:Synchronized
        var instance: RetrofitClient? = null
            get() {
                if (field == null) {
                    field = RetrofitClient()
                }
                return field
            }
            private set
    }

    init {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://musicw-production.up.railway.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        myApi = retrofit.create(ApiInterface::class.java)
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create()
        val retrofit2: Retrofit = Retrofit.Builder().baseUrl("https://musicw-production.up.railway.app/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        myApi2 = retrofit2.create(ApiInterface::class.java)
    }

    fun getMyApi(): ApiInterface {
        return myApi
    }

    fun getMyApi2(): ApiInterface {
        return myApi2
    }
}