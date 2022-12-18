package br.com.jrmantovani.unsplashaula.api

import br.com.jrmantovani.unsplashaula.model.RespostaFotos
import br.com.jrmantovani.unsplashaula.model.RespostaPesquisaFotos
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashAPI {


    @GET("photos")
    suspend fun recuperarImagens(
        @Query("page") pagina: Int
    ) : Response<RespostaFotos>

    @GET("search/photos")
    suspend fun recuperarImagensPesquisa(
        @Query("page") pagina: Int,
        @Query("query") textoPesquisa: String
    ) : Response<RespostaPesquisaFotos>
}