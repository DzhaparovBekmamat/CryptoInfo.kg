package com.template.flowwithretrofit.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.template.flowwithretrofit.R
import com.template.flowwithretrofit.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    // Инициализируем NavController для навигации между фрагментами
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Создаем привязку для активити
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализируем NavController, который будет управлять навигацией
        navController = findNavController(R.id.fragmentContainerView)

        // Создаем конфигурацию панели действий с указанием фрагментов, имеющих верхний уровень
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.detailsFragment
            )
        )

        // Настраиваем панель действий с NavController и AppBarConfiguration
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    // Обрабатываем нажатие на кнопку "Назад" в панели действий
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    // Очищаем привязку при уничтожении активити
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
