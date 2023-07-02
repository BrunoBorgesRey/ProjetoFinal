package com.example.projetofinal.telas.cliente

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetofinal.models.Cliente
import com.example.projetofinal.telas.produto.getProdutoRepository
import kotlinx.coroutines.launch

class AlterarCliente : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val receivedString = intent.getStringExtra("myStringExtra")
            AlterarClienteTela(receivedString)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AlterarClienteTela(idcliente: String?){
    val repository = getProdutoRepository()
    val coroutineScope = rememberCoroutineScope()
    val contexto = LocalContext.current
    val estadoCampoDeTextoCpf = remember { mutableStateOf(TextFieldValue()) }
    val estadoCampoDeTextoNome = remember { mutableStateOf(TextFieldValue()) }
    val estadoCampoDeTextoTelefone = remember { mutableStateOf(TextFieldValue()) }
    val estadoCampoDeTextoEndereco = remember { mutableStateOf(TextFieldValue()) }
    val estadoCampoDeTextoInstagram = remember { mutableStateOf(TextFieldValue()) }



    LazyColumn(
        Modifier
            .padding(40.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if(idcliente != null) {
            coroutineScope.launch {
                repository.buscaPorIdCliente(idcliente) { cliente ->
                    cliente?.let {
                        estadoCampoDeTextoCpf.value = TextFieldValue(text = cliente.cpf)
                        estadoCampoDeTextoNome.value = TextFieldValue(text = cliente.nome)
                        estadoCampoDeTextoTelefone.value = TextFieldValue(text = cliente.telefone)
                        estadoCampoDeTextoEndereco.value = TextFieldValue(text = cliente.endereco)
                        estadoCampoDeTextoInstagram.value = TextFieldValue(text = cliente.instagram)
                    } ?: run {
                        Log.i("TAG", "AlterarClienteTela: Cliente é nulo")
                        Toast.makeText(contexto, "Cliente não encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        } else  {
            Log.i("TAG", "AlterarClienteTela: idCliente é nulo")
            Toast.makeText(contexto, "Cliente não encontrado", Toast.LENGTH_SHORT).show()
        }
        item {
            Text(
                text = "Alterar Cliente",
                fontStyle = FontStyle.Normal,
                style = TextStyle(
                    fontSize = 24.sp,
                ),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(25.dp))
        }
        item{
            TextField(
                value = estadoCampoDeTextoCpf.value,
                onValueChange = {
                    estadoCampoDeTextoCpf.value = it
                },
                placeholder = { Text(text = "Insira o CPF") },
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
        }
        item{
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
        }
        item{
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
        }
        item{
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
        }
        item{
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
        }
        item{
            Button(onClick = {
                val cpf = estadoCampoDeTextoCpf.value.text
                val nome = estadoCampoDeTextoNome.value.text
                val telefone = estadoCampoDeTextoTelefone.value.text
                val endereco = estadoCampoDeTextoEndereco.value.text
                val instagram = estadoCampoDeTextoInstagram.value.text

                if (idcliente != null && cpf.isNotEmpty() && nome.isNotEmpty() && telefone.isNotEmpty() && endereco.isNotEmpty() && instagram.isNotEmpty()) {
                    val cliente = Cliente(
                        id= idcliente,
                        cpf = cpf,
                        nome = nome,
                        telefone = telefone,
                        endereco = endereco,
                        instagram = instagram
                    )

//                     Inicie uma coroutine para buscar o ByteArray da imagem
                    coroutineScope.launch {
                        repository.editarCliente(idcliente, cliente)
                        Log.i("AlterarCliente", "Botao Alterar")
                    }
                }
            }, modifier = Modifier.width(300.dp)) {
                Text(text = "Alterar")
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
        item{
            Button(onClick = {
                Log.i("TelaCliente","Botao Voltar Cliente")
                contexto.startActivity(Intent(contexto, ListaCliente::class.java))
            }, modifier = Modifier.width(300.dp)) {
                Text(text = "Voltar")
            }
        }



    }
}