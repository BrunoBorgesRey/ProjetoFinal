package com.example.projetofinal.telas.pedido

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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


    val clientesLiveData = repository.buscaTodosCliente()
    val clientesState by clientesLiveData.observeAsState(emptyList())

    var estadoCampoDeTextoFkCpf by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Escolher Cliente") }

    Column(
        Modifier.padding(40.dp)
    ) {
        Text(text="Tela de Pedido", textAlign = TextAlign.Center,modifier = Modifier.fillMaxWidth())
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
                            //Toast.makeText(contexto, item, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
        Button(onClick = {
            Log.i("TelaPedido","Botao Inserir")

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
            Log.i("TelaPedido","Botao Voltar Pedido")
            contexto.startActivity(Intent(contexto, MainActivity::class.java))
        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Voltar")
        }

    }

}
