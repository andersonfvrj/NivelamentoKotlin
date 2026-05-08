package com.example.gestaocontatos.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.gestaocontatos.api.network.ViaCepRetrofitClient
import com.example.gestaocontatos.data.entity.Contact

@Composable
fun ContactForm(
    contact: Contact? = null,
    isReadOnly: Boolean = false,
    onSave: (Contact) -> Unit,
    onCancel: () -> Unit
) {
    var name by remember { mutableStateOf(contact?.name ?: "") }
    var email by remember { mutableStateOf(contact?.email ?: "") }
    
    var phoneDigits by remember { 
        mutableStateOf(contact?.phone?.filter { it.isDigit() } ?: "") 
    }
    var dateDigits by remember { 
        mutableStateOf(contact?.dateOfBirth?.filter { it.isDigit() } ?: "") 
    }
    
    var cepDigits by remember { 
        mutableStateOf(contact?.cep?.filter { it.isDigit() } ?: "") 
    }
    var street by remember { mutableStateOf(contact?.street ?: "") }
    var number by remember { mutableStateOf(contact?.number ?: "") }
    var neighborhood by remember { mutableStateOf(contact?.neighborhood ?: "") }
    var city by remember { mutableStateOf(contact?.city ?: "") }
    var state by remember { mutableStateOf(contact?.state ?: "") }

    var cepError by remember { mutableStateOf<String?>(null) }
    var isLoadingCep by remember { mutableStateOf(false) }

    val isEmailValid = remember(email) {
        email.isEmpty() || android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    val isFormValid = isEmailValid && name.trim().isNotEmpty() && phoneDigits.isNotEmpty() && cepDigits.length == 8

    LaunchedEffect(cepDigits) {
        if (cepDigits.length == 8) {
            isLoadingCep = true
            cepError = null
            try {
                val response = ViaCepRetrofitClient.api.getEnderecoByCep(cepDigits)
                if (response != null) {
                    if (response.erro == true) {
                        cepError = "CEP não encontrado"
                    } else {
                        street = response.logradouro ?: ""
                        neighborhood = response.bairro ?: ""
                        city = response.localidade ?: ""
                        state = response.uf ?: ""
                    }
                } else {
                    cepError = "CEP não encontrado"
                }
            } catch (e: Exception) {
                cepError = "Erro ao buscar CEP"
            } finally {
                isLoadingCep = false
            }
        } else {
            cepError = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome *") },
            isError = name.trim().isEmpty(),
            readOnly = isReadOnly,
            modifier = Modifier.fillMaxWidth()
        )
        
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            isError = !isEmailValid,
            readOnly = isReadOnly,
            supportingText = {
                if (!isEmailValid) {
                    Text("Formato de e-mail inválido")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            value = phoneDigits,
            onValueChange = { input ->
                val digits = input.filter { it.isDigit() }
                if (digits.length <= 11) {
                    phoneDigits = digits
                }
            },
            label = { Text("Telefone *") },
            isError = phoneDigits.isEmpty(),
            readOnly = isReadOnly,
            placeholder = { Text("(99)99999-9999") },
            visualTransformation = PhoneVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = dateDigits,
            onValueChange = { input ->
                val digits = input.filter { it.isDigit() }
                if (digits.length <= 8) {
                    dateDigits = digits
                }
            },
            label = { Text("Data de Nascimento") },
            placeholder = { Text("DD/MM/YYYY") },
            readOnly = isReadOnly,
            visualTransformation = DateVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = cepDigits,
            onValueChange = { input ->
                val digits = input.filter { it.isDigit() }
                if (digits.length <= 8) {
                    cepDigits = digits
                }
            },
            label = { Text("CEP *") },
            isError = cepError != null || (cepDigits.isNotEmpty() && cepDigits.length < 8),
            readOnly = isReadOnly,
            placeholder = { Text("99999-999") },
            visualTransformation = CepVisualTransformation(),
            supportingText = {
                if (cepError != null) {
                    Text(text = cepError!!, color = Color.Red)
                } else if (cepDigits.isNotEmpty() && cepDigits.length < 8) {
                    Text("O CEP deve ter 8 dígitos")
                }
            },
            trailingIcon = {
                if (isLoadingCep) {
                    CircularProgressIndicator(modifier = Modifier.padding(8.dp), strokeWidth = 2.dp)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(value = street, onValueChange = { street = it }, readOnly = isReadOnly, label = { Text("Rua") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = number, onValueChange = { number = it }, readOnly = isReadOnly, label = { Text("Número") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = neighborhood, onValueChange = { neighborhood = it }, readOnly = isReadOnly, label = { Text("Bairro") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = city, onValueChange = { city = it }, readOnly = isReadOnly, label = { Text("Cidade") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = state, onValueChange = { state = it }, readOnly = isReadOnly, label = { Text("Estado") }, modifier = Modifier.fillMaxWidth())

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            if (isReadOnly) {
                Button(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Voltar")
                }
            } else {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Button(
                    onClick = {
                        if (isFormValid) {
                            val newContact = Contact(
                                id = contact?.id ?: 0,
                                name = name,
                                email = email,
                                phone = formatPhone(phoneDigits),
                                dateOfBirth = formatDate(dateDigits),
                                cep = formatCep(cepDigits),
                                street = street,
                                number = number,
                                neighborhood = neighborhood,
                                city = city,
                                state = state
                            )
                            onSave(newContact)
                        }
                    },
                    enabled = isFormValid,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (contact == null) "Cadastrar" else "Salvar")
                }
            }
        }
    }
}

class CepVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text
        val out = buildString {
            append(digits.take(5))
            if (digits.length >= 5) append("-")
            append(digits.drop(5).take(3))
        }

        val cepOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 5) return offset
                return offset + 1
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 5) return offset
                return offset - 1
            }
        }

        return TransformedText(AnnotatedString(out), cepOffsetTranslator)
    }
}

class PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text
        val isMobile = digits.length == 11
        val out = buildString {
            if (digits.isNotEmpty()) append("(")
            append(digits.take(2))
            if (digits.length >= 2) append(")")
            
            if (isMobile) {
                append(digits.drop(2).take(5))
                if (digits.length >= 7) append("-")
                append(digits.drop(7).take(4))
            } else {
                append(digits.drop(2).take(4))
                if (digits.length >= 6) append("-")
                append(digits.drop(6).take(4))
            }
        }

        val phoneOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return 0
                var transformed = offset + 1 
                if (offset >= 2) transformed += 1 
                if (offset >= 6) transformed += 1 
                return transformed.coerceAtMost(out.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                var original = offset
                if (offset > 0) original -= 1 
                if (offset > 3) original -= 1 
                if (offset > 8) original -= 1 
                return original.coerceIn(0, text.length)
            }
        }

        return TransformedText(AnnotatedString(out), phoneOffsetTranslator)
    }
}

class DateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text
        val out = buildString {
            append(digits.take(2))
            if (digits.length >= 2) append("/")
            append(digits.drop(2).take(2))
            if (digits.length >= 4) append("/")
            append(digits.drop(4).take(4))
        }

        val dateOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var transformed = offset
                if (offset >= 2) transformed += 1 
                if (offset >= 4) transformed += 1 
                return transformed.coerceAtMost(out.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                var original = offset
                if (offset > 2) original -= 1 
                if (offset > 5) original -= 1 
                return original.coerceIn(0, text.length)
            }
        }

        return TransformedText(AnnotatedString(out), dateOffsetTranslator)
    }
}

private fun formatPhone(digits: String): String {
    if (digits.isEmpty()) return ""
    return buildString {
        append("(")
        append(digits.take(2))
        append(")")
        if (digits.length == 11) {
            append(digits.drop(2).take(5))
            append("-")
            append(digits.drop(7))
        } else {
            append(digits.drop(2).take(4))
            if (digits.length >= 6) {
                append("-")
                append(digits.drop(6))
            }
        }
    }
}

private fun formatDate(digits: String): String {
    if (digits.isEmpty()) return ""
    return buildString {
        append(digits.take(2))
        if (digits.length >= 2) append("/")
        append(digits.drop(2).take(2))
        if (digits.length >= 4) append("/")
        append(digits.drop(4).take(4))
    }
}

private fun formatCep(digits: String): String {
    if (digits.length < 8) return digits
    return "${digits.take(5)}-${digits.drop(5)}"
}