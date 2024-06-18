package com.infruit.ui.scan

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
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

        val result = "$score\n\n$label"

        val imageOption = RequestOptions().transform(RoundedCorners(10))
//        Glide.with(binding.imageResult).load(image).apply(imageOption).into(binding.imageResult)
        binding.imageResult.setImageURI(image)
        binding.resultTextView.text = result
        binding.recommendationTextView.text = recommendation

//        when (label) {
//            "freshapples" -> binding.recommendationTextView.text = getString(R.string.freshapples)
//            "freshbanana" -> binding.recommendationTextView.text = getString(R.string.freshbanana)
//            "freshoranges" -> binding.recommendationTextView.text = getString(R.string.freshoranges)
//            "rottenapples" -> binding.recommendationTextView.text = getString(R.string.rottenapples)
//            "rottenbanana" -> binding.recommendationTextView.text = getString(R.string.rottenbanana)
//            "rottenoranges" -> binding.recommendationTextView.text = getString(R.string.rottenoranges)
//            else -> binding.recommendationTextView.text = "Unknown Label"
//        }
    }

    companion object {
        const val EXTRA_IMAGE = "EXTRA_IMAGE"
        const val EXTRA_LABEL = "EXTRA_LABEL"
        const val EXTRA_SCORE = "0"
        const val EXTRA_RECOMMENDATION = "EXTRA_RECOMMENDATION"
    }
}