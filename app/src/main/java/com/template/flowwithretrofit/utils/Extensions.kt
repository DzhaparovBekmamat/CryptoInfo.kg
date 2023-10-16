package com.template.flowwithretrofit.utils

import android.content.Context
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat

// Функция для управления видимостью элементов
fun View.isVisible(isShowLoading: Boolean, container: View) {
    if (isShowLoading) {
        this.visibility = View.VISIBLE
        container.visibility = View.GONE
    } else {
        this.visibility = View.GONE
        container.visibility = View.VISIBLE
    }
}

// Функция для получения цвета из ресурсов совместимости
fun Context.getCompatColor(@ColorRes colorId: Int) =
    ResourcesCompat.getColor(resources, colorId, null)

// Функция для получения рисунка из ресурсов совместимости
fun Context.getCompatDrawable(@DrawableRes drawableId: Int) =
    AppCompatResources.getDrawable(this, drawableId)!!

// Расширение RecyclerView для инициализации
fun RecyclerView.initRecycler(
    layoutManager: RecyclerView.LayoutManager, adapter: RecyclerView.Adapter<*>
) {
    this.adapter = adapter
    this.layoutManager = layoutManager
}

// Расширение для преобразования списка Double в список Pair<String, Float>
fun List<Double?>?.toDoubleFloatPairs(): List<Pair<String, Float>> {
    return this!!.map { d ->
        val f = d!!.toFloat()
        val s = d.toString()
        Pair(s, f)
    }
}

// Форматирование Double до двух знаков после запятой
private val formatter2 = DecimalFormat("##.##")
fun Double.roundToTwoDecimals() = formatter2.format(this).toString()

// Форматирование Double до трех знаков после запятой
private val formatter3 = DecimalFormat("##.###")
fun Double.roundToThreeDecimals() = formatter3.format(this).toString()
