package com.example.projetofinal.telas.pedido
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
import com.example.projetofinal.models.Pedido
import com.example.projetofinal.telas.produto.getProdutoRepository

private const val TAG = "ListaPedido"
class ListaPedido : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val repository = getProdutoRepository()
            Log.i("ListaPedido","instancia repository, ${repository.toString()}")

            val listaPedidosLiveData = repository.buscarTodosPedidos()
            Log.i("ListaPedido","resultado do buscarTodosPedidos: ${listaPedidosLiveData.toString()}")
            ListaPedidosContent(listaPedidosLiveData)
        }
    }
}

@Composable
fun ListaPedidosContent(pedidosLiveData: LiveData<List<Pedido>>) {
    val pedidosState by pedidosLiveData.observeAsState(emptyList())
    Log.i(TAG, "ListaPedidosContent: pedidosLiveData valor da lista de pedidos ${pedidosLiveData.value}")
    Log.i(TAG, "ListaPedidosContent: pedidosState $pedidosState")
    ListaPedidos(pedidos = pedidosState)
}

@Composable
fun ListaPedidos(pedidos: List<Pedido>) {
    LazyColumn {
        items(pedidos) { pedido ->
            ProductItem(pedido = pedido)
        }
    }
}

@Composable
fun ProductItem(pedido: Pedido) {
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
            text = pedido.data.toString(),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            ),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = pedido.listaProduto.toString(),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            ),
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = {
                repository.removePedido(pedido.id) },
            modifier = Modifier.width(100.dp)
        ) {
            Text(text = "Deletar")
        }
        Button(
            onClick = {
                val intent = Intent(contexto, AlterarPedido::class.java).apply {
                    putExtra("myStringExtra", pedido.id)
                }
                contexto.startActivity(intent)
            },
            modifier = Modifier.width(100.dp)
        ) {
            Text(text = "Alterar")
        }
    }
}
