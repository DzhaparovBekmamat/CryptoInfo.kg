package com.template.flowwithretrofit.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.template.flowwithretrofit.R
import com.template.flowwithretrofit.databinding.ItemBinding
import com.template.flowwithretrofit.response.ResponseCoinsMarkets
import com.template.flowwithretrofit.utils.Constants
import com.template.flowwithretrofit.utils.roundToThreeDecimals
import com.template.flowwithretrofit.utils.toDoubleFloatPairs
import javax.inject.Inject

// Это объявление класса с именем CryptosAdapter, который расширяет RecyclerView.Adapter.
// Этот адаптер используется для заполнения RecyclerView данными.
class CryptosAdapter @Inject constructor() : RecyclerView.Adapter<CryptosAdapter.ViewHolder>() {

    // Эти переменные объявлены как lateinit для хранения ссылок на привязку элемента и контекст.
    private lateinit var binding: ItemBinding
    private lateinit var context: Context

    // Эта функция вызывается, когда RecyclerView нужен новый ViewHolder.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemBinding.inflate(inflater, parent, false)
        context = parent.context

        // Создаем новый ViewHolder и возвращаем его.
        return ViewHolder()
    }

    // Эта функция возвращает количество элементов в списке, которые будут отображаться в RecyclerView.
    override fun getItemCount(): Int = differ.currentList.size

    // Эта функция вызывается для привязки данных к ViewHolder в указанной позиции.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
        holder.setIsRecyclable(false)
    }

    // Внутренний класс ViewHolder, который расширяет RecyclerView.ViewHolder.
    // Он представляет собой шаблон для представления элемента списка.
    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {

        // Эта аннотация подавляет предупреждение о использовании setText с параметром, который не является строкой.
        @SuppressLint("SetTextI18n")

        // Эта функция привязывает данные элемента к представлению внутри ViewHolder.
        fun bind(item: ResponseCoinsMarkets.ResponseCoinsMarketsItem) {
            binding.apply {
                tvName.text = item.id
                tvSymbol.text = item.symbol?.uppercase()
                tvPrice.text = "€${item.currentPrice?.roundToThreeDecimals()}"

                // Загрузка изображения с использованием библиотеки Coil.
                imgCrypto.load(item.image) {
                    crossfade(true)
                    crossfade(500)
                    placeholder(R.drawable.round_currency_bitcoin_24)
                    error(R.drawable.round_currency_bitcoin_24)
                }

                // Настройка градиента для lineChart.
                lineChart.gradientFillColors = intArrayOf(
                    Color.parseColor("#2a9085"), Color.TRANSPARENT
                )
                lineChart.animation.duration = Constants.animationDuration

                // Преобразование данных для анимации графика.
                val listData = item.sparklineIn7d?.price.toDoubleFloatPairs()
                lineChart.animate(listData)

                // Установка слушателя нажатия на элемент списка.
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }
            }
        }
    }

    // Это закрытое свойство для хранения слушателя нажатия на элемент списка.
    private var onItemClickListener: ((ResponseCoinsMarkets.ResponseCoinsMarketsItem) -> Unit)? = null

    // Эта функция устанавливает слушатель нажатия на элемент списка.
    fun setOnItemClickListener(listener: (ResponseCoinsMarkets.ResponseCoinsMarketsItem) -> Unit) {
        onItemClickListener = listener
    }

    // Класс для определения методов сравнения элементов списка.
    private val differCallback = object : DiffUtil.ItemCallback<ResponseCoinsMarkets.ResponseCoinsMarketsItem>() {

        // Этот метод проверяет, являются ли два элемента одинаковыми.
        override fun areItemsTheSame(
            oldItem: ResponseCoinsMarkets.ResponseCoinsMarketsItem,
            newItem: ResponseCoinsMarkets.ResponseCoinsMarketsItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        // Этот метод проверяет, имеют ли два элемента одинаковые данные.
        override fun areContentsTheSame(
            oldItem: ResponseCoinsMarkets.ResponseCoinsMarketsItem,
            newItem: ResponseCoinsMarkets.ResponseCoinsMarketsItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    // Список, используемый для вычисления различий в данных.
    val differ = AsyncListDiffer(this, differCallback)
}
