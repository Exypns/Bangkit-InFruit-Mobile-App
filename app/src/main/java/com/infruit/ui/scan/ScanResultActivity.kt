package com.infruit.ui.scan

import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import com.infruit.databinding.ActivityScanResultBinding

class ScanResultActivity : AppCompatActivity() {

    private lateinit var binding : ActivityScanResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val image = Uri.parse(intent.getStringExtra(EXTRA_IMAGE))
        val label = intent.getStringExtra(EXTRA_LABEL)
        val score = intent.getStringExtra(EXTRA_SCORE)
        val recommendation = intent.getStringExtra(EXTRA_RECOMMENDATION)

        val result = "$score\n$label"

        binding.imageResult.setImageURI(image)
        binding.resultTextView.text = result

        if (label?.contains("Class", ignoreCase = false) == true) {
            binding.recommendationTextView.gravity = Gravity.LEFT
        }
        binding.recommendationTextView.text = recommendation

    }

    companion object {
        const val EXTRA_IMAGE = "EXTRA_IMAGE"
        const val EXTRA_LABEL = "EXTRA_LABEL"
        const val EXTRA_SCORE = "0"
        const val EXTRA_RECOMMENDATION = "EXTRA_RECOMMENDATION"
    }
}