package com.template.flowwithretrofit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.template.flowwithretrofit.repository.ApiRepository
import com.template.flowwithretrofit.response.ResponseCoinsMarkets
import com.template.flowwithretrofit.response.ResponseDetailsCoin
import com.template.flowwithretrofit.utils.DataStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {
    // LiveData для списка криптовалют
    private val _coinsList =
        MutableLiveData<DataStatus<List<ResponseCoinsMarkets.ResponseCoinsMarketsItem>>>()
    val coinsList: LiveData<DataStatus<List<ResponseCoinsMarkets.ResponseCoinsMarketsItem>>>
        get() = _coinsList

    // Функция для получения списка криптовалют
    fun getCoinsList(vs_currency: String) = viewModelScope.launch {
        repository.getCoinsList(vs_currency).collect {
            _coinsList.value = it
        }
    }

    // LiveData для деталей криптовалюты
    private val _detailsCoin = MutableLiveData<DataStatus<ResponseDetailsCoin>>()
    val detailsCoin: LiveData<DataStatus<ResponseDetailsCoin>>
        get() = _detailsCoin

    // Функция для получения деталей криптовалюты по её идентификатору
    fun getDetailsCoin(id: String) = viewModelScope.launch {
        repository.getDetailsCoin(id).collect {
            _detailsCoin.value = it
        }
    }
}
