package com.infruit.ui.scan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.infruit.databinding.ActivityScanResultBinding

class ScanResultActivity : AppCompatActivity() {

    private lateinit var binding : ActivityScanResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}