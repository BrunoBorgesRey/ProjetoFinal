package com.example.projetofinal.telas.pedido


import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.projetofinal.models.Pedido
import com.example.projetofinal.models.Produto
import com.example.projetofinal.telas.MainActivity
import com.example.projetofinal.telas.produto.getProdutoRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar


class TelaPedido() : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Log.i("TelaPedido oncreate","Tela Pedido")
            Pedidos()
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Pedidos() {
    val repository = getProdutoRepository()
    val coroutineScope = rememberCoroutineScope()
    val contexto = LocalContext.current
    val clientesLiveData = repository.buscaTodosCliente()
    val clientesState by clientesLiveData.observeAsState(emptyList())
    val produtosLiveData = repository.buscaTodos()
    val produtosState by produtosLiveData.observeAsState(emptyList())
    var estadoCampoDeTextoFkCpf by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Escolher Cliente") }
    val calendar = Calendar.getInstance()

    var selectedDateText by remember { mutableStateOf("") }

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
        Button(onClick = {
            Log.i("TelaPedido","Botao Inserir")
            val cliente = estadoCampoDeTextoFkCpf
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val data = dateFormat.parse(selectedDateText)

            if (cliente.isNotEmpty() ) {
                val pedido = Pedido(
                    cliente = cliente,
                    listaProduto = selectedProdutos,
                    data = data,
                )
//                     Inicie uma coroutine para buscar o ByteArray da imagem
                coroutineScope.launch {
                    repository.salvarpedido(pedido)

                }
            }

        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Inserir")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            Log.i("TelaPedido","Botao Listar")
            contexto.startActivity(Intent(contexto, ListaPedido::class.java))
        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Listar")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            Log.i("TelaPedido","Botao Voltar Pedido")
            contexto.startActivity(Intent(contexto, MainActivity::class.java))
        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Voltar")
        }

    }


}

@Composable
fun FilterItem(
    filter: Produto,
    selectedProdutos: MutableList<Produto>,
    //onProdutoSelected: (Produto) -> Unit
) {
    val isSelected = selectedProdutos.contains(filter)

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .border(
                width = 1.dp,
                color = if (isSelected) Color.Black else Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .fillMaxWidth()
            .clickable {
                if (isSelected) {
                    selectedProdutos.remove(filter)
                } else {
                    selectedProdutos.add(filter)
                }
                //onProdutoSelected(filter)
            }
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = filter.descricao
        )
    }
}

