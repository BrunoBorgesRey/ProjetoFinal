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
import java.util.Date
import java.util.UUID

private const val COLECAO_FIRESTORE_PRODUTOS = "produtos"
private const val COLECAO_FIRESTORE_CLIENTES = "clientes"
private const val COLECAO_FIRESTORE_PEDIDOS = "pedidos"


private const val TAG = "Repository"

class ProdutoRepository(
    private val firestore: FirebaseFirestore, private val storage: FirebaseStorage,
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

    suspend fun buscarDocumentoIdPorObjIdEColecao(
        idProduto: String,
        colecao: String,
        onComplete: (String?) -> Unit,
    ) {
        Log.i(TAG, "buscarDocumentoIdPorObjIdEColecao: idProduto $idProduto, colecao $colecao")


//        val db = FirebaseFirestore.getInstance()
//
//        db.collection(colecao)
//            .whereEqualTo("id", idProduto)
//            .get()
//            .addOnSuccessListener { result ->
//                if (!result.isEmpty) {
//                    val documentoId = result.documents[0].id
//                    onComplete(documentoId)
//                } else {
//                    onComplete(null)
//                }
//            }
//            .addOnFailureListener { e ->
//                // Ocorreu um erro ao buscar o documento
//                onComplete(null)
//            }

        Log.i(TAG, "buscarDocumentoIdPorObjIdEColecao: idProduto $idProduto, colecao $colecao")

        val db = FirebaseFirestore.getInstance()

        try {
            val result = db.collection(colecao)
                .whereEqualTo("id", idProduto)
                .get()
                .await()

            Log.i(TAG, "buscarDocumentoIdPorObjIdEColecao: RESULT $result")

            if (!result.isEmpty) {
                val documentoId = result.documents[0].id

                Log.i(TAG, "buscarDocumentoIdPorObjIdEColecao: documentoId $documentoId")
                onComplete(documentoId)
            } else {
                onComplete(null)
            }
        } catch (e: Exception) {
            // Ocorreu um erro ao buscar o documento
            onComplete(null)
        }
    }

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

    suspend fun enviaImagem(
        docRefFireStore: String,
        produtoId: String,
        foto: ByteArray,
    ): LiveData<Boolean> =
        MutableLiveData<Boolean>().apply {

            Log.i(
                "PRODUTO REPOSITORY STORAGE",
                "enviaImagem: enviando a imagem $foto, produto id $produtoId para o storage"
            )
            try {

                if (produtoId.isEmpty() || foto.isEmpty()) {
                    Log.e(
                        "PRODUTO REPOSITORY STORAGE",
                        "Não é possível salar imagem. Motivo: ProdutoId ou foto vazios",
                        Exception("Não é possível salar imagem. Motivo: ProdutoId ou foto vazios")
                    )
//                    throw Exception("Não é possível salar imagem. Motivo: ProdutoId ou foto vazios")
                }

                val referenciaStorage = storage.reference.child("produtos/$produtoId.jpg")
                Log.i(
                    "PRODUTO REPOSITORY STORAGE",
                    "enviaImagem: referencia do firestore  $referenciaStorage"
                )

                referenciaStorage.putBytes(foto).await()
                val url = referenciaStorage.downloadUrl.await()

                val documento =
                    firestore.collection(COLECAO_FIRESTORE_PRODUTOS).document(docRefFireStore)
                documento.update("foto", url.toString()).await()

                value = true
            } catch (e: Exception) {
                Log.e(
                    "PRODUTO REPOSITORY STORAGE",
                    "Erro ao enviar Imagem: falha ao enviar a imagem ${foto.toString()}, produto id $produtoId",
                    e
                )
                value = false
            }
        }

    suspend fun salvarProduto(produto: Produto, foto: ByteArray): LiveData<Boolean> =
        MutableLiveData<Boolean>().apply {

            Log.i("ProdutoRepository", "recebendo o produto $produto e a foto $foto")
//            val colecao = firestore.collection(COLECAO_FIRESTORE_PRODUTOS)

            produto.id = UUID.randomUUID().toString()
            val produtoMapeado = mapOf<String, Any>(
                "id" to produto.id,
                "descricao" to produto.descricao,
                "preco" to produto.preco.toDouble(),
            )

            Log.i("ProdutoRepository", "produtoMapeado: ${produtoMapeado}")

            Log.i(TAG, "salva: produto ${produto.toString()}")

//            var docSalvo: Boolean = false
//            val referencia = colecao.add(produtoMapeado)
//            referencia.addOnSuccessListener {
//                    docSalvo = true
//                    Log.d("FireStore", "save: produto salvo")
//                }.addOnFailureListener { e ->
//                    Log.w("FireStore", "save: produto erro ${e}")
//                    docSalvo = false
//                }
//
//            val documentReference = referencia.result
//            Log.i(TAG, "salva: documentReference ${documentReference.path}, $documentReference")
//            val imagemSalva = enviaImagem(documentReference.path, produto.id, foto)
//            value = docSalvo && imagemSalva.value == true

//            var id: String = ""
            var docSalvo: Boolean = false
//            var documentReference: DocumentReference? = null
//            var path: String = ""
//            var id: String = ""

//            var imagemSalva : any? = null

//            val id = colecao.add(produtoMapeado)
//                .addOnSuccessListener { document ->
//                    docSalvo = true
//                    Log.i(
//                        TAG,
//                        "salva DENTRO DO ADD: id do documento firestore ${document.id}, "
//                    )
//                    Log.d("FireStore", "save: produto salvo")
//
//                }.addOnFailureListener { e ->
//                    Log.w("FireStore", "save: produto erro ${e}")
//                    docSalvo = false
//                }.await().id
//
//
//            while (id.isEmpty()) {
//                delay(100)
//            }
//            val documentId = (++documentIdProdutoFiretore.id).toString()
//            colecao.document(documentId).set(produtoMapeado).addOnSuccessListener {
//                Log.d("FireStore", "save: produto salvo")
//            }.addOnFailureListener { e ->
//                Log.w("FireStore", "save: produto erro ${e}")
//            }

            val db = FirebaseFirestore.getInstance()
            Log.i("PRODUTO REPOSITORY FireStore", "salvarProduto: db $db")
            val produtosCollection = db.collection("produtos")
            Log.i(
                "PRODUTO REPOSITORY FireStore",
                "salvarProduto: produtosCollection $produtosCollection"
            )
            val novoDocumento = produtosCollection.document()
            Log.i(
                "PRODUTO REPOSITORY FireStore",
                "salvarProduto: novoDocumento $novoDocumento novoDocumentoId ${novoDocumento?.id} novoDocumentoPaht  ${novoDocumento?.path} novoDocumentoParent ${novoDocumento.parent} novoDocumentoGet ${novoDocumento.parent?.get()} ---------"
            )

            novoDocumento.set(produtoMapeado).addOnSuccessListener {
                docSalvo = true
                Log.d("\"PRODUTO REPOSITORY FireStore\"", "save: produto salvo")
            }.addOnFailureListener { e ->
                docSalvo = false
                Log.w("\"PRODUTO REPOSITORY FireStore\"", "save: produto erro ${e}")
            }

            val imagemSalva = enviaImagem(novoDocumento.id, produto.id, foto).value
            Log.i("PRODUTO REPOSITORY FireStore", "salva: imagemSalva ${imagemSalva}")
            value = docSalvo && imagemSalva == true
        }


    suspend fun editarProduto(produtoAlterado: Produto, fotoByteArray: ByteArray? = null) =
        MutableLiveData<Boolean>().apply {
            var docSalvo: Boolean = false
            var imagemSalva: LiveData<Boolean> = MutableLiveData<Boolean>().apply {
                value = true
            }
            val documento =
                firestore.collection(COLECAO_FIRESTORE_PRODUTOS).document(produtoAlterado.id)

            val produtoAlteradoDocumento = ProdutoDocumento(
                descricao = produtoAlterado.descricao,
                preco = produtoAlterado.preco
            )

            val produtoMapeado = mapOf<String, Any>(
                "descricao" to produtoAlteradoDocumento.descricao,
                "preco" to produtoAlteradoDocumento.preco.toDouble(),
            )

            documento.update(produtoMapeado).addOnSuccessListener {
                docSalvo = true
                Log.d("PRODUTO REPOSITORY FireStore", "save: produto salvo")
            }.addOnFailureListener { e ->
                Log.w("PRODUTO REPOSITORY FireStore", "save: produto erro ${e}")
                throw IllegalArgumentException("Erro ao salvar produto, tente novamente, $e")

                docSalvo = false
            }

            if (fotoByteArray != null) {
                imagemSalva =
                    enviaImagem(
                        documento.id,
                        produtoAlterado.id,
                        fotoByteArray
                    )
                if (imagemSalva.value == false) {
                    throw IllegalArgumentException("Erro ao salvar imagem, tente novamente")
                }
            }

            value = docSalvo == true
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


    suspend fun salvarPedido(pedido: Pedido): LiveData<Boolean> = MutableLiveData<Boolean>().apply {
        Log.i("pedidoRepository", "recebendo o pedido $pedido")
        val colecao = firestore.collection(COLECAO_FIRESTORE_PEDIDOS)
        pedido.id = UUID.randomUUID().toString()
        Log.i("ProdutoRepository", "Informações do ID $pedido.id")
        val produtoMapeado = mapOf<String, Any>(
            "id" to pedido.id,
            "cliente" to pedido.cliente,
            "listaProduto" to pedido.listaProduto,
            "data" to pedido.data,
        )

        Log.i("ProdutoRepository", "produtoMapeado: ${produtoMapeado}")
        colecao.document(pedido.id).set(produtoMapeado).addOnSuccessListener {
            Log.d("FireStore", "save: produto salvo")
        }.addOnFailureListener { e ->
            Log.w("FireStore", "save: produto erro ${e}")
        }


        value = true
    }

    fun buscaTodosProdutos(): LiveData<List<Produto>> {
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

    fun buscaTodosPedido(): LiveData<List<Pedido>> {
        val liveData = MutableLiveData<List<Pedido>>()
        firestore.collection(COLECAO_FIRESTORE_PEDIDOS)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("buscaTodos", "Erro ao buscar pedidos: ${exception.message}")
                    return@addSnapshotListener
                }
                snapshot?.let { snapshot ->
                    val pedidos: List<Pedido> = snapshot.documents.mapNotNull { documento ->
                        converteParaPedido(documento)
                    }
                    liveData.value = pedidos
                    Log.i("ListaPedidosRepositorio", "pedidos:  ${pedidos}")
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

    fun removePedido(pedidoId: String): LiveData<Boolean> = MutableLiveData<Boolean>().apply {
        firestore.collection(COLECAO_FIRESTORE_PEDIDOS).document(pedidoId).delete()
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



    suspend fun editarPedido(id: String, pedidoAlterado: Pedido) {
        val document = firestore.collection(COLECAO_FIRESTORE_PEDIDOS).document(id)
        val pedidoAlteradoDocumento = PedidoDocumento(
            id = pedidoAlterado.id,
            cliente = pedidoAlterado.cliente,
            data = pedidoAlterado.data,
            listaProduto = pedidoAlterado.listaProduto

        )
        document.set(pedidoAlteradoDocumento).await()
    }

    private fun converteParaProduto(documento: DocumentSnapshot): Produto? =
        documento.toObject<ProdutoDocumento>()?.paraProduto(documento.id)

    private fun converteParaCliente(documento: DocumentSnapshot): Cliente? =
        documento.toObject<ClienteDocumento>()?.paraCliente(documento.id)

    private fun converteParaPedido(documento: DocumentSnapshot): Pedido? =
        documento.toObject<PedidoDocumento>()?.paraPedido(documento.id)

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

    private class PedidoDocumento(
        val id: String = "",
        val cliente: String = "",
        val data: Date,
        val listaProduto: MutableList<Produto>,

        ) {
        constructor() : this("", "", Date(), mutableListOf())
        fun paraPedido(id: String): Pedido = Pedido(
            id = id,
            cliente = cliente,
            data = data,
            listaProduto = listaProduto
        )
    }
}
