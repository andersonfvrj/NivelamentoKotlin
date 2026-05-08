package com.example.gestaocontatos.data.service

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.gestaocontatos.data.dao.ContactDao
import com.example.gestaocontatos.data.entity.Contact

class ContactService(private val contactDao: ContactDao) {

    var contacts by mutableStateOf<List<Contact>>(emptyList())
        private set //Faz com que só possa ser alterada pelo service

    suspend fun carregarContatos() {
        contacts = contactDao.getAll()
    }

    suspend fun getAll(): List<Contact> {
        return contactDao.getAll()
    }

    suspend fun insert(contact: Contact) {
        contactDao.insert(contact)
        carregarContatos()
    }

    suspend fun update(contact: Contact) {
        contactDao.update(contact)
        carregarContatos()
    }

    suspend fun delete(contact: Contact) {
        contactDao.delete(contact)
        carregarContatos()
    }
}