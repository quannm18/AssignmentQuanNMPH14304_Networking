package com.quannm18.assignmentquannmph14304_networking.network

import com.google.gson.GsonBuilder
import com.quannm18.assignmentquannmph14304_networking.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

object RetrofitInstance {
    private val gSon = GsonBuilder()
        .setLenient()
        .create()

    private val mRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    val api : TodoInterface by lazy {
        mRetrofit.create(TodoInterface::class.java)
    }
}