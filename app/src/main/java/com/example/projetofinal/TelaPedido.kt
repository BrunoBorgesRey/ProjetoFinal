package com.example.projetofinal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

class TelaPedido() : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Log.i("Teste","Tela Cliente")
            Pedidos()
        }
    }
}


@Composable
fun Pedidos() {
    val contexto = LocalContext.current
    val estadoCampoDeTextoCpf = remember { mutableStateOf(TextFieldValue()) }
    val estadoCampoDeTexto = remember { mutableStateOf(TextFieldValue()) }
    val activity = (LocalContext.current as? Activity)
    Column(
        Modifier.padding(40.dp)
    ) {
        Text(text="Tela de Pedido", textAlign = TextAlign.Center,modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(25.dp))
        /*TextField(
            value = estadoCampoDeTextoCpf.value,
            onValueChange = { estadoCampoDeTextoCpf.value = it },
            placeholder = { Text(text = "CPF") },
            maxlines = 1,
            modifier = Modifier.fillMaxWidth()

        )*/
        Spacer(modifier = Modifier.height(25.dp))
        Button(onClick = {
            Log.i("TesteCliente","Botao Inserir")
        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Inserir")
        }
        Spacer(modifier = Modifier.height(25.dp))
        Button(onClick = {
            Log.i("TesteCliente","Botao Inserir")
        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Listar")
        }
        Spacer(modifier = Modifier.height(25.dp))
        Button(onClick = {
            Log.i("TesteCliente","Botao Inserir")
        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Deletar")
        }
        Spacer(modifier = Modifier.height(25.dp))
        Button(onClick = {
            Log.i("TesteCliente","Botao Inserir")
        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Alterar")
        }
        Spacer(modifier = Modifier.height(25.dp))
        Button(onClick = {
            Log.i("TestePedido","Botao Voltar Pedido")
            contexto.startActivity(Intent(contexto, MainActivity::class.java))
        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Voltar")
        }

    }

}
