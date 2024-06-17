package com.infruit.ui.scan

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.infruit.R
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

        val result = "$score\n\n$label"

        binding.imageResult.setImageURI(image)
        binding.resultTextView.text = result

        when (label) {
            "freshapples" -> binding.recommendationTextView.text = getString(R.string.freshapples)
            "freshbanana" -> binding.recommendationTextView.text = getString(R.string.freshbanana)
            "freshoranges" -> binding.recommendationTextView.text = getString(R.string.freshoranges)
            "rottenapples" -> binding.recommendationTextView.text = getString(R.string.rottenapples)
            "rottenbanana" -> binding.recommendationTextView.text = getString(R.string.rottenbanana)
            "rottenoranges" -> binding.recommendationTextView.text = getString(R.string.rottenoranges)
        }
    }

    companion object {
        const val EXTRA_IMAGE = "EXTRA_IMAGE"
        const val EXTRA_LABEL = "EXTRA_LABEL"
        const val EXTRA_SCORE = "0"
    }
}