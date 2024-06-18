package com.infruit.ui.auth.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.infruit.MainActivity
import com.infruit.data.TypesResponse
import com.infruit.data.datastore.UserDataPreferences
import com.infruit.data.model.user.LoginRequest
import com.infruit.databinding.ActivityLoginBinding
import com.infruit.factory.LoginViewModelFactory
import com.infruit.showDialogError
import com.infruit.showDialogLoading
import com.infruit.showDialogSuccess
import com.infruit.ui.auth.sign_in.SignInActivity
import com.infruit.viewmodel.login.LoginViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var dialogLoading: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        val pref = UserDataPreferences.getInstance(dataStore)
        loginViewModel =
            ViewModelProvider(this, LoginViewModelFactory(pref))[LoginViewModel::class.java]
        dialogLoading = showDialogLoading(this)

        onAction()
    }


    private fun onAction() {
        binding.apply {

            tvRegisterHere.setOnClickListener {
                startActivity(Intent(this@LoginActivity, SignInActivity::class.java))
                finish()
            }

            btnLogin.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val pass = etPassword.text.toString().trim()

                if (validationInput(email, pass)) {
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
                    loginUser(email, pass)
                }
            }
        }
    }

    private fun loginUser(email: String, pass: String) {
        val user = LoginRequest(email, pass)
        loginViewModel.loginUser(user)

        loginViewModel.loginResponse.observe(this) { response ->
            when (response) {
                is TypesResponse.Error -> {
                    dialogLoading.dismiss()
                    showDialogError(this@LoginActivity, response.message.toString())
                }

                is TypesResponse.Loading -> {
                    dialogLoading.show()
                }

                is TypesResponse.Success -> {
                    dialogLoading.dismiss()
                    val loginResponse = response.data
                    val token = loginResponse?.data?.token

                    if (token != null) {
                        loginViewModel.saveUserData(token)
                    }

                    val dialogSuccess = showDialogSuccess(
                        this,
                        "Selamat anda berhasil login"
                    )
                    dialogSuccess.show()

                    Handler(Looper.getMainLooper())
                        .postDelayed({
                            dialogSuccess.dismiss()
                            startActivity(Intent(this, MainActivity::class.java))
                            finishAffinity()
                        }, 1500)
                }

                else -> {}
            }
        }
    }


    private fun validationInput(email: String, pass: String): Boolean {
        binding.apply {
            when {
                email.isEmpty() -> {
                    etEmail.error = "Isi email anda terlebih dahulu"
                    etEmail.requestFocus()
                }

                pass.isEmpty() -> {
                    etPassword.error = "Isi password anda terlebih dahulu"
                    etPassword.requestFocus()
                }

                else -> return true
            }
        }
        return false
    }
}