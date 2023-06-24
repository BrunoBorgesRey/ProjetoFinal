package com.example.projetofinal

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage


//import androidx.compose.material.Button
//import androidx.compose.material.Text
//import androidx.compose.material.TextField
//import androidx.compose.material.TextFieldValue
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.input.KeyboardCapitalization
//import androidx.compose.ui.text.input.KeyboardOptions
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.TextUnit
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.core.net.toUri
//import com.google.accompanist.activity.result.contract.ActivityResultContracts
//import com.google.accompanist.activity.result.registerForActivityResult
//import com.google.accompanist.imageloading.ImageLoadState
//import com.google.accompanist.imageloading.LoadPainter
//import com.google.accompanist.imageloading.rememberLoadPainter
//import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.inappmessaging.FirebaseInAppMessaging
//import com.google.firebase.storage.FirebaseStorage
//import com.google.firebase.storage.ktx.storageMetadata
//import io.insert-koin.koin.androidx.compose.get
//import io.insert-koin.koin.androidx.compose.getViewModel
//import io.insert-koin.koin.androidx.compose.viewModel
//import io.insert-koin.koin.androidx.compose.withState
//import io.insert-koin.koin.java.KoinJavaComponent.inject
//import io.insert-koin.koin.java.get

class TelaProduto() : ComponentActivity() {
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
    val contexto = LocalContext.current
    val estadoCampoDeTextoIdProduto = remember { mutableStateOf(TextFieldValue()) }
    val estadoCampoDeTextoDescricao = remember { mutableStateOf(TextFieldValue()) }
    val estadoCampoDeTextoValor = remember { mutableStateOf(TextFieldValue()) }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    var singlePhotoPickerLaucher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {uri -> selectedImageUri = uri }
    )

    var multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = {uris -> selectedImageUris = uris }
    )


    val activity = (LocalContext.current as? Activity)

    LazyColumn(
        Modifier
            .padding(40.dp)
            .fillMaxSize(),
        verticalArrangement =  Arrangement.Center,
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
                value = estadoCampoDeTextoIdProduto.value,
                onValueChange = {
                    estadoCampoDeTextoIdProduto.value = it
                },
                placeholder = { Text(text = "Insira o Id Produto") },
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
                value = estadoCampoDeTextoDescricao.value,
                onValueChange = {
                    estadoCampoDeTextoDescricao.value = it
                },
                placeholder = { Text(text = "Insira a Descrição") },
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
                placeholder = { Text(text = "Insira o Valor") },
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
                    Text(text = "Selecione uma foto")
                }
                Button(onClick = {

                    multiplePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {
                    Text(text = "Selecione multiplas fotos")
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
//        item {
//
//        }

        item {
            Button(onClick = {
                Log.i("TesteProduto", "Botao Inserir")
            }, modifier = Modifier.width(300.dp)) {
                Text(text = "Inserir")
            }
            Spacer(modifier = Modifier.height(25.dp))
        }
        item {

            Button(onClick = {
                Log.i("TesteProduto", "Botao Listar")
            }, modifier = Modifier.width(300.dp)) {
                Text(text = "Listar")
            }
            Spacer(modifier = Modifier.height(25.dp))
        }
        item {
            Button(onClick = {
                Log.i("TesteProduto", "Botao Deletar")
            }, modifier = Modifier.width(300.dp)) {
                Text(text = "Deletar")
            }
            Spacer(modifier = Modifier.height(25.dp))
        }
        item {
            Button(onClick = {
                Log.i("TesteProduto", "Botao Alterar")
            }, modifier = Modifier.width(300.dp)) {
                Text(text = "Alterar")
            }
            Spacer(modifier = Modifier.height(25.dp))
        }
        item {
            Button(onClick = {
                Log.i("TesteProduto", "Botao Voltar Produto")
                contexto.startActivity(Intent(contexto, MainActivity::class.java))
            }, modifier = Modifier.width(300.dp)) {
                Text(text = "Voltar")
            }
        }

    }

}

@Preview(showSystemUi = true)
@Composable
fun ProdutosPreview() {
    Produtos()
}
