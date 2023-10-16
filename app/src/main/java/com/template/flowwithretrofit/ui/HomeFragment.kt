package com.template.flowwithretrofit.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.template.flowwithretrofit.adapter.CryptosAdapter
import com.template.flowwithretrofit.databinding.FragmentHomeBinding
import com.template.flowwithretrofit.utils.DataStatus
import com.template.flowwithretrofit.utils.initRecycler
import com.template.flowwithretrofit.utils.isVisible
import com.template.flowwithretrofit.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Инициализируем ViewModel с помощью Hilt
    private val viewModel: MainViewModel by viewModels()

    // Инжектируем адаптер для отображения списка криптовалют
    @Inject
    lateinit var cryptosAdapter: CryptosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Настраиваем RecyclerView для отображения списка
        setupRecyclerView()

        // Запускаем корутину для обработки данных в фоновом режиме
        lifecycleScope.launch {
            binding.apply {

                // Запрашиваем список криптовалют в Euro
                viewModel.getCoinsList("eur")

                // Наблюдаем за изменениями в списке криптовалют
                viewModel.coinsList.observe(viewLifecycleOwner) { dataStatus ->
                    when (dataStatus.status) {
                        DataStatus.Status.LOADING -> {
                            // Показываем прогресс-бар и скрываем RecyclerView
                            pBarLoading.isVisible(true, rvCrypto)
                        }

                        DataStatus.Status.SUCCESS -> {
                            // Скрываем прогресс-бар и обновляем данные в адаптере
                            pBarLoading.isVisible(false, rvCrypto)
                            cryptosAdapter.differ.submitList(dataStatus.data)

                            // Устанавливаем слушатель кликов на элементы списка
                            cryptosAdapter.setOnItemClickListener { item ->
                                Log.d("HomeFragment", item.id!!)
                                val direction =
                                    HomeFragmentDirections.actionHomeFragmentToDetailsFragment(item.id)
                                findNavController().navigate(direction)
                            }
                        }

                        DataStatus.Status.ERROR -> {
                            // Показываем прогресс-бар и выводим сообщение об ошибке
                            pBarLoading.isVisible(true, rvCrypto)
                            Toast.makeText(
                                requireContext(), "Что-то пошло не так!", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    // Настраиваем RecyclerView с использованием LinearLayoutManager и адаптера
    private fun setupRecyclerView() {
        binding.rvCrypto.initRecycler(LinearLayoutManager(requireContext()), cryptosAdapter)
    }

    // Очищаем привязку при уничтожении фрагмента
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
