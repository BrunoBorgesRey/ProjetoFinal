package com.example.projetofinal.telas.pedido

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.projetofinal.telas.MainActivity
import com.example.projetofinal.telas.produto.getProdutoRepository


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
    val estadoCampoDeTextoIdPedido = remember { mutableStateOf(TextFieldValue()) }
    val estadoCampoDeTextoData = remember { mutableStateOf(TextFieldValue()) }
    val estadoCampoDeTextoFkCpf = remember { mutableStateOf(TextFieldValue()) }
    val activity = (LocalContext.current as? Activity)
    Column(
        Modifier.padding(40.dp)
    ) {
        Text(text="Tela de Pedido", textAlign = TextAlign.Center,modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = estadoCampoDeTextoIdPedido.value,
            onValueChange = {
                estadoCampoDeTextoIdPedido.value = it
            },
            placeholder = { Text(text = "Insira o ID Pedido") },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,//Sem restrições (letras/números).
                autoCorrect = true,
            ),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = TextUnit.Unspecified,
                fontFamily = FontFamily.SansSerif
            ),
            maxLines = 1,
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = estadoCampoDeTextoData.value,
            onValueChange = {
                estadoCampoDeTextoData.value = it
            },
            placeholder = { Text(text = "Insira a Data") },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,//Sem restrições (letras/números).
                autoCorrect = true,
            ),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = TextUnit.Unspecified,
                fontFamily = FontFamily.SansSerif
            ),
            maxLines = 1,
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = estadoCampoDeTextoFkCpf.value,
            onValueChange = {
                estadoCampoDeTextoFkCpf.value = it
            },
            placeholder = { Text(text = "Insira o CPF do Cliente") },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,//Sem restrições (letras/números).
                autoCorrect = true,
            ),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = TextUnit.Unspecified,
                fontFamily = FontFamily.SansSerif
            ),
            maxLines = 1,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            Log.i("TelaPedido","Botao Inserir")
            /*val idPedido = estadoCampoDeTextoIdPedido.value.text
            val data = estadoCampoDeTextoData.value.to

            if (data.isNotEmpty() ) {
                val pedido = Pedido(
                    idPedido = idPedido,
                    data = data ,
                    listaProduto = ""
                )
//                     Inicie uma coroutine para buscar o ByteArray da imagem
                coroutineScope.launch {
                    repository.salvarpedido(pedido)
                    Log.i("TelaCliente", "Botao Inserir")
                }
            }*/
        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Inserir")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            Log.i("TelaPedido","Botao Listar")

        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Listar")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            Log.i("TelaPedido","Botao Deletar")
        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Deletar")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            Log.i("TelaPedido","Botao Alterar")
        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Alterar")
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
