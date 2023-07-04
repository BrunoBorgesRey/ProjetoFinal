package com.example.projetofinal.models
import java.util.Date
data class Pedido(
    var id: String = "",
    var data: Date,
    var cliente: String,
    var listaProduto: MutableList<Produto> = mutableListOf()
) {
    constructor() : this("", Date(), "", mutableListOf())
}

