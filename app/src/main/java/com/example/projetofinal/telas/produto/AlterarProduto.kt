package com.example.projetofinal.telas.produto

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import coil.compose.AsyncImage
import com.example.projetofinal.models.Produto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "AlterarProduto"

class AlterarProduto : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val receivedString = intent.getStringExtra("myStringExtra")
            Log.i(TAG, "onCreate: $receivedString")
            AlterarProdutoTela(receivedString)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlterarProdutoTela(idproduto: String?) {
    val repository = getProdutoRepository()
    val coroutineScope = rememberCoroutineScope()
    val contexto = LocalContext.current
    val estadoCampoDeTextoDescricao = remember { mutableStateOf(TextFieldValue()) }
    val estadoCampoDeTextoValor = remember { mutableStateOf(TextFieldValue()) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val singlePhotoPickerLaucher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri -> selectedImageUri = uri })

    LazyColumn(
        Modifier
            .padding(40.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Log.i(TAG, "AlterarProdutoTela: idproduto: $idproduto")


        if(idproduto != null) {
            coroutineScope.launch {
                repository.buscaPorId(idproduto) { produto ->
                    produto?.let {
                        Log.i(TAG, "AlterarProdutoTela: PRODUTO ${produto.toString()}")

                        estadoCampoDeTextoDescricao.value = TextFieldValue(text = produto.descricao)
                        Log.i(TAG, "AlterarProdutoTela: ${produto.descricao}")
                        estadoCampoDeTextoValor.value = estadoCampoDeTextoValor.value.copy(text = produto.preco.toString())
                        Log.i(TAG, "AlterarProdutoTela: ${produto.preco}")
                        selectedImageUri = Uri.parse(produto.foto.toString())
                        Log.i(TAG, "AlterarProdutoTela: ${produto.foto}")
                    } ?: run {
                        Log.i(TAG, "AlterarProdutoTela: Produto é nulo")
                        Toast.makeText(contexto, "Produto não encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        } else  {
                Log.i(TAG, "AlterarProdutoTela: idproduto é nulo")
                Toast.makeText(contexto, "Produto não encontrado", Toast.LENGTH_SHORT).show()
        }


        item {
            Text(
                text = "Alterar Produto",
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
        item {
            TextField(
                value = estadoCampoDeTextoDescricao.value,
                onValueChange = {
                    estadoCampoDeTextoDescricao.value = it
                },
                modifier = Modifier.height(60.dp),
                placeholder = {
                    Text(
                        text = "Insira a Descrição",
                        style = TextStyle(
                            fontSize = 18.sp,
                        ),
                        fontWeight = FontWeight.Bold,
                    )
                },
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
        item {
            TextField(
                value = estadoCampoDeTextoValor.value,
                onValueChange = {
                    estadoCampoDeTextoValor.value = it
                },
                placeholder = {
                    Text(
                        text = "Insira o Valor",
                        style = TextStyle(
                            fontSize = 18.sp,
                        ),
                        fontWeight = FontWeight.Bold,
                    )
                },
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
            Spacer(modifier = Modifier.height(25.dp))
        }
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    singlePhotoPickerLaucher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {
                    Text(
                        text = "Selecione uma foto", fontStyle = FontStyle.Normal,
                        style = TextStyle(
                            fontSize = 16.sp,
                        ),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                }

                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    contentScale = ContentScale.Crop,
                )
            }
        }
        item {
            Button(onClick = {
                Log.i("TelaProduto Inserir", "Botao Alterar")
                val descricao = estadoCampoDeTextoDescricao.value.text
                val valor = estadoCampoDeTextoValor.value.text.toDoubleOrNull()
                val fotoUri = selectedImageUri

                if (descricao.isNotEmpty() && idproduto != null && valor != null && fotoUri != null) {
                    val produto = Produto(id= idproduto,descricao = descricao, preco = valor)

//                     Inicie uma coroutine para buscar o ByteArray da imagem
                    coroutineScope.launch {
                        val fotoByteArray = withContext(Dispatchers.IO) {
//                     TODO: remover esse código para uma função específica ->  getByteArrayFromUri(fotoUri)
                            val inputStream = contexto.contentResolver.openInputStream(fotoUri)
                            inputStream?.readBytes()
                        }
                        produto.foto = fotoByteArray.toString()
                        Log.i("TelaProduto Alterar", "$produto, $fotoByteArray")
                        if (fotoByteArray != null) {
                                repository.editar(idproduto, produto)
                        } else {
                            // TODO Lidar com a falha ao obter o ByteArray da imagem
                        }
                    }
                } else {
                    // Lidar com dados inválidos
                    Log.e("TelaProduto Inserir", "Erro ao inserir")
                }
                Log.i("TelaProduto", "Botao Alterar")
            }, modifier = Modifier.width(300.dp)) {
                Text(
                    text = "Alterar", fontStyle = FontStyle.Normal,
                    style = TextStyle(
                        fontSize = 18.sp,
                    ),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(modifier = Modifier.height(25.dp))
        }
        item {
            Button(onClick = {
                Log.i("TelaProduto", "Botao Voltar Produto")
                contexto.startActivity(Intent(contexto, ListaProduto::class.java))
            }, modifier = Modifier.width(300.dp)) {
                Text(
                    text = "Voltar",
                    style = TextStyle(
                        fontSize = 18.sp,
                    ),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}