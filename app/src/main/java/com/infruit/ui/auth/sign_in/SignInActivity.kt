package com.infruit.ui.auth.sign_in

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.infruit.R
import com.infruit.data.TypesResponse
import com.infruit.data.model.user.RegisterRequest
import com.infruit.databinding.ActivitySignInBinding
import com.infruit.hideSoftKeyboard
import com.infruit.showDialogError
import com.infruit.showDialogLoading
import com.infruit.showDialogSuccess
import com.infruit.ui.auth.login.LoginActivity
import com.infruit.viewmodel.sign_in.RegisterViewModel

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var dialogLoading: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        dialogLoading = showDialogLoading(this)

        onAction()
    }

    private fun onAction() {
        binding.apply {

            tvLoginHere.setOnClickListener {
                startActivity(Intent(this@SignInActivity, LoginActivity::class.java))
                finish()
            }

            btnRegister.setOnClickListener {
                val name = etUsername.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val pass = etPassword.text.toString().trim()

                if (checkValidation(name, email, pass)) {
                    hideSoftKeyboard(this@SignInActivity, binding.root)
                    registerUser(name, email, pass)
                }
            }
        }
    }

    private fun registerUser(name: String, email: String, pass: String) {
        val user = RegisterRequest(name, email, pass)
        registerViewModel.registerUser(user)

        registerViewModel.registerResponse.observe(this) { response ->
            when (response) {
                is TypesResponse.Loading -> {
                    dialogLoading.show()
                }

                is TypesResponse.Success -> {
                    dialogLoading.dismiss()
                    val dialogSuccess = showDialogSuccess(
                        this,
                        "Selamat, anda berhasil membuat akun"
                    )
                    dialogSuccess.show()

                    Handler(Looper.getMainLooper())
                        .postDelayed({
                            dialogSuccess.dismiss()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }, 1500)
                }

                is TypesResponse.Error -> {
                    dialogLoading.dismiss()
                    showDialogError(this@SignInActivity, response.message.toString())
                }

                else -> {}
            }
        }
    }

    private fun checkValidation(name: String, email: String, pass: String): Boolean {
        binding.apply {
            when {
                name.isEmpty() -> {
                    etUsername.error = "Tolong isi Nama anda terlebih dahulu"
                    etUsername.requestFocus()
                }

                email.isEmpty() -> {
                    etEmail.error = "Tolong isi Email anda terlebih dahulu"
                    etEmail.requestFocus()
                }

                pass.isEmpty() -> {
                    etPassword.error = "Tolong isi Password anda terlebih dahulu"
                    etPassword.requestFocus()
                }

                else -> return true
            }
        }
        return false
    }
}