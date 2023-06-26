package com.example.projetofinal.models

data class Produto(
    var id: String = "",
    var descricao: String,
    var preco: Double,
    var foto: String? = null, //A princípio, a tela de produto está aceitando selecionar mais de 1 foto.
) {
}
