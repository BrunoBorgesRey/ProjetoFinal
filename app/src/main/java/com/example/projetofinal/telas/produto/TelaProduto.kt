package com.example.projetofinal.telas.produto

import android.app.Activity
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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
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
import androidx.lifecycle.LiveData
import coil.compose.AsyncImage
import com.example.projetofinal.models.Produto
import com.example.projetofinal.repository.ProdutoRepository
import com.example.projetofinal.telas.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.getKoin

class TelaProduto : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Produtos()
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Produtos() {
    val repository = getProdutoRepository()
    val coroutineScope = rememberCoroutineScope()
    val contexto = LocalContext.current

    val estadoCampoDeTextoDescricao = remember { mutableStateOf(TextFieldValue()) }
    val estadoCampoDeTextoValor = remember { mutableStateOf(TextFieldValue()) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    var singlePhotoPickerLaucher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri -> selectedImageUri = uri })

    var produto: Produto? = null
    val activity: Activity? = (LocalContext.current as? Activity)
    LazyColumn(
        Modifier
            .padding(40.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Text(
                text = "Tela de Produto",
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
                        .size(200.dp).padding(10.dp),


                    contentScale = ContentScale.Crop,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        item {

            Button(
                enabled = estadoCampoDeTextoDescricao.value.text.isNotEmpty() &&
                        estadoCampoDeTextoValor.value.text.isNotEmpty() &&
                        selectedImageUri != null,
                onClick = {
                    Log.i("TelaProduto Inserir", "Botao Inserir")
                    val descricao = estadoCampoDeTextoDescricao.value.text
                    val valor = estadoCampoDeTextoValor.value.text.toDoubleOrNull()
                    val fotoUri = selectedImageUri


                    if (descricao.isNotEmpty() && valor != null && fotoUri != null) {
                        val produto = Produto(descricao = descricao, preco = valor)
                        coroutineScope.launch {
                            val fotoByteArray = withContext(Dispatchers.IO) {
//                     TODO: remover esse código para uma função específica ->  getByteArrayFromUri(fotoUri)
                                val inputStream = contexto.contentResolver.openInputStream(fotoUri)
                                val bytes = inputStream?.readBytes()
                                inputStream?.close()
                                bytes
                            }

                            if (selectedImageUri == null) {
                                Toast.makeText(contexto, "Selecione uma foto", Toast.LENGTH_LONG)
                                    .show()
                            }

                            val resultado: LiveData<Boolean> = repository.salvarProduto(produto, fotoByteArray!!)

                            if (resultado.value == true) {
                                Toast.makeText(
                                    contexto,
                                    "Produto ${produto.descricao} salva com sucesso",
                                    Toast.LENGTH_LONG
                                ).show()

                                estadoCampoDeTextoDescricao.value = TextFieldValue("")
                                estadoCampoDeTextoValor.value = TextFieldValue("")
                                selectedImageUri = null
                            } else {
                                // TODO Lidar com a falha ao obter o ByteArray da imagem
                                Toast.makeText(
                                    contexto,
                                    "Houve algum erro ao salvar o produto ${produto.descricao}",
                                    Toast.LENGTH_LONG
                                ).show()
                                Log.e("TelaProduto Inserir", "Erro ao inserir")
                            }
                        }
                    } else {
                        // TODO Lidar com dados inválidos
                        Log.e("TelaProduto Inserir", "Erro ao inserir")
                    }
                    Log.i("TelaProduto", "Botao Inserir")
                },
                modifier = Modifier.width(300.dp)
            ) {
                Text(
                    text = "Inserir", fontStyle = FontStyle.Normal,
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
                Log.i("TelaProduto", "Botao Listar")
                contexto.startActivity(Intent(contexto, ListaProduto::class.java))
            }, modifier = Modifier.width(300.dp)) {
                Text(
                    text = "Listar",
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
                activity?.finish()
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

@Composable
fun getProdutoRepository(): ProdutoRepository {
    Log.i("TelaProduto", "chegou no método getProdutoRepository() ")
    val produtoRepository by getKoin().inject<ProdutoRepository>()
    Log.i("TelaProduto", "getProdutoRepository() $produtoRepository")
    return produtoRepository
}


//private suspend fun getByteArrayFromUri(uri: Uri): ByteArray? {
//    val context = LocalContext.current
//    return withContext(Dispatchers.IO) {
//        val inputStream = context.contentResolver.openInputStream(uri)
//        inputStream?.readBytes()
//    }
//}

//@Preview(showSystemUi = true)
//@Composable
//fun ProdutosPreview() {
//    Produtos()
//}

