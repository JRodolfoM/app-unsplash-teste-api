package br.com.jrmantovani.unsplashaula.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitService {
    private const val BASE_URL = "https://api.unsplash.com/"


    fun <T> getAPI(classe: Class<T>): T {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHTTP())
            .build()
            .create(classe)
    }

    private fun getOkHTTP(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(InterceptorCustom())
            .build()
    }
}
