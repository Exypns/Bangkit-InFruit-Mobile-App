package com.infruit.ui.scan

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.infruit.R
import com.infruit.createCustomTempFile
import com.infruit.databinding.ActivityCameraBinding
import com.infruit.helper.FreshAndRottenClassifierHelper
import com.infruit.helper.GradingClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var gradingClassifierHelper: GradingClassifierHelper
    private lateinit var freshAndRottenClassifierHelper: FreshAndRottenClassifierHelper
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        cameraSuggestionDialog()

        binding.switchCamera.setOnClickListener {
            cameraSelector = if (cameraSelector.equals(CameraSelector.DEFAULT_BACK_CAMERA)) CameraSelector.DEFAULT_FRONT_CAMERA
            else CameraSelector.DEFAULT_BACK_CAMERA

            startCamera()
        }
        binding.captureButton.setOnClickListener { takePhoto() }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )

            } catch (exc: Exception) {
                Toast.makeText(
                    this@CameraActivity,
                    "Gagal memunculkan kamera",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e(TAG, "startCamera: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createCustomTempFile(application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        val scanType = intent.getStringExtra(EXTRA_MODEL)

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Berhasil mengambil gambar",
                        Toast.LENGTH_SHORT
                    ).show()
                    output.savedUri
                    output.savedUri?.let {
                        if (scanType == "Grading") {
                            gradingAnalyze(it)
                        }
                        else if (scanType == "FreshAndRotten") {
                            freshAndRottenAnalyze(it)
                        }
                    }
                    finish()
                }

                override fun onError(e: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        "Gagal mengambil gambar",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, "onError ${e.message}")
                }
            }
        )
    }

    private fun gradingAnalyze(image: Uri) {
        gradingClassifierHelper = GradingClassifierHelper(
            context = this,
            classifierListener = object : GradingClassifierHelper.ClassifierListener {
                override fun onError(error: String) { showToast(error) }

                override fun onResults(results: List<Classifications>?) {
                    results?.let { it ->
                        if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                            println(it)
                            val sortedCategory = it[0].categories.sortedByDescending { it?.score }
                            val categoryLabel = sortedCategory.joinToString { it.label }
                            val confidenceScore = sortedCategory.joinToString { NumberFormat.getPercentInstance()
                                .format(it.score).trim() }
                            val recommendation = ""
                            moveToResult(image, categoryLabel, confidenceScore, recommendation)
                        }
                    }
                }
            }
        )
        gradingClassifierHelper.classifyImage(image)
    }

    private fun freshAndRottenAnalyze(image: Uri) {
        freshAndRottenClassifierHelper = FreshAndRottenClassifierHelper(
            context = this,
            classifierListener = object : FreshAndRottenClassifierHelper.ClassifierListener {
                override fun onError(error: String) { showToast(error) }

                override fun onResults(results: List<Classifications>?) {
                    results?.let { it ->
                        if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                            println(it)
                            val sortedCategory = it[0].categories.sortedByDescending { it?.score }
                            val categoryLabel = sortedCategory.joinToString { it.label }
                            val confidenceScore = sortedCategory.joinToString { NumberFormat.getPercentInstance()
                                .format(it.score).trim() }
                            val recommendation = ""
                            moveToResult(image, categoryLabel, confidenceScore, recommendation)
                        }
                    }
                }
            }
        )
        freshAndRottenClassifierHelper.classifyImage(image)
    }

    private fun moveToResult(image: Uri, label: String, score: String, recommendation: String) {
        val intent = Intent(this, ScanResultActivity::class.java)
        intent.putExtra(ScanResultActivity.EXTRA_IMAGE, image.toString())
        intent.putExtra(ScanResultActivity.EXTRA_LABEL, label)
        intent.putExtra(ScanResultActivity.EXTRA_SCORE, score)
        intent.putExtra(ScanResultActivity.EXTRA_RECOMMENDATION, recommendation)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun cameraSuggestionDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.camera_suggestion_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val confirmButton = dialog.findViewById<Button>(R.id.confirmButton)

        confirmButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun hideSystemUI() {
//        @Suppress("DEPRECATION")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.insetsController?.hide(WindowInsets.Type.statusBars())
//        } else {
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//            )
//        }
        supportActionBar?.hide()
    }

    companion object {
        private const val TAG = "CameraActivity"
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        const val EXTRA_MODEL = "EXTRA_MODEL"
    }
}