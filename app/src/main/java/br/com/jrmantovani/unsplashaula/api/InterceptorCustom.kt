package br.com.jrmantovani.unsplashaula.api

import okhttp3.Interceptor
import okhttp3.Response

class InterceptorCustom: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val requisicaoBuilder = chain.request().newBuilder()
        val requisicao =  requisicaoBuilder.addHeader("Authorization","Client-ID ${Credencias.CLIENT_ID}").build()

        return chain.proceed(requisicao)
    }

}