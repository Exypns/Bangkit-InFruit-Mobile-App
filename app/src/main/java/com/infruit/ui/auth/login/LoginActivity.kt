package com.infruit.ui.auth.login

import android.app.Dialog
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
import com.infruit.R
import com.infruit.data.datastore.UserDataPreferences
import com.infruit.databinding.ActivityLoginBinding
import com.infruit.viewmodel.login.LoginViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var alertProcess: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserDataPreferences.getInstance(dataStore)

        alertProcess = AlertDialog.Builder(this).setTitle("Loading")
            .setMessage("Sedang Loading").create()
//        alertProcess.setContentView(R.layout.dialog_loading)
        alertProcess.setCancelable(false)

//        actionUser()
    }

//    private fun actionUser() {
//        binding.apply {
//
//            btnRegister.setOnClickListener {
//                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
//                finish()
//            }
//
//            btnLogin.setOnClickListener {
//                val email = etEmail.text.toString().trim()
//                val pass = etPassword.text.toString().trim()
//
//                if (validationInput(email, pass)) {
//                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
//                    loginUser(email, pass)
//                }
//            }
//
//        }
//    }
//
//    private fun loginUser(email: String, pass: String) {
//        val user = LoginRequest(email, pass)
//        loginViewModel.loginUser(user)
//
//        loginViewModel.loginResponse.observe(this) { response ->
//            when (response) {
//                is TypesResponse.Error -> {
//                    alertProcess.dismiss()
//
//                    AlertDialog.Builder(this)
//                        .setTitle("Gagal")
//                        .setMessage(response.message.toString())
//                        .setPositiveButton("OK") { alertProcess, _ ->
//                            alertProcess.dismiss()
//                        }
//                        .show()
//                }
//
//                is TypesResponse.Loading -> {
//                    alertProcess.show()
//                }
//
//                is TypesResponse.Success -> {
//                    alertProcess.dismiss()
//                    val userData = response.data
//                    val userId = userData?.loginResult?.userId
//                    val name = userData?.loginResult?.name
//                    val token = userData?.loginResult?.token
//
//                    if (userId != null && name != null && token != null) {
//                        loginViewModel.saveUserData(userId, name, token)
//                    }
//
//                    val dialogSuccess =
//                        AlertDialog.Builder(this)
//                            .setTitle("Berhasil")
//                            .setMessage("Selamat, anda berhasil login")
//                            .setPositiveButton("OK") { alertProcess, _ ->
//                                alertProcess.dismiss()
//                            }
//                            .create()
//                    dialogSuccess.show()
//
//                    Handler(Looper.getMainLooper())
//                        .postDelayed({
//                            dialogSuccess.dismiss()
//                            startActivity(Intent(this, MainActivity::class.java))
//                            finishAffinity()
//                        }, 1500)
//                }
//
//                else -> {}
//            }
//        }
//    }

//    private fun validationInput(email: String, pass: String): Boolean {
//        binding.apply {
//            when {
//                email.isEmpty() -> {
//                    etEmail.error = "Isi email anda terlebih dahulu"
//                    etEmail.requestFocus()
//                }
//
//                pass.isEmpty() -> {
//                    etPassword.error = "Isi password anda terlebih dahulu"
//                    etPassword.requestFocus()
//                }
//
//                else -> return true
//            }
//        }
//        return false
//    }
}