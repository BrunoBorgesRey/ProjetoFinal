package com.example.projetofinal.telas.cliente
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.projetofinal.models.Cliente
import com.example.projetofinal.telas.produto.AlterarProduto
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
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                Modifier
                    .weight(9f)
                    .fillMaxHeight()
            ) {

                Column(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "CPF:${cliente.cpf}",
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
                        text = "Nome:${cliente.nome}", fontStyle = FontStyle.Normal,
                        style = TextStyle(
                            fontSize = 14.sp,
                        ),
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text ="Telefone:${cliente.telefone}", fontStyle = FontStyle.Normal,
                        style = TextStyle(
                            fontSize = 14.sp,
                        ),
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Endereco:${cliente.endereco}", fontStyle = FontStyle.Normal,
                        style = TextStyle(
                            fontSize = 14.sp,
                        ),
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Instagram:${cliente.instagram}", fontStyle = FontStyle.Normal,
                        style = TextStyle(
                            fontSize = 14.sp,
                        ),
                        fontWeight = FontWeight.Bold,
                    )
                }
            }


            Column(
                Modifier.width( 70.dp )
            ) {
                Button(
                    onClick = {
                        repository.removeCliente(cliente.id)
                    },
                    shape = RoundedCornerShape(100),
                    modifier = Modifier.fillMaxSize()

                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Icon(
                            Icons.Rounded.DeleteForever,
                            contentDescription = null
                        )

                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        val intent = Intent(contexto, AlterarCliente::class.java).apply {
                            putExtra("myStringExtra", cliente.id)
                        }
                        contexto.startActivity(intent)
                    },
                    shape = RoundedCornerShape(100),
                    modifier = Modifier
                        .fillMaxSize()

                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {

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