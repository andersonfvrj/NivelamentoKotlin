package com.example.gestaocontatos.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gestaocontatos.data.entity.Contact

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: Contact)

    @Update
    suspend fun update(contact: Contact)

    @Query("SELECT * FROM contacts")
    fun getAll(): List<Contact>

    @Query("SELECT * FROM contacts WHERE name LIKE '%' || :name || '%'")
    fun getByName(name: String): List<Contact>

    @Delete
    suspend fun delete(contact: Contact)

}