package com.example.projetofinal.models

import java.util.Date

data class Pedido(
    val id: String,
    val data: Date,
    val cliente: Cliente,
    val listaProduto: MutableList<Produto>,
) {}

