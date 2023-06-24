package com.example.projetofinal.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.projetofinal.models.Produto
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

private const val COLECAO_FIRESTORE_PRODUTOS = "produtos"

class ProdutoRepository(
    private val firestore: FirebaseFirestore
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

    fun salva(produto: Produto): LiveData<Boolean> = MutableLiveData<Boolean>().apply {
        val produtoDocumento = ProdutoDocumento(
            descricao = produto.descricao,
            valor = produto.preco.toDouble(),
            foto = produto.foto,
        )

        val colecao = firestore.collection(COLECAO_FIRESTORE_PRODUTOS)
        val documento = produto.id?.let { id ->
            colecao.document(id)
        } ?: colecao.document()

        value = true
    }

    fun buscaTodos(): LiveData<List<Produto>> = MutableLiveData<List<Produto>>().apply {
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
    }

    fun remove(produtoId: String): LiveData<Boolean> = MutableLiveData<Boolean>().apply {
        firestore.collection(COLECAO_FIRESTORE_PRODUTOS)
            .document(produtoId)
            .delete()
        value = true
    }


    suspend fun editar(id: String, produtoAlterado: Produto) {
        val document = firestore.collection(COLECAO_FIRESTORE_PRODUTOS).document(id)
        val produtoAlteradoDocumento = ProdutoDocumento(descricao = produtoAlterado.descricao, foto=produtoAlterado.foto, valor=produtoAlterado.preco)
        document.set(produtoAlteradoDocumento).await()
    }

    private fun converteParaProduto(documento: DocumentSnapshot): Produto? =
        documento.toObject<ProdutoDocumento>()?.paraProduto(documento.id)

    private class ProdutoDocumento(
        val id: String = "",
        val descricao: String = "",
        val valor: Double = 0.0,
        val foto:String? = null,
    ) {

        fun paraProduto(id: String): Produto = Produto(
            id = id,
            descricao = descricao,
            preco = valor,
            foto = foto,
        )
    }
}
