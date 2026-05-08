package com.example.gestaocontatos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gestaocontatos.data.database.AppDatabase
import com.example.gestaocontatos.data.entity.Contact
import com.example.gestaocontatos.data.service.ContactService
import com.example.gestaocontatos.ui.screens.ContactListScreen
import com.example.gestaocontatos.ui.screens.ContactScreen
import com.example.gestaocontatos.ui.theme.GestaoContatosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val db = AppDatabase.getDatabase(this)
        val dao = db.contactDao()
        val service = ContactService(dao)

        enableEdgeToEdge()
        setContent {
            GestaoContatosTheme {
                GerenciadorNavegacao(service)
            }
        }
    }
}

@Composable
fun GerenciadorNavegacao(service: ContactService) {
    val navController = rememberNavController()
    var selectedContact by remember { mutableStateOf<Contact?>(null) }
    var isReadOnly by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { navController.navigate("listarContatos") },
                    icon = { 
                        Icon(
                            Icons.Default.AccountCircle, 
                            contentDescription = "Início"
                        )
                    },
                    label = { Text("Contatos") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { 
                        selectedContact = null
                        isReadOnly = false
                        navController.navigate("manipularContato") 
                    },
                    icon = { 
                        Icon(
                            Icons.Default.AddCircle, 
                            contentDescription = "Cadastrar"
                        ) 
                    },
                    label = { Text("Novo") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "listarContatos",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("listarContatos") {
                ContactListScreen(
                    service = service,
                    onContactClick = { contact ->
                        selectedContact = contact
                        isReadOnly = true
                        navController.navigate("manipularContato")
                    },
                    onEdit = { contact ->
                        selectedContact = contact
                        isReadOnly = false
                        navController.navigate("manipularContato")
                    }
                )
            }

            composable("manipularContato") {
                ContactScreen(
                    service = service,
                    contact = selectedContact,
                    isReadOnly = isReadOnly,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
