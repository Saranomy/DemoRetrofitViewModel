package com.saranomy.demoretrofitviewmodel.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val baseUrl = "https://reqres.in/api/"
    private var retrofit: Retrofit? = null
    fun getClient() : Retrofit {
        if(retrofit == null)
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit!!
    }
}