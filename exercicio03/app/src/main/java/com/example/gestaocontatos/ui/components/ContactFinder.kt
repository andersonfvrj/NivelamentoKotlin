package com.example.gestaocontatos.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestaocontatos.data.entity.Contact

@Composable
fun ContactFinder(contacts: List<Contact>): List<Contact> {
    var query by remember { mutableStateOf("") }

    Column {
        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Buscar contatos:") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = { Text("Insira o nome...") }
        )
    }

    return remember(query, contacts) {
        if (query.isEmpty()) {
            contacts
        } else {
            contacts.filter { it.name.contains(query, ignoreCase = true) }
        }
    }
}