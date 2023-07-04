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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AlterarProdutoTela(idproduto: String?) {
    val repository = getProdutoRepository()
    val coroutineScope = rememberCoroutineScope()
    val contexto = LocalContext.current
    val activity: Activity? = (LocalContext.current as? Activity)
    val estadoCampoDeTextoDescricao = remember { mutableStateOf(TextFieldValue()) }
    val estadoCampoDeTextoValor = remember { mutableStateOf(TextFieldValue()) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val singlePhotoPickerLaucher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri -> selectedImageUri = uri })

//    var estadoFotoByteArray by remember { mutableStateOf<ByteArray?>(null) }
    var fotoByteArray: ByteArray? = null

    var fotoAlterada: Boolean = false
    LazyColumn(
        Modifier
            .padding(40.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Log.i(TAG, "AlterarProdutoTela: idproduto: $idproduto")

        if (idproduto != null) {
            coroutineScope.launch {
                repository.buscaPorId(idproduto) { produto ->
                    produto?.let {
                        Log.i(TAG, "AlterarProdutoTela: PRODUTO $produto")

                        estadoCampoDeTextoDescricao.value = TextFieldValue(text = produto.descricao)
                        Log.i(TAG, "AlterarProdutoTela: ${produto.descricao}")
                        estadoCampoDeTextoValor.value =
                            estadoCampoDeTextoValor.value.copy(text = produto.preco.toString())
                        Log.i(TAG, "AlterarProdutoTela: ${produto.preco}")
                        selectedImageUri = Uri.parse(produto.foto.toString())
                        Log.i(TAG, "AlterarProdutoTela: ${produto.foto}")
                    } ?: run {
                        Log.i(TAG, "AlterarProdutoTela: Produto é nulo")
                        Toast.makeText(contexto, "Produto não encontrado", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        } else {
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
                label = {
                    Text(
                        text = "Descrição: ",
                        style = TextStyle(
                            fontSize = 12.sp,
                        ),
                        modifier = Modifier.padding(bottom = 10.dp),
                        fontWeight = FontWeight.Bold,
                    )
                },
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Descrição",
                        style = TextStyle(
                            fontSize = 20.sp,
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
                maxLines = 3,
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        item {
            TextField(
                value = estadoCampoDeTextoValor.value,
                onValueChange = {
                    estadoCampoDeTextoValor.value = it
                },
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                label = {
                    Text(
                        text = "Valor: ",
                        style = TextStyle(
                            fontSize = 12.sp,
                        ),
                        fontWeight = FontWeight.Bold,
                    )
                },
                placeholder = {
                    Text(
                        text = "Valor",
                        style = TextStyle(
                            fontSize = 18.sp,
                        ),
                        fontWeight = FontWeight.Bold,
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
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
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(modifier = Modifier.height(60.dp), onClick = {

//                        var testeComparacao1: String = selectedImageUri.toString()
                    Log.i(
                        TAG,
                        "AlterarProdutoTela: selectedImageUri ANTES DE ALTERAR IMAGEM: $selectedImageUri"
                    )
                    singlePhotoPickerLaucher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                    fotoAlterada = true

                }) {
                    Text(
                        text = "Clique para alterar foto", fontStyle = FontStyle.Normal,
                        style = TextStyle(
                            fontSize = 16.sp,
                        ),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .padding(bottom = 20.dp),
                    contentScale = ContentScale.Crop,
                    )
            }
        }
        item {

            Button(
                enabled = estadoCampoDeTextoDescricao.value.text.isNotEmpty() && estadoCampoDeTextoValor.value.text.isNotEmpty() && selectedImageUri != null,
                onClick = {
                    Log.i("AlterarProduto", "Botao Alterar")
                    val descricao = estadoCampoDeTextoDescricao.value.text
                    val valor = estadoCampoDeTextoValor.value.text.toDoubleOrNull()
                    val fotoUri = selectedImageUri

                    Log.i(
                        TAG,
                        "AlterarProdutoTela: Descricao $descricao, valor $valor, fotoUri $fotoUri"
                    )

                    if (descricao.isNotEmpty() && (idproduto != null) && (valor != null)) {
                        val produto = Produto(id = idproduto, descricao = descricao, preco = valor)

                        var resultadoRepository: LiveData<Boolean>
                        Log.i(TAG, "AlterarProdutoTela: fotoAlterada: $fotoAlterada")


                        coroutineScope.launch {
                            if (fotoAlterada) {

                                val fotoByteArray = withContext(Dispatchers.IO) {
//                              TODO: remover esse código para uma função específica ->  getByteArrayFromUri(fotoUri)
                                    val inputStream =
                                        contexto.contentResolver.openInputStream(selectedImageUri!!)
                                    inputStream?.readBytes()
                                }

                                Log.i(TAG, "AlterarProdutoTela: fotoByteArray: $fotoByteArray")
                                resultadoRepository = repository.editarProduto(produto, fotoByteArray)

                                Log.i(TAG, "AlterarProdutoTela: IF ${resultadoRepository.value}")
                            } else {
                                resultadoRepository = repository.editarProduto(produto)

                                Log.i(TAG, "AlterarProdutoTela: ELSE ${resultadoRepository.value}")
                            }

                            if (resultadoRepository.value == true) {
                                    Toast.makeText(
                                        contexto,
                                        "Produto ${produto.descricao} alterado com sucesso",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    Log.i(
                                        "TelaAlterar Inserir",
                                        "Produto ${produto.descricao} alterado com sucesso"
                                    )
                                } else {
                                    // TODO Lidar com a falha ao obter o ByteArray da imagem
                                    Toast.makeText(
                                        contexto,
                                        "Houve algum erro ao salvar o produto ${produto.descricao}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    Log.e(
                                        "TelaAlterar Inserir",
                                        "Erro ao alterar produto ${produto.descricao}  ",
                                        Exception("Erro ao alterar produto ${produto.descricao} ")
                                    )
                                }
                        }
                    } else {
                        // Lidar com dados inválidos
                        Log.e("TelaProduto Inserir", "Erro ao inserir")
                    }
                    Log.i("TelaProduto", "Botao Alterar")
                },
                modifier = Modifier.width(300.dp)
            ) {
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
                 ->
                Log.i("TelaProduto", "Botao Voltar Produto")
                activity?.finish()


            }
                , modifier = Modifier.width(300.dp)) {
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