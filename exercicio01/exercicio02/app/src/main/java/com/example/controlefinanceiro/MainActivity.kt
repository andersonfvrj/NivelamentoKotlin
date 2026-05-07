package com.example.controlefinanceiro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.controlefinanceiro.ui.theme.ControleFinanceiroTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

val VerdeMoeda = Color(0xFF2E7D32)

// Função auxiliar para formatar a exibição (converte AAAA-MM-DD para DD-MM-AAAA)
fun formatarParaExibicao(dataIso: String): String {
    if (dataIso.isEmpty()) return ""
    val partes = dataIso.split("-")
    return if (partes.size == 3) "${partes[2]}-${partes[1]}-${partes[0]}" else dataIso
}

data class Transacao(
    val data: String,
    val descricao: String,
    val valor: Double
)

data class Desejo(
    val descricao: String,
    val valor: Double
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ControleFinanceiroTheme {
                GerenciadorNavegacao()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GerenciadorNavegacao() {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val listaTransacoes = remember {
        mutableStateListOf(
            Transacao("2026-03-01", "Salário Março", 2500.00),
            Transacao("2026-03-05", "Energia Elétrica", -150.00),
            Transacao("2026-03-10", "Aluguel", -450.00),
            Transacao("2026-03-12", "Venda de Bolo", 100.00),
            Transacao("2026-04-01", "Salário Abril", 2500.00),
            Transacao("2026-04-05", "Internet", -100.00)
        )
    }
    
    val listaDesejos = remember {
        mutableStateListOf(
            Desejo("Carro", 20000.00),
            Desejo("Casa", 300000.00),
            Desejo("Celular", 1500.00)
        )
    }

    val saldoAtual = listaTransacoes.sumOf { it.valor }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Controle Financeiro",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = VerdeMoeda
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = VerdeMoeda) {
                val itemColors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                    unselectedTextColor = Color.White.copy(alpha = 0.6f),
                    indicatorColor = Color.White.copy(alpha = 0.2f)
                )

                NavigationBarItem(
                    selected = currentRoute == "inicio",
                    onClick = { navController.navigate("inicio") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Início") },
                    label = { Text("Início") },
                    colors = itemColors
                )
                NavigationBarItem(
                    selected = currentRoute == "movimentacoes",
                    onClick = { navController.navigate("movimentacoes") },
                    icon = { Icon(Icons.Default.Menu, contentDescription = "Extrato") },
                    label = { Text("Extrato") },
                    colors = itemColors
                )
                NavigationBarItem(
                    selected = currentRoute == "registros",
                    onClick = { navController.navigate("registros") },
                    icon = { Icon(Icons.Default.Edit, contentDescription = "Registros") },
                    label = { Text("Registros") },
                    colors = itemColors
                )
                NavigationBarItem(
                    selected = currentRoute == "desejos",
                    onClick = { navController.navigate("desejos") },
                    icon = { Icon(Icons.Default.Star, contentDescription = "Desejos") },
                    label = { Text("Desejos") },
                    colors = itemColors
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "inicio",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("inicio") {
                Inicio(saldoAtual)
            }
            composable("movimentacoes") {
                Movimentacoes(listaTransacoes, saldoAtual)
            }
            composable("registros") {
                Registros(listaTransacoes, saldoAtual)
            }
            composable("desejos") {
                Desejos(listaDesejos, saldoAtual)
            }
        }
    }
}

@Composable
fun Inicio(saldo: Double) {
    val saldoFormatado = String.format(Locale.getDefault(), "%.2f", saldo)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fundo_inicio),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp, start = 20.dp),
            contentAlignment = Alignment.TopStart
        ) {
            ElevatedCard(
                modifier = Modifier.size(width = 200.dp, height = 150.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.dolares),
                            contentDescription = null,
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Saldo",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "R$ $saldoFormatado",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (saldo >= 0) VerdeMoeda else Color.Red,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Movimentacoes(lista: SnapshotStateList<Transacao>, saldo: Double) {
    var dataInicial by remember { mutableStateOf("") }
    var dataFinal by remember { mutableStateOf("") }

    var filtroDataInicial by remember { mutableStateOf("") }
    var filtroDataFinal by remember { mutableStateOf("") }

    val listaExibida by remember {
        derivedStateOf {
            val base = if (filtroDataInicial.isNotEmpty() && filtroDataFinal.isNotEmpty()) {
                lista.filter { it.data in filtroDataInicial..filtroDataFinal }
            } else {
                lista
            }
            base.sortedByDescending { it.data }
        }
    }

    var showDatePickerInicial by remember { mutableStateOf(false) }
    var showDatePickerFinal by remember { mutableStateOf(false) }

    val datePickerStateInicial = rememberDatePickerState()
    val datePickerStateFinal = rememberDatePickerState()

    fun Long?.toDateString(): String {
        return this?.let {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            sdf.format(Date(it))
        } ?: ""
    }

    if (showDatePickerInicial) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerInicial = false },
            confirmButton = {
                TextButton(onClick = {
                    dataInicial = datePickerStateInicial.selectedDateMillis.toDateString()
                    showDatePickerInicial = false
                }) { Text("Confirmar") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerInicial = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerStateInicial)
        }
    }

    if (showDatePickerFinal) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerFinal = false },
            confirmButton = {
                TextButton(onClick = {
                    dataFinal = datePickerStateFinal.selectedDateMillis.toDateString()
                    showDatePickerFinal = false
                }) { Text("Confirmar") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerFinal = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerStateFinal)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Exibição do Saldo no Topo da tela de Movimentações
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Saldo Atual",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "R$ ${String.format(Locale.getDefault(), "%.2f", saldo)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (saldo >= 0) VerdeMoeda else Color.Red
            )
        }

        HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp), thickness = 0.5.dp)

        Text(
            text = "Filtrar por período",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = formatarParaExibicao(dataInicial),
                    onValueChange = { },
                    label = { Text("Início") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            modifier = Modifier.clickable { showDatePickerInicial = true }
                        )
                    }
                )
                Box(modifier = Modifier
                    .matchParentSize()
                    .clickable { showDatePickerInicial = true })
            }

            Box(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = formatarParaExibicao(dataFinal),
                    onValueChange = { },
                    label = { Text("Fim") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            modifier = Modifier.clickable { showDatePickerFinal = true }
                        )
                    }
                )
                Box(modifier = Modifier
                    .matchParentSize()
                    .clickable { showDatePickerFinal = true })
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    filtroDataInicial = dataInicial
                    filtroDataFinal = dataFinal
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeMoeda)
            ) {
                Text("Filtrar", color = Color.White)
            }

            OutlinedButton(
                onClick = {
                    dataInicial = ""
                    dataFinal = ""
                    filtroDataInicial = ""
                    filtroDataFinal = ""
                },
                modifier = Modifier.weight(0.6f)
            ) {
                Text("Limpar", color = VerdeMoeda)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Histórico",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listaExibida) { transacao ->
                ItemTransacao(transacao, onRemove = { lista.remove(transacao) })
                HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
            }
        }
    }
}

