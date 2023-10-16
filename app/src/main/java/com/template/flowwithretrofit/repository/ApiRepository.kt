package com.template.flowwithretrofit.repository

import com.template.flowwithretrofit.api.ApiServices
import com.template.flowwithretrofit.utils.DataStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

// Этот класс служит репозиторием для выполнения запросов к API и обработки потока данных.
class ApiRepository @Inject constructor(private val apiServices: ApiServices) {

    // Эта функция извлекает список монет из API.
    suspend fun getCoinsList(vs_currency: String) = flow {
        // Генерируем состояние "загрузка", чтобы уведомить наблюдателей, что данные загружаются.
        emit(DataStatus.loading())

        // Делаем сетевой запрос для получения списка монет.
        val result = apiServices.getCoinsMarket(vs_currency)

        // Обрабатываем различные HTTP-коды ответа и генерируем соответствующее состояние DataStatus.
        when (result.code()) {
            200 -> emit(DataStatus.success(result.body()))
            400 -> emit(DataStatus.error(result.message()))
            500 -> emit(DataStatus.error(result.message()))
        }
    }.catch {
        // Если происходит исключение во время выполнения потока, генерируем состояние ошибки.
        emit(DataStatus.error(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    // Эта функция получает подробности о конкретной монете по ее уникальному идентификатору.
    suspend fun getDetailsCoin(id: String) = flow {
        // Делаем сетевой запрос для получения подробностей о монете.
        val result = apiServices.getDetailsCoin(id)

        // Обрабатываем различные HTTP-коды ответа и генерируем соответствующее состояние DataStatus.
        when (result.code()) {
            200 -> emit(DataStatus.success(result.body()))
            400 -> emit(DataStatus.error(result.message()))
            500 -> emit(DataStatus.error(result.message()))
        }
    }.catch {
        // Если происходит исключение во время выполнения потока, генерируем состояние ошибки.
        emit(DataStatus.error(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}
