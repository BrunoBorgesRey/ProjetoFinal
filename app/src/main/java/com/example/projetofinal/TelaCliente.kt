package com.example.projetofinal

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableTarget
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.TextUnit


class TelaCliente() : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Log.i("Teste","Tela Cliente")
            Clientes()
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Clientes() {
    val contexto = LocalContext.current
    val estadoCampoDeTextoCpf = remember { mutableStateOf(TextFieldValue("")) }
    val estadoCampoDeTextoNome = remember { mutableStateOf(TextFieldValue()) }
    val estadoCampoDeTextoTelefone = remember { mutableStateOf(TextFieldValue()) }
    val estadoCampoDeTextoEndereco = remember { mutableStateOf(TextFieldValue()) }
    val estadoCampoDeTextoInstagram = remember { mutableStateOf(TextFieldValue()) }

    val activity = (LocalContext.current as? Activity)
    Column(
        Modifier.padding(40.dp)
    ) {
        Text(text="Tela de Clientes", textAlign = TextAlign.Center,modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(25.dp))

        TextField(
            value = estadoCampoDeTextoCpf.value,
            onValueChange =  {estadoCampoDeTextoCpf.value = it },
            placeholder = Text(text = "cpf"),
            keyboardOptions= KeyboardOptions(capitalization = KeyboardCapitalization.None ),
            textStile = TextStyle(color = Color.Black, fontSize = TextUnit.Unspecified, fontFamily = FontFamily.SansSerif ),
            maxlines = 1,
            modifier = Modifier.fillMaxWidth()

        )
        Spacer(modifier = Modifier.height(25.dp))
        TextField(
            value = estadoCampoDeTextoCpf.value,
            onValueChange =  {estadoCampoDeTextoCpf.value = it },
            placeholder = Text(text = "Nome"),
            keyboardOptions= KeyboardOptions(capitalization = KeyboardCapitalization.None ),
            textStile = TextStyle(color = Color.Black, fontSize = TextUnit.Unspecified, fontFamily = FontFamily.SansSerif ),
            maxlines = 1,
            modifier = Modifier.fillMaxWidth()

        )
        Spacer(modifier = Modifier.height(25.dp))
        TextField(
            value = estadoCampoDeTextoCpf.value,
            onValueChange =  {estadoCampoDeTextoCpf.value = it },
            placeholder = Text(text = "Telefone"),
            keyboardOptions= KeyboardOptions(capitalization = KeyboardCapitalization.None ),
            textStile = TextStyle(color = Color.Black, fontSize = TextUnit.Unspecified, fontFamily = FontFamily.SansSerif ),
            maxlines = 1,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(25.dp))
        TextField(
            value = estadoCampoDeTextoCpf.value,
            onValueChange =  {estadoCampoDeTextoCpf.value = it },
            placeholder = Text(text = "Endereco"),
            keyboardOptions= KeyboardOptions(capitalization = KeyboardCapitalization.None ),
            textStile = TextStyle(color = Color.Black, fontSize = TextUnit.Unspecified, fontFamily = FontFamily.SansSerif ),
            maxlines = 1,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(25.dp))
        TextField(
            value = estadoCampoDeTextoCpf.value,
            onValueChange =  {estadoCampoDeTextoCpf.value = it },
            placeholder = Text(text = "Instagram"),
            keyboardOptions= KeyboardOptions(capitalization = KeyboardCapitalization.None ),
            textStile = TextStyle(color = Color.Black, fontSize = TextUnit.Unspecified, fontFamily = FontFamily.SansSerif ),
            maxlines = 1,
            modifier = Modifier.fillMaxWidth()
        )

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
            Log.i("TesteCliente","Botao Voltar Cliente")
            contexto.startActivity(Intent(contexto, MainActivity::class.java))
        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Voltar")
        }

    }

}
