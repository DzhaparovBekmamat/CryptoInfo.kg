package com.template.flowwithretrofit.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.template.flowwithretrofit.R
import com.template.flowwithretrofit.databinding.FragmentDetailsBinding
import com.template.flowwithretrofit.utils.Constants.animationDuration
import com.template.flowwithretrofit.utils.DataStatus
import com.template.flowwithretrofit.utils.getCompatDrawable
import com.template.flowwithretrofit.utils.isVisible
import com.template.flowwithretrofit.utils.roundToThreeDecimals
import com.template.flowwithretrofit.utils.roundToTwoDecimals
import com.template.flowwithretrofit.utils.toDoubleFloatPairs
import com.template.flowwithretrofit.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailsBinding.inflate(layoutInflater)
        viewModel.getDetailsCoin(args.id)
        return binding.root
    }

    // Функция вызывается, когда фрагмент создан и его представление готово
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            binding.apply {
                viewModel.detailsCoin.observe(viewLifecycleOwner) { dataStatus ->
                    when (dataStatus.status) {
                        DataStatus.Status.LOADING -> {
                            // Показываем прогресс-бар и скрываем основной контент
                            pBarLoading.isVisible(true, mainLayout)
                        }

                        DataStatus.Status.SUCCESS -> {
                            // Если данные успешно загружены
                            dataStatus.data?.let { data ->
                                pBarLoading.isVisible(false, mainLayout)

                                // Заполняем UI с полученными данными
                                tvCoinNameSymbol.text =
                                    "${data.name} [ ${data.symbol?.uppercase()} ]"
                                tvCurrentPrice.text =
                                    data.marketData?.currentPrice?.eur?.roundToThreeDecimals()

                                val number =
                                    data.marketData?.priceChangePercentage24h?.roundToTwoDecimals()
                                        ?.toDouble()!!

                                tvChangePercentage.text = "$number%"

                                // Устанавливаем цвет и направление стрелки в зависимости от изменения цены
                                when {
                                    number > 0 -> {
                                        tvChangePercentage.setTextColor(Color.GREEN)
                                        imgArrow.setImageDrawable(
                                            requireContext().getCompatDrawable(
                                                R.drawable.baseline_arrow_drop_up_24
                                            )
                                        )
                                    }

                                    number < 0 -> {
                                        tvChangePercentage.setTextColor(Color.RED)
                                        imgArrow.setImageDrawable(
                                            requireContext().getCompatDrawable(
                                                R.drawable.baseline_arrow_drop_down_24
                                            )
                                        )
                                    }

                                    else -> {
                                        tvChangePercentage.setTextColor(Color.LTGRAY)
                                        imgArrow.setImageDrawable(
                                            requireContext().getCompatDrawable(
                                                R.drawable.baseline_minimize_24
                                            )
                                        )
                                    }
                                }

                                // Загружаем изображение монеты
                                imgCoinLogo.load(data.image?.large) {
                                    crossfade(true)
                                    crossfade(500)
                                    placeholder(R.drawable.round_currency_bitcoin_24)
                                    error(R.drawable.round_currency_bitcoin_24)
                                }

                                // Устанавливаем градиент для графика
                                lineChart.gradientFillColors = intArrayOf(
                                    Color.parseColor("#2a9085"), Color.TRANSPARENT
                                )
                                lineChart.animation.duration = animationDuration

                                // Анимируем график
                                val listData =
                                    data.marketData.sparkline7d?.price?.toDoubleFloatPairs()
                                lineChart.animate(listData!!)

                                // Устанавливаем категорию и дату создания
                                tvCategories.text = data.categories?.get(0)!!
                                tvGenesisDate.text =
                                    if (!data.genesisDate.isNullOrEmpty()) data.genesisDate else "-"

                                // Устанавливаем ссылку и добавляем обработчик клика для перехода
                                tvLink.text = data.links?.homepage?.get(0)
                                tvLink.setOnClickListener {
                                    val uri = Uri.parse(data.links?.homepage?.get(0))
                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                    requireContext().startActivity(intent)
                                }

                                // Устанавливаем описание, очищаем HTML теги, если они есть
                                tvDescription.text =
                                    if (data.description?.en != null && data.description.en.isNotEmpty()) Jsoup.parse(
                                        data.description.en
                                    ).text() else "-"
                                tvDescription.movementMethod = ScrollingMovementMethod()
                            }
                        }

                        DataStatus.Status.ERROR -> {
                            // Показываем прогресс-бар и выводим сообщение об ошибке
                            pBarLoading.isVisible(true, mainLayout)
                            Toast.makeText(
                                requireContext(), "Что-то пошло не так!", Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    // Вызывается, когда фрагмент уничтожается и представление больше не нужно
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
