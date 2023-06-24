package com.example.projetofinal.models

data class Produto(
    val id: String,
    val descricao: String,
    val preco: Double,
    val foto: String? = null,
) {
}
