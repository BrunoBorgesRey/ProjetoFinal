package com.example.projetofinal.telas.cliente

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetofinal.models.Cliente
import com.example.projetofinal.telas.produto.getProdutoRepository
import kotlinx.coroutines.launch

private const val TAG = "TelaCliente"

class TelaCliente() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Log.i(TAG, "Tela Cliente")
            Clientes()
        }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Clientes() {
    val repository = getProdutoRepository()
    val coroutineScope = rememberCoroutineScope()
    val contexto = LocalContext.current
    val estadoCampoDeTextoCpf = remember { mutableStateOf(TextFieldValue()) }
    val estadoCampoDeTextoNome = remember { mutableStateOf(TextFieldValue()) }
    val estadoCampoDeTextoTelefone = remember { mutableStateOf(TextFieldValue()) }
    val estadoCampoDeTextoEndereco = remember { mutableStateOf(TextFieldValue()) }
    val estadoCampoDeTextoInstagram = remember { mutableStateOf(TextFieldValue()) }
    val activity: Activity? = (LocalContext.current as? Activity)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text(
            text = "Tela de Clientes",
            fontStyle = FontStyle.Normal,
            style = TextStyle(
                fontSize = 24.sp,
            ),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(25.dp))
        TextField(
            value = estadoCampoDeTextoCpf.value,
            onValueChange = {
                estadoCampoDeTextoCpf.value = it
            },
            placeholder = { Text(text = "Insira o CPF") },
            keyboardOptions = KeyboardOptions(
                keyboardType = androidx.compose.ui.text.input.KeyboardType.Number,//Apenas números.
//                capitalization = KeyboardCapitalization.None,//Sem restrições (letras/números).
//                autoCorrect = true,
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
            value = estadoCampoDeTextoNome.value,
            onValueChange = {
                estadoCampoDeTextoNome.value = it
            },
            placeholder = { Text(text = "Insira o Nome") },
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
            value = estadoCampoDeTextoTelefone.value,
            onValueChange = {
                estadoCampoDeTextoTelefone.value = it
            },
            placeholder = { Text(text = "Insira o Telefone") },
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
            value = estadoCampoDeTextoEndereco.value,
            onValueChange = {
                estadoCampoDeTextoEndereco.value = it
            },
            placeholder = { Text(text = "Insira o Endereço") },
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
            value = estadoCampoDeTextoInstagram.value,
            onValueChange = {
                estadoCampoDeTextoInstagram.value = it
            },
            placeholder = { Text(text = "Insira o Instagram") },
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
            Log.i("TelaCliente Inserir", "Botao Inserir")

            val cpf = estadoCampoDeTextoCpf.value.text
            val nome = estadoCampoDeTextoNome.value.text
            val telefone = estadoCampoDeTextoTelefone.value.text
            val endereco = estadoCampoDeTextoEndereco.value.text
            val instagram = estadoCampoDeTextoInstagram.value.text

            if (cpf.isNotEmpty() && nome.isNotEmpty() && telefone.isNotEmpty() && endereco.isNotEmpty() && instagram.isNotEmpty()) {
                val cliente = Cliente(
                    cpf = cpf,
                    nome = nome,
                    telefone = telefone,
                    endereco = endereco,
                    instagram = instagram
                )

//                     Inicie uma coroutine para buscar o ByteArray da imagem
                coroutineScope.launch {


                    repository.salvarcliente(cliente)


                    Log.i("TelaCliente", "Botao Inserir")
                }
            }
        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Inserir")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            Log.i(TAG, "Botao Listar")
            contexto.startActivity(Intent(contexto, ListaCliente::class.java))
        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Listar")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            Log.i(TAG, "Botao Voltar Cliente")
            activity?.finish()
        }, modifier = Modifier.width(300.dp)) {
            Text(text = "Voltar")
        }

    }

}


@Preview
@Composable
fun ClientesPreview() {
    Clientes()
}
