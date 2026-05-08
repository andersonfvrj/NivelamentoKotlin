package com.example.gestaocontatos.api.network

import com.example.gestaocontatos.api.service.ViaCepService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ViaCepRetrofitClient {

    private const val BASE_URL = "https://viacep.com.br/ws/"

    val api: ViaCepService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ViaCepService::class.java)
    }
}