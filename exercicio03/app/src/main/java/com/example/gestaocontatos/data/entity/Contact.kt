package com.example.gestaocontatos.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val email: String,
    val phone: String,
    val dateOfBirth: String,
    val cep: String,
    val street: String,
    val number: String,
    val neighborhood: String,
    val city: String,
    val state: String
)
