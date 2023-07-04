package com.example.projetofinal.models
data class Produto(
    var id: String = "",
    var descricao: String,
    var preco: Double,
    var foto: String? = null
) {
    constructor() : this("", "", 0.0, null)
}
