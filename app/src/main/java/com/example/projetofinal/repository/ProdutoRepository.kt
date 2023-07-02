package com.example.projetofinal.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.projetofinal.models.Cliente
import com.example.projetofinal.models.Pedido
import com.example.projetofinal.models.Produto
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import kotlin.math.log

private const val COLECAO_FIRESTORE_PRODUTOS = "produtos"
private const val COLECAO_FIRESTORE_CLIENTES = "clientes"
private const val COLECAO_FIRESTORE_PEDIDOS = "pedidos"


private const val TAG = "Repository"

class ProdutoRepository(
    private val firestore: FirebaseFirestore, private val storage: FirebaseStorage
) {

//    fun buscaPorId(id: String): LiveData<Produto> = MutableLiveData<Produto>().apply {
//        firestore.collection(COLECAO_FIRESTORE_PRODUTOS)
//            .document(id)
//            .addSnapshotListener { s, _ ->
//                s?.let { documento ->
//                    converteParaProduto(documento)
//                        ?.let { produto ->
//                            value = produto
//                        }
//                }
//            }
//    }

    suspend fun buscaPorId(id: String, callback: (Produto?) -> Unit) {

        firestore.collection(COLECAO_FIRESTORE_PRODUTOS).document(id).get()
            .addOnSuccessListener { document ->
                val produto = converteParaProduto(document)
                callback(produto)
            }.addOnFailureListener { exception ->
                Log.e(TAG, "Erro ao buscar o produto por ID", exception)
                callback(null)
            }
    }

    suspend fun buscaPorIdCliente(id: String, callback: (Cliente?) -> Unit) {

        firestore.collection(COLECAO_FIRESTORE_CLIENTES).document(id).get()
            .addOnSuccessListener { document ->
                val cliente = converteParaCliente(document)
                callback(cliente)
            }.addOnFailureListener { exception ->
                Log.e(TAG, "Erro ao buscar o cliente por ID", exception)
                callback(null)
            }
    }

    suspend fun enviaImagem(referencia: String, produtoId: String, foto: ByteArray): LiveData<Boolean> =
        MutableLiveData<Boolean>().apply {

            Log.i(TAG, "enviaImagem: enviando a imagem $foto, produto id $produtoId para o storage")
            try {

                if(produtoId.isEmpty() || foto.isEmpty()) {
                    Log.e(TAG, "Não é possível salar imagem. Motivo: ProdutoId ou foto vazios", Exception("Não é possível salar imagem. Motivo: ProdutoId ou foto vazios"))
//                    throw Exception("Não é possível salar imagem. Motivo: ProdutoId ou foto vazios")
                }

                val referencia = storage.reference.child("produtos/$produtoId.jpg")
                Log.i(TAG, "enviaImagem: referencia do firestore  $referencia")

                referencia.putBytes(foto).await()
                val url = referencia.downloadUrl.await()

                val documento = firestore.collection(COLECAO_FIRESTORE_PRODUTOS).document(produtoId)
                documento.update("foto", url.toString()).await()

                value = true
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao enviar Imagem: falha ao enviar a imagem ${foto.toString()}, produto id $produtoId", e)
                value = false
            }
        }

    suspend fun salva(produto: Produto, foto: ByteArray): LiveData<Boolean> =
        MutableLiveData<Boolean>().apply {

            Log.i("ProdutoRepository", "recebendo o produto $produto e a foto $foto")
            val colecao = firestore.collection(COLECAO_FIRESTORE_PRODUTOS)

            val produtoMapeado = mapOf<String, Any>(
                "id" to produto.id,
                "descricao" to produto.descricao,
                "preco" to produto.preco.toDouble(),
            )

            Log.i("ProdutoRepository", "produtoMapeado: ${produtoMapeado}")
            produto.id = UUID.randomUUID().toString()
            Log.i(TAG, "salva: produto ${produto.toString()}")
            var docSalvo: Boolean = false
            val referencia = colecao.add(produtoMapeado)

            referencia.addOnSuccessListener {
                    docSalvo = true
                    Log.d("FireStore", "save: produto salvo")
                }.addOnFailureListener { e ->
                    Log.w("FireStore", "save: produto erro ${e}")
                    docSalvo = false
                }
            val imagemSalva = enviaImagem(referencia.toString(), produto.id, foto)
            value = docSalvo && imagemSalva.value == true
        }

    suspend fun editar(produtoAlterado: Produto, foto: ByteArray) =
        MutableLiveData<Boolean>().apply {
            val documento =
                firestore.collection(COLECAO_FIRESTORE_PRODUTOS).document(produtoAlterado.id)
            val produtoAlteradoDocumento = ProdutoDocumento(
//            id = produtoAlterado.id,
                descricao = produtoAlterado.descricao,
//            foto = produtoAlterado.foto,
                preco = produtoAlterado.preco
            )

            val produtoMapeado = mapOf<String, Any>(
                "descricao" to produtoAlteradoDocumento.descricao,
                "preco" to produtoAlteradoDocumento.preco.toDouble(),
            )

            var docSalvo: Boolean = false
           val referencia = documento.update(produtoMapeado).addOnSuccessListener {
                    docSalvo = true
                    Log.d("FireStore", "save: produto salvo")

                }.addOnFailureListener { e ->
                    Log.w("FireStore", "save: produto erro ${e}")
                    docSalvo = false
                }

            val imagemSalva = enviaImagem(referencia,  produtoAlterado.id, foto)

            value = docSalvo && imagemSalva.value == true
        }

    suspend fun salvarcliente(cliente: Cliente): LiveData<Boolean> =
        MutableLiveData<Boolean>().apply {
            Log.i("ClienteRepository", "recebendo o cliente $cliente")
            val colecao = firestore.collection(COLECAO_FIRESTORE_CLIENTES)
            cliente.id = UUID.randomUUID().toString()
            Log.i("ProdutoRepository", "Informações do ID $cliente.id")
            val clienteMapeado = mapOf<String, Any>(
                "id" to cliente.id,
                "cpf" to cliente.cpf,
                "nome" to cliente.nome,
                "telefone" to cliente.telefone,
                "endereco" to cliente.endereco,
                "instagram" to cliente.instagram,
            )

            Log.i("ProdutoRepository", "produtoMapeado: ${clienteMapeado}")
            colecao.document(cliente.id).set(clienteMapeado).addOnSuccessListener {
                    Log.d("FireStore", "save: produto salvo")
                }.addOnFailureListener { e ->
                    Log.w("FireStore", "save: produto erro ${e}")
                }



            value = true
        }

    suspend fun salvarpedido(pedido: Pedido): LiveData<Boolean> = MutableLiveData<Boolean>().apply {
        Log.i("pedidoRepository", "recebendo o pedido $pedido")
        val colecao = firestore.collection(COLECAO_FIRESTORE_PEDIDOS)
//            pedido.id = UUID.randomUUID().toString()
        Log.i("ProdutoRepository", "Informações do ID $pedido.id")
        val produtoMapeado = mapOf<String, Any>(
            "id" to pedido.id,
            "cliente" to pedido.cliente,
            "produtos" to pedido.listaProduto,

            )

        Log.i("ProdutoRepository", "produtoMapeado: ${produtoMapeado}")
        colecao.document(pedido.id).set(produtoMapeado).addOnSuccessListener {
                Log.d("FireStore", "save: produto salvo")
            }.addOnFailureListener { e ->
                Log.w("FireStore", "save: produto erro ${e}")
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
                    Log.e("buscaTodos", "Erro ao buscar clientes: ${exception.message}")
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
        firestore.collection(COLECAO_FIRESTORE_PRODUTOS).document(produtoId).delete()
        value = true
    }

    fun removeCliente(clienteId: String): LiveData<Boolean> = MutableLiveData<Boolean>().apply {
        firestore.collection(COLECAO_FIRESTORE_CLIENTES).document(clienteId).delete()
        value = true
    }


    suspend fun editarCliente(id: String, clienteAlterado: Cliente) {
        val document = firestore.collection(COLECAO_FIRESTORE_CLIENTES).document(id)
        val clienteAlteradoDocumento = ClienteDocumento(
            id = clienteAlterado.id,
            cpf = clienteAlterado.cpf,
            nome = clienteAlterado.nome,
            telefone = clienteAlterado.telefone,
            endereco = clienteAlterado.endereco,
            instagram = clienteAlterado.instagram
        )
        document.set(clienteAlteradoDocumento).await()
    }

    private fun converteParaProduto(documento: DocumentSnapshot): Produto? =
        documento.toObject<ProdutoDocumento>()?.paraProduto(documento.id)

    private fun converteParaCliente(documento: DocumentSnapshot): Cliente? =
        documento.toObject<ClienteDocumento>()?.paraCliente(documento.id)

    private class ProdutoDocumento(
        val id: String = "",
        val descricao: String = "",
        val preco: Double = 0.0,
        val foto: String? = null,
    ) {

        fun paraProduto(id: String): Produto = Produto(
            id = id,
            descricao = descricao,
            preco = preco,
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
