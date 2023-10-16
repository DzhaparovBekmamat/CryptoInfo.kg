package com.template.flowwithretrofit.api

import com.template.flowwithretrofit.response.ResponseCoinsMarkets
import com.template.flowwithretrofit.response.ResponseDetailsCoin
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// Это интерфейс, который определяет конечные точки API и их соответствующие параметры.
interface ApiServices {

    // Это GET-запрос для получения данных о рынке монет.
    // Он включает параметр запроса "vs_currency" для указания валюты для сравнения.
    @GET("coins/markets?sparkline=true")
    suspend fun getCoinsMarket(@Query("vs_currency") vs_currency: String): Response<ResponseCoinsMarkets>

    // Это GET-запрос для получения подробной информации о монете по её уникальному идентификатору (id).
    // Также включает параметр "sparkline=true" для получения данных о графике.
    @GET("coins/{id}?sparkline=true")
    suspend fun getDetailsCoin(@Path("id") id: String): Response<ResponseDetailsCoin>
}
