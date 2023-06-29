package com.example.projetofinal
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.projetofinal.models.Produto



class ListaProduto : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val repository = getProdutoRepository()
            val produtosLiveData = repository.buscaTodos()
            ListaProdutosContent(produtosLiveData)
        }
    }
}

@Composable
fun ListaProdutosContent(produtosLiveData: LiveData<List<Produto>>) {
    val produtosState by produtosLiveData.observeAsState(emptyList())
    ListaProdutos(produtos = produtosState)
}

@Composable
fun ListaProdutos(produtos: List<Produto>) {
    LazyColumn {
        items(produtos) { produto ->
            ProductItem(produto = produto)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItem(produto: Produto) {
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
            text = produto.descricao,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            ),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = produto.preco.toString(),
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            ),
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = {
                repository.remove(produto.id) },
            modifier = Modifier.width(100.dp)
        ) {
            Text(text = "Deletar")
        }
        Button(
            onClick = {
                val intent = Intent(contexto, AlterarProduto::class.java).apply {
                    putExtra("myStringExtra", produto.id)
                }
                contexto.startActivity(intent)
            },

            modifier = Modifier.width(100.dp)
        ) {
            Text(text = "Alterar")
        }
    }
}




