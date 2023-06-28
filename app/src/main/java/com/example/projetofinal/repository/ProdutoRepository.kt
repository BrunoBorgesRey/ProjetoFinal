package com.example.projetofinal.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.projetofinal.models.Cliente
import com.example.projetofinal.models.Produto
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID

private const val COLECAO_FIRESTORE_PRODUTOS = "produtos"
private const val COLECAO_FIRESTORE_CLIENTES = "clientes"

class ProdutoRepository(
    private val firestore: FirebaseFirestore, private val storage: FirebaseStorage
) {

    fun buscaPorId(id: String): LiveData<Produto> = MutableLiveData<Produto>().apply {
        firestore.collection(COLECAO_FIRESTORE_PRODUTOS)
            .document(id)
            .addSnapshotListener { s, _ ->
                s?.let { documento ->
                    converteParaProduto(documento)
                        ?.let { produto ->
                            value = produto
                        }
                }
            }
    }

//    suspend fun enviaImagem(produtoId: String, foto: ByteArray) {
//        GlobalScope.launch {
//            try {
//                val documento = firestore.collection(COLECAO_FIRESTORE_PRODUTOS)
//                    .document(produtoId)
//
//                val referencia = storage.reference.child("produtos/$produtoId.jpg")
//                referencia.putBytes(foto).await()
//
//                val url = referencia.downloadUrl.await()
//
//                documento
//                    .update(mapOf("foto" to url.toString()))
//                    .await()
//            } catch (e: Exception) {
//                Log.e("ENVIAIMAGEM", "enviaImagem: falha ao enviar a imagem", e)
//            }
//        }
//    }
//    suspend fun salva(produto: Produto, foto: ByteArray): LiveData<Boolean> = MutableLiveData<Boolean>().apply {
//        val produtoDocumento = ProdutoDocumento(
//            descricao = produto.descricao,
//            valor = produto.preco.toDouble(),
//            foto = produto.foto,
//        )
//
//        val colecao = firestore.collection(COLECAO_FIRESTORE_PRODUTOS)
//        val documento = produto.id?.let { id ->
//            colecao.document(id)
//        } ?: colecao.document()
//
//        enviaImagem(documento.id, foto)
//        value = true
//    }

    suspend fun enviaImagem(produtoId: String, foto: ByteArray) {
        try {
            val referencia = storage.reference.child("produtos/$produtoId.jpg")

            referencia.putBytes(foto).await()
            val url = referencia.downloadUrl.await()

            val documento = firestore.collection(COLECAO_FIRESTORE_PRODUTOS)
                .document(produtoId)
            documento
                .update("foto", url.toString())
                .await()

            return
        } catch (e: Exception) {
            Log.e("ENVIAIMAGEM", "enviaImagem: falha ao enviar a imagem", e)
        }
    }

    suspend fun salva(produto: Produto, foto: ByteArray): LiveData<Boolean> =
        MutableLiveData<Boolean>().apply {
            Log.i("ProdutoRepository", "recebendo o produto $produto e a foto $foto")

            val colecao = firestore.collection(COLECAO_FIRESTORE_PRODUTOS)
//            val documento = produtoDocumento.id?.let { id ->
//                Log.i("ProdutoRepository","Valor id $id")
//                colecao.document(id)
//            } ?: colecao.document()

            produto.id = UUID.randomUUID().toString()
            Log.i("ProdutoRepository", "Informações do ID $produto.id")
            val produtoMapeado = mapOf<String, Any>(
                "id" to produto.id,
                "descricao" to produto.descricao,
                "preco" to produto.preco.toDouble(),

                )

            Log.i("ProdutoRepository", "produtoMapeado: ${produtoMapeado}")
            colecao.document(produto.id)
                .set(produtoMapeado)
                .addOnSuccessListener {
                    Log.d("FireStore", "save: produto salvo")
                }.addOnFailureListener {
                   e -> Log.w("FireStore", "save: produto erro ${e}")
                }

            enviaImagem(produto.id, foto)
            value = true
        }

    suspend fun salvarcliente(cliente: Cliente): LiveData<Boolean> =
        MutableLiveData<Boolean>().apply {
            Log.i("ClienteRepository", "recebendo o cliente $cliente")
            val colecao = firestore.collection(COLECAO_FIRESTORE_CLIENTES)
            cliente.id = UUID.randomUUID().toString()
            Log.i("ProdutoRepository", "Informações do ID $cliente.id")
            val produtoMapeado = mapOf<String, Any>(
                "id" to cliente.id,
                "descricao" to cliente.cpf,
                "nome" to cliente.nome,
                "telefone" to cliente.telefone,
                "endereco" to cliente.endereco,
                "instagram" to cliente.instagram,
                )

            Log.i("ProdutoRepository", "produtoMapeado: ${produtoMapeado}")
            colecao.document(cliente.id)
                .set(produtoMapeado)
                .addOnSuccessListener {
                    Log.d("FireStore", "save: produto salvo")
                }.addOnFailureListener {
                        e -> Log.w("FireStore", "save: produto erro ${e}")
                }


            value = true
        }
    fun buscaTodos(): LiveData<List<Produto>> {
        val liveData = MutableLiveData<List<Produto>>()
        firestore.collection(COLECAO_FIRESTORE_PRODUTOS)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("buscaTodos", "Erro ao buscar produtos: ${exception.message}")
                    return@addSnapshotListener
                }
                snapshot?.let { snapshot ->
                    val produtos: List<Produto> = snapshot.documents.mapNotNull { documento ->
                        converteParaProduto(documento)
                    }
                    liveData.value = produtos
                }
            }
        return liveData
    }

    fun buscaTodosCliente(): LiveData<List<Cliente>> {
        val liveData = MutableLiveData<List<Cliente>>()
        firestore.collection(COLECAO_FIRESTORE_CLIENTES)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("buscaTodos", "Erro ao buscar produtos: ${exception.message}")
                    return@addSnapshotListener
                }
                snapshot?.let { snapshot ->
                    val clientes: List<Cliente> = snapshot.documents.mapNotNull { documento ->
                        converteParaCliente(documento)
                    }
                    liveData.value = clientes
                }
            }
        return liveData
    }


    /*
     fun buscaTodos(): LiveData<List<Produto>> = MutableLiveData<List<Produto>>().apply {
        Log.i("buscaTodos", "Inicio")
        firestore.collection(COLECAO_FIRESTORE_PRODUTOS)
            .addSnapshotListener { s, _ ->
                s?.let { snapshot ->
                    val produtos: List<Produto> = snapshot.documents
                        .mapNotNull { documento ->
                            converteParaProduto(documento)
                        }
                    value = produtos

                }
            }
        Log.i("buscaTodos", "Fim")
    }*/

    fun remove(produtoId: String): LiveData<Boolean> = MutableLiveData<Boolean>().apply {
        firestore.collection(COLECAO_FIRESTORE_PRODUTOS)
            .document(produtoId)
            .delete()
        value = true
    }

    fun removeCliente(clienteId: String): LiveData<Boolean> = MutableLiveData<Boolean>().apply {
        firestore.collection(COLECAO_FIRESTORE_CLIENTES)
            .document(clienteId)
            .delete()
        value = true
    }

    suspend fun editar(id: String, produtoAlterado: Produto) {
        val document = firestore.collection(COLECAO_FIRESTORE_PRODUTOS).document(id)
        val produtoAlteradoDocumento = ProdutoDocumento(
            descricao = produtoAlterado.descricao,
            foto = produtoAlterado.foto,
            valor = produtoAlterado.preco
        )
        document.set(produtoAlteradoDocumento).await()
    }

    private fun converteParaProduto(documento: DocumentSnapshot): Produto? =
        documento.toObject<ProdutoDocumento>()?.paraProduto(documento.id)

    private fun converteParaCliente(documento: DocumentSnapshot): Cliente? =
        documento.toObject<ClienteDocumento>()?.paraCliente(documento.id)

    private class ProdutoDocumento(
        val id: String = "",
        val descricao: String = "",
        val valor: Double = 0.0,
        val foto: String? = null,
    ) {

        fun paraProduto(id: String): Produto = Produto(
            id = id,
            descricao = descricao,
            preco = valor,
            foto = foto,
        )
    }
    private class ClienteDocumento(
        val id: String = "",
        val cpf: String = "",
        val nome: String = "",
        val telefone: String = "",
        val endereco: String = "",
        val instagram: String = "",

    ) {

        fun paraCliente(id: String): Cliente = Cliente(
            id = id,
            cpf = cpf,
            nome = nome,
            telefone = telefone,
            endereco = endereco,
            instagram = instagram
        )
    }
}
