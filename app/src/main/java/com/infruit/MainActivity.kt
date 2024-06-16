package com.infruit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.infruit.databinding.ActivityMainBinding
import com.infruit.ui.scan.CameraActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavView.background = null
        supportActionBar?.hide()

        val navView: BottomNavigationView = binding.bottomNavView

        val navController = findNavController(R.id.nav_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_activity, R.id.navigation_shop, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.scanButton.setOnClickListener {
            startCameraX()
        }
    }

    private fun startCameraX() {
        startActivity(Intent(this, CameraActivity::class.java))
    }
}