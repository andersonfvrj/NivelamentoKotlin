package com.example.gestaocontatos.api.service

import com.example.gestaocontatos.api.model.ViaCepModel
import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepService {

    @GET("{cep}/json/")
    suspend fun getEnderecoByCep(@Path("cep") cep: String): ViaCepModel?

}