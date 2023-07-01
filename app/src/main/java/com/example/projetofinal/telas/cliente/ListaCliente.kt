package com.example.projetofinal.telas.cliente
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.example.projetofinal.models.Cliente
import com.example.projetofinal.telas.produto.getProdutoRepository


class ListaCliente : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val repository = getProdutoRepository()
            Log.i("ListaCliente","ComecaBusca")
            val clientesLiveData = repository.buscaTodosCliente()
            Log.i("ListaCliente","Acaba Busca")
            ListaClientesContent(clientesLiveData)
        }
    }
}

@Composable
fun ListaClientesContent(clientesLiveData: LiveData<List<Cliente>>) {
    val clientesState by clientesLiveData.observeAsState(emptyList())
    ListaClientes(clientes = clientesState)
}

@Composable
fun ListaClientes(clientes: List<Cliente>) {
    LazyColumn {
        items(clientes) { cliente ->
            ProductItem(cliente = cliente)
        }
    }
}

@Composable
fun ProductItem(cliente: Cliente) {
    val repository = getProdutoRepository()
    val contexto = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Display the product details
        Text(
            text = cliente.nome,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            ),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = cliente.cpf,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            ),
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = {
                repository.removeCliente(cliente.id) },
            modifier = Modifier.width(100.dp)
        ) {
            Text(text = "Deletar")
        }
        Button(
            onClick = {
                val intent = Intent(contexto, AlterarCliente::class.java).apply {
                    putExtra("myStringExtra", cliente.id)
                }
                contexto.startActivity(intent)
            },
            modifier = Modifier.width(100.dp)
        ) {
            Text(text = "Alterar")
        }
    }
}