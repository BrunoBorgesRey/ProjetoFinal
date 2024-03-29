package com.example.projetofinal.telas.pedido

import android.app.Activity
import android.app.DatePickerDialog

import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material3.Button

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.projetofinal.models.Cliente
import com.example.projetofinal.models.Pedido
import com.example.projetofinal.models.Produto

import com.example.projetofinal.telas.produto.getProdutoRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

class AlterarPedido : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val receivedString = intent.getStringExtra("myStringExtra")
            AlterarPedidoTela(receivedString)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AlterarPedidoTela(idpedido: String?) {
    val repository = getProdutoRepository()
    val coroutineScope = rememberCoroutineScope()
    val contexto = LocalContext.current
    val clientesLiveData = repository.buscaTodosCliente()
    val clientesState by clientesLiveData.observeAsState(emptyList())
    val produtosLiveData = repository.buscaTodosProdutos()
    val produtosState by produtosLiveData.observeAsState(emptyList())
    var estadoCampoDeTextoFkCpf by remember { mutableStateOf("")  }
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Escolher Cliente") }
    val calendar = Calendar.getInstance()

    var selectedDateText by remember { mutableStateOf("") }
    val activity: Activity? = (LocalContext.current as? Activity)
// Fetching current year, month and day
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
    val selectedProdutos: MutableList<Produto> = remember { mutableListOf() }
    val datePicker = DatePickerDialog(
        contexto,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            selectedDateText = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
        }, year, month, dayOfMonth
    )



    Column(
        Modifier.padding(40.dp)
    ) {
        Text(text="Tela de Pedido", textAlign = TextAlign.Center,modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = if (selectedDateText.isNotEmpty()) {
                "Selected date is $selectedDateText"
            } else {
                "Please pick a date"
            }, textAlign = TextAlign.Center,modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                datePicker.show()
            }
        ) {
            Text(text = "Selecione a data", textAlign = TextAlign.Center,modifier = Modifier.fillMaxWidth())
        }
        Spacer(modifier = Modifier.height(10.dp))
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                clientesState.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.nome) },
                        onClick = {
                            selectedText = item.nome
                            estadoCampoDeTextoFkCpf = item.id
                            expanded = false

                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn {
            items(items = produtosState) { item ->
                FilterItem(
                    filter = item,
                    selectedProdutos = selectedProdutos,
                    //onProdutoSelected = { produto ->
                    // Handle the selection logic if needed
                    //}
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            Log.i("TelaPedido","Botao Inserir")
            val cliente = estadoCampoDeTextoFkCpf
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val data = dateFormat.parse(selectedDateText)


                val pedido = idpedido?.let {

                        Pedido(
                            id = it,
                            cliente = cliente,
                            listaProduto = selectedProdutos,
                            data = data,
                        )

                }
                coroutineScope.launch {
                    if (idpedido != null && pedido != null) {
                            repository.editarPedido(idpedido, pedido)

                    }

                }

        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Alterar")
        }

        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            Log.i("TelaPedido","Botao Voltar Pedido")
            activity?.finish()
        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Voltar")
        }

    }


}