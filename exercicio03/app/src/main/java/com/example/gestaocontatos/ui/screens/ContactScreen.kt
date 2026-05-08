package com.example.gestaocontatos.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.gestaocontatos.data.entity.Contact
import com.example.gestaocontatos.data.service.ContactService
import com.example.gestaocontatos.ui.components.ContactForm
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(
    service: ContactService,
    contact: Contact?,
    isReadOnly: Boolean = false,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    val title = when {
                        isReadOnly -> "Visualizar Contato"
                        contact == null -> "Novo Contato"
                        else -> "Editar Contato"
                    }
                    Text(title) 
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            ContactForm(
                contact = contact,
                isReadOnly = isReadOnly,
                onSave = { updatedContact ->
                    scope.launch {
                        if (contact == null) {
                            service.insert(updatedContact)
                        } else {
                            service.update(updatedContact)
                        }
                        onBack()
                    }
                },
                onCancel = onBack
            )
        }
    }
}