@Composable
fun ItemTransacao(transacao: Transacao, onRemove: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onRemove) {
            Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = Color.Red)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transacao.descricao,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = formatarParaExibicao(transacao.data),
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Text(
            text = "R$ ${String.format(Locale.getDefault(), "%.2f", transacao.valor)}",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            color = if (transacao.valor >= 0) VerdeMoeda else Color.Red
        )
    }
}

@Composable
fun ItemDesejo(desejo: Desejo, saldo: Double, onRemove: () -> Unit) {
    val podeComprar = saldo >= desejo.valor
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onRemove) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Remover",
                tint = Color.Red
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = desejo.descricao,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "R$ ${String.format(Locale.getDefault(), "%.2f", desejo.valor)}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Icon(
            imageVector = if (podeComprar) Icons.Default.ThumbUp else Icons.Default.Warning,
            contentDescription = if (podeComprar) "OK" else "Não OK",
            tint = if (podeComprar) VerdeMoeda else Color.Red,
            modifier = Modifier.size(28.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Registros(lista: SnapshotStateList<Transacao>, saldo: Double) {
    var descricao by remember { mutableStateOf("") }
    var valorStr by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("Entrada") }
    var dataSelecionada by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    
    val datePickerState = rememberDatePickerState()
    val radioOptions = listOf("Entrada", "Saída")

    fun Long?.toDateString(): String {
        return this?.let {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            sdf.format(Date(it))
        } ?: ""
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    dataSelecionada = datePickerState.selectedDateMillis.toDateString()
                    showDatePicker = false
                }) { Text("Confirmar") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Exibição do Saldo no Topo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Saldo Atual",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "R$ ${String.format(Locale.getDefault(), "%.2f", saldo)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (saldo >= 0) VerdeMoeda else Color.Red
            )
        }

        HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp), thickness = 0.5.dp)

        Text(
            text = "Novo Registro",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = descricao,
            onValueChange = { descricao = it },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = valorStr,
            onValueChange = { valorStr = it },
            label = { Text("Valor") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = formatarParaExibicao(dataSelecionada),
                onValueChange = { },
                label = { Text("Data") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.clickable { showDatePicker = true }
                    )
                }
            )
            Box(modifier = Modifier
                .matchParentSize()
                .clickable { showDatePicker = true })
        }

        Spacer(modifier = Modifier.height(16.dp))

        // RadioGroup
        Row(
            Modifier
                .selectableGroup()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            radioOptions.forEach { text ->
                Row(
                    Modifier
                        .selectable(
                            selected = (text == tipo),
                            onClick = { tipo = text },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == tipo),
                        onClick = null, // null for accessibility
                        colors = RadioButtonDefaults.colors(selectedColor = VerdeMoeda)
                    )
                    Text(
                        text = text,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val valor = valorStr.toDoubleOrNull()
                if (descricao.isNotEmpty() && valor != null && dataSelecionada.isNotEmpty()) {
                    val valorFinal = if (tipo == "Saída") -valor else valor
                    lista.add(Transacao(dataSelecionada, descricao, valorFinal))
                    descricao = ""
                    valorStr = ""
                    dataSelecionada = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = VerdeMoeda)
        ) {
            Text("Registrar", color = Color.White)
        }
    }
}

@Composable
fun Desejos(lista: SnapshotStateList<Desejo>, saldo: Double) {
    var novaDescricao by remember { mutableStateOf("") }
    var novoValor by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Exibição do Saldo no Topo da tela de Desejos
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Saldo Atual",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "R$ ${String.format(Locale.getDefault(), "%.2f", saldo)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (saldo >= 0) VerdeMoeda else Color.Red
            )
        }

        HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp), thickness = 0.5.dp)

        // Área para inclusão de novos desejos
        Text(
            text = "Novo Desejo",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = novaDescricao,
                onValueChange = { novaDescricao = it },
                label = { Text("Nome") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = novoValor,
                onValueChange = { novoValor = it },
                label = { Text("Valor") },
                modifier = Modifier.weight(0.6f)
            )
            Button(
                onClick = {
                    val valor = novoValor.toDoubleOrNull()
                    if (novaDescricao.isNotEmpty() && valor != null) {
                        lista.add(Desejo(novaDescricao, valor))
                        novaDescricao = ""
                        novoValor = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = VerdeMoeda)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar", tint = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Minha Lista de Desejos",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(lista) { desejo ->
                ItemDesejo(
                    desejo = desejo, 
                    saldo = saldo,
                    onRemove = { lista.remove(desejo) }
                )
                HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
            }
        }
    }
}