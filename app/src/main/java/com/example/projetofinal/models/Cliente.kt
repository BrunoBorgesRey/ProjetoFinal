package com.example.projetofinal.models

import java.util.Date

data class Cliente(
    var id: String = "",
    val cpf: String,
    val nome: String,
    val telefone: String,
    val endereco: String,
    val instagram: String
) {
}