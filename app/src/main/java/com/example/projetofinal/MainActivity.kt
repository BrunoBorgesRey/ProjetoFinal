package com.example.projetofinal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Log.i("Teste","Comeca Menu")
            MenuPrincipal()
        }
    }
}


@Composable
fun MenuPrincipal() {
    val contexto = LocalContext.current
    Column( 
        Modifier.padding(40.dp)
    ) {
        Text(text = "Pé fedorento", textAlign = TextAlign.Center, modifier = Modifier.width(300.dp) )
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {
                Log.i("Teste","Botão Cliente")
                contexto.startActivity(Intent(contexto, TelaCliente::class.java))

            },
            modifier = Modifier.width(300.dp)) {
            Text(text = "Clientes")

        }
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {
                Log.i("Teste","Botão Produto")

            },
            modifier = Modifier.width(300.dp)) {
            Text(text = "Produtos")

        }
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {
                Log.i("Teste","Botão Pedidos")

            },
            modifier = Modifier.width(300.dp)) {
            Text(text = "Pedidos")

        }
    }
    
}

//@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

}