package com.template.flowwithretrofit.utils

data class DataStatus<out T>(val status: Status, val data: T? = null, val message: String? = null) {
    // Статусы загрузки данных
    enum class Status {
        LOADING, SUCCESS, ERROR
    }

    companion object {
        // Функция создания объекта с статусом "Загрузка"
        fun <T> loading(): DataStatus<T> {
            return DataStatus(Status.LOADING)
        }

        // Функция создания объекта с статусом "Успех" и данными
        fun <T> success(data: T?): DataStatus<T> {
            return DataStatus(Status.SUCCESS, data)
        }

        // Функция создания объекта с статусом "Ошибка" и сообщением об ошибке
        fun <T> error(error: String): DataStatus<T> {
            return DataStatus(Status.ERROR, message = error)
        }
    }
}
