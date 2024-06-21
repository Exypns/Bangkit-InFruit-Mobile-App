package com.infruit.ui.scan

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.infruit.R
import com.infruit.createCustomTempFile
import com.infruit.data.TypesResponse
import com.infruit.data.datastore.UserDataPreferences
import com.infruit.databinding.ActivityCameraBinding
import com.infruit.factory.HistoryViewModelFactory
import com.infruit.helper.FreshAndRottenClassifierHelper
import com.infruit.helper.GradingClassifierHelper
import com.infruit.reduceFileImage
import com.infruit.uriToFile
import com.infruit.viewmodel.history.HistoryViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.text.NumberFormat
import java.util.UUID

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private lateinit var gradingClassifierHelper: GradingClassifierHelper
    private lateinit var freshAndRottenClassifierHelper: FreshAndRottenClassifierHelper
    private lateinit var historyViewModel: HistoryViewModel

    private lateinit var tokenData: String
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

        val pref = UserDataPreferences.getInstance(dataStore)

        historyViewModel = ViewModelProvider(this, HistoryViewModelFactory(pref))[HistoryViewModel::class.java]

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

        val token = intent.getStringExtra("token")

        Log.d("Token in camera", token.toString())
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

                @RequiresApi(Build.VERSION_CODES.Q)
                override fun onResults(results: List<Classifications>?) {
                    results?.let { it ->
                        if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                            println(it)
                            val sortedCategory = it[0].categories.sortedByDescending { it?.score }
                            val categoryLabel = sortedCategory.joinToString { it.label.trim() }
                            val confidenceScore = sortedCategory.joinToString { NumberFormat.getPercentInstance()
                                .format(it.score).trim() }
                            var recommendation = ""
                            when (categoryLabel) {
                                "Extra_Class" -> recommendation = getString(R.string.extra_class)
                                "Class_I" -> recommendation = getString(R.string.class_1)
                                "Class_II" -> recommendation = getString(R.string.class_2)
                                else -> recommendation = "Unknown Label"
                            }
                            val imageFile = uriToFile(image, this@CameraActivity).reduceFileImage()
                            val id = UUID.randomUUID().toString().toRequestBody("text/plain".toMediaType())
                            observeToken(confidenceScore, id, imageFile, categoryLabel, recommendation)
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

                @RequiresApi(Build.VERSION_CODES.Q)
                override fun onResults(results: List<Classifications>?) {
                    results?.let { it ->
                        if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                            println(it)
                            val sortedCategory = it[0].categories.sortedByDescending { it?.score }
                            val categoryLabel = sortedCategory.joinToString { it.label.trim() }
                            val confidenceScore = sortedCategory.joinToString { NumberFormat.getPercentInstance()
                                .format(it.score).trim() }
                            var recommendation = ""
                            when (categoryLabel) {
                                "freshapples" -> recommendation = getString(R.string.freshapples)
                                "freshbanana" -> recommendation = getString(R.string.freshbanana)
                                "freshoranges" -> recommendation = getString(R.string.freshoranges)
                                "rottenapples" -> recommendation = getString(R.string.rottenapples)
                                "rottenbanana" -> recommendation = getString(R.string.rottenbanana)
                                "rottenoranges" -> recommendation = getString(R.string.rottenoranges)
                                else -> recommendation = "Unknown Label"
                            }
                            val imageFile = uriToFile(image, this@CameraActivity).reduceFileImage()
                            val id = UUID.randomUUID().toString().toRequestBody("text/plain".toMediaType())
                            observeToken(confidenceScore, id, imageFile, categoryLabel, recommendation)
                            moveToResult(image, categoryLabel, confidenceScore, recommendation)
                        }
                    }
                }
            }
        )
        freshAndRottenClassifierHelper.classifyImage(image)
    }

    private fun observeToken(score: String, id: RequestBody,
                             image: File, label: String, recommendation: String) {
        val score = score.toString().toRequestBody("text/plain".toMediaType())
        val label = label.toString().toRequestBody("text/plain".toMediaType())
        val recommendation = recommendation.toString().toRequestBody("text/plain".toMediaType())
        historyViewModel.getToken().observe(this) { token ->
            token.let {
                if (token != null) {
                    tokenData = token
                    Log.d("Token in history", token)
                    createHistory(score, image, id, label, recommendation)
                }
            }

        }
    }


    private fun createHistory(score: RequestBody, image: File,
                              id: RequestBody, label: RequestBody, recommendation: RequestBody) {
        historyViewModel.createHistory(tokenData, image, id, label, score, recommendation)
        Log.d("Data History", "$tokenData, $score, $image, $recommendation, $label")
        historyViewModel.createHistoryResponse.observe(this) { response ->
            Log.d("Response History", response.message.toString())
            when (response) {
                is TypesResponse.Loading -> {
                    Log.d("Create History Loading", response.message.toString())
                }
                is TypesResponse.Success -> {
                    Log.d("Create History Success", "Success")
                }
                is TypesResponse.Error -> {
                    Log.d("Create History Error", "Error")
                }
            }
        }
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
        supportActionBar?.hide()
    }

    companion object {
        private const val TAG = "CameraActivity"
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        const val EXTRA_MODEL = "EXTRA_MODEL"
    }
}