package com.example.projetofinal.telas.produto

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import coil.compose.AsyncImage
import com.example.projetofinal.models.Produto

private const val TAG = "ListaProdutos"

class ListaProduto : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val repository = getProdutoRepository()
            val produtosLiveData = repository.buscaTodosProdutos()

            Log.i(TAG, "onCreate: ${produtosLiveData.value.toString()}")
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
    LazyColumn(contentPadding = PaddingValues(bottom = 8.dp)) {
        items(produtos) { produto ->
            Log.i(TAG, "ListaProdutos: ${produto.toString()}")
            ProductItem(produto = produto)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductItem(produto: Produto) {
    val repository = getProdutoRepository()
    val contexto = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    selectedImageUri = Uri.parse(produto.foto.toString())

    Log.i(TAG, "nome:${produto.toString()}")
    Card(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
//                .fillMaxWidth()
//                .fillMaxHeight()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                Modifier
                    .weight(9f)
                    .fillMaxHeight()
            ) {
                produto.foto?.let { foto ->
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = produto.descricao,
//                    modifier = Modifier
//                        .size(50.dp)
//                        .padding(bottom = 20.dp),
                        modifier = Modifier
                            .width(80.dp),
                        contentScale = ContentScale.Crop,
                    )
                }
                Column(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = produto.descricao,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontStyle = FontStyle.Normal,
                        style = TextStyle(
                            fontSize = 14.sp,
                        ),
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = produto.preco.toString(), fontStyle = FontStyle.Normal,
                        style = TextStyle(
                            fontSize = 14.sp,
                        ),
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
            // Display the product details
//            Column() {
//                AsyncImage(
//                    model = selectedImageUri,
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(50.dp)
//                        .padding(bottom = 20.dp),
//                    contentScale = ContentScale.Crop,
//                )
//                Text(
//                    text = produto.descricao,
//                    style = TextStyle(
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 16.sp
//                    ),
//                    modifier = Modifier.weight(1f)
//                )
//                Log.i("ListaProduto", "nome:${produto.preco}")
//                Text(
//                    text = produto.preco.toString(),
//                    style = TextStyle(
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 16.sp
//                    ),
//                    modifier = Modifier.weight(1f)
//                )
//            }

            Column(
                Modifier.width( 70.dp )
            ) {
                Button(
                    onClick = {
                        repository.remove(produto.id)
                    },
                    shape = RoundedCornerShape(100),
                    modifier = Modifier.fillMaxSize()

                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        IconButton(onClick = {
//                            repository.remove(produto.id)
//                            /* doSomething() */
//                        },  modifier = Modifier.size(30.dp),
//                        ) {
//                            Icon(Icons.Rounded.DeleteForever, contentDescription = "Localized description")
//                        }
                        Icon(
                            Icons.Rounded.DeleteForever,
                            contentDescription = null
                        )

                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        val intent = Intent(contexto, AlterarProduto::class.java).apply {
                            putExtra("myStringExtra", produto.id)
                        }
                        contexto.startActivity(intent)
                    },
                    shape = RoundedCornerShape(100),
                    modifier = Modifier
                        .fillMaxSize()

                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        IconButton(modifier = Modifier.size(30.dp),
//                        ) {
//                            Icon(Icons.Rounded.Edit, contentDescription = "Localized description")
//                        }
                        Icon(
                            Icons.Rounded.Edit,
                            contentDescription = null
                        )

                    }

                }
            }

        }
    }


}




