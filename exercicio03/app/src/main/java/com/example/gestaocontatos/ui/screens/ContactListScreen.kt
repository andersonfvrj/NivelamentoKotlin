package com.example.gestaocontatos.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.gestaocontatos.data.entity.Contact
import com.example.gestaocontatos.data.service.ContactService
import com.example.gestaocontatos.ui.components.ContactFinder
import com.example.gestaocontatos.ui.components.ContactItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(
    service: ContactService,
    onContactClick: (Contact) -> Unit,
    onEdit: (Contact) -> Unit
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        service.carregarContatos()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Gestor de Contatos") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            val filteredContacts = ContactFinder(contacts = service.contacts)

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(filteredContacts) { contact ->
                    ContactItem(
                        contact = contact,
                        onClick = { onContactClick(contact) },
                        onEdit = { onEdit(contact) },
                        onDelete = {
                            scope.launch {
                                service.delete(contact)
                            }
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
