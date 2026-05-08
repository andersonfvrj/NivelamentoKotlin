package com.example.gestaocontatos.api.repository

import com.example.gestaocontatos.api.model.ViaCepModel
import com.example.gestaocontatos.api.network.ViaCepRetrofitClient

class ViaCepRepository {

    suspend fun getEnderecoByCep(cep: String): ViaCepModel? {
        return ViaCepRetrofitClient.api.getEnderecoByCep(cep)
    }
}