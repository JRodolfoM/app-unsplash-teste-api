package br.com.jrmantovani.unsplashaula.model

data class RespostaPesquisaFotos(
    val results: List<RespostaFotosItem>,
    val total: Int,
    val total_pages: Int
)