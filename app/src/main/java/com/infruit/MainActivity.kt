package com.infruit

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
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
                R.id.navigation_home, R.id.navigation_history, R.id.navigation_shop, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        binding.scanButton.setOnClickListener {
            scanTypeDialog()
        }

//        Log.d("Token ", token.toString())
    }

    private fun scanTypeDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.scan_type_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val grading = dialog.findViewById<Button>(R.id.gradingButton)
        val freshAndRotten = dialog.findViewById<Button>(R.id.freshAndRottenButton)


        grading.setOnClickListener {
            moveToCamera("Grading")
            dialog.dismiss()
        }

        freshAndRotten.setOnClickListener {
            moveToCamera("FreshAndRotten")
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun moveToCamera(scanType: String) {
        val token = intent.getStringExtra("token")
        Log.d("Token ", token.toString())
        val intentCamera = Intent(this, CameraActivity::class.java)
        intentCamera.putExtra(CameraActivity.EXTRA_MODEL, scanType)
        intentCamera.putExtra("token", token)
        startActivity(intentCamera)
    }
}