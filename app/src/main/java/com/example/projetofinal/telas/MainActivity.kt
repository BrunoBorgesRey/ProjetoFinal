package com.example.projetofinal.telas

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.projetofinal.telas.cliente.TelaCliente
import com.example.projetofinal.telas.pedido.TelaPedido
import com.example.projetofinal.telas.produto.TelaProduto

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Log.i("MainActivity", "Comeca Menu")
            MenuPrincipal()
        }

    }
}

@Composable
fun MenuPrincipal() {
    val contexto = LocalContext.current
    Column(
        Modifier.padding(40.dp)
    ) {
        Text(text = "Pé fedorento", textAlign = TextAlign.Center, modifier = Modifier.width(300.dp))
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {
                Log.i("MenuPrincipal", "Botão Cliente")
                contexto.startActivity(Intent(contexto, TelaCliente::class.java))
            },
            modifier = Modifier.width(300.dp)
        ) {
            Text(text = "Clientes")

        }
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {
                Log.i("MenuPrincipal", "Botão Produto")
                contexto.startActivity(Intent(contexto, TelaProduto::class.java))
            },
            modifier = Modifier.width(300.dp)
        ) {
            Text(text = "Produtos")

        }
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {
                Log.i("MenuPrincipal", "Botão Pedidos")
                contexto.startActivity(Intent(contexto, TelaPedido::class.java))
            },
            modifier = Modifier.width(300.dp)
        ) {
            Text(text = "Pedidos")

        }
    }

}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//
//}