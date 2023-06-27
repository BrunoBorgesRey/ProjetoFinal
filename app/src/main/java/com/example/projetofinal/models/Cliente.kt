package com.example.projetofinal.models

data class Cliente(
    var id: String = "",
    val cpf: String,
    val nome: String,
    val telefone: String,
    val endereco: String,
    val instagram: String
) {
}