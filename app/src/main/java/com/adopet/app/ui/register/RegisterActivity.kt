package com.adopet.app.ui.register

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.adopet.app.databinding.ActivityRegisterBinding
import com.adopet.app.utils.Result
import com.adopet.app.utils.ViewModelFactory
import com.adopet.app.utils.applyDefaultSystemPadding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.applyDefaultSystemPadding()

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val phoneNumber = binding.etPhone.text.toString().trim()

            if (validateInput(username, password, email, phoneNumber)) {
                viewModel.register(username, password, email, phoneNumber)
                    .observe(this@RegisterActivity) { result ->
                        when (result) {
                            is Result.Error -> {
                                Toast.makeText(this@RegisterActivity, "Gagal Mendaftar", Toast.LENGTH_SHORT).show()
                            }
                            is Result.Loading -> {}
                            is Result.Success -> {
                                showToast()
                            }
                        }
                    }
            }
        }
    }

    private fun validateInput(
        username: String,
        password: String,
        email: String,
        phoneNumber: String
    ): Boolean {
        var isValid = true

        if (username.isEmpty()) {
            binding.etUsername.error = "Username tidak boleh kosong"
            isValid = false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Password tidak boleh kosong"
            isValid = false
        }

        if (email.isEmpty()) {
            binding.etEmail.error = "Email tidak boleh kosong"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Format email tidak valid"
            isValid = false
        }


        if (phoneNumber.isEmpty()) {
            binding.etPhone.error = "Nomor telepon tidak boleh kosong"
            isValid = false
        } else if (!phoneNumber.matches(Regex("^08[0-9]{8,13}$"))) {
            binding.etPhone.error =
                "Nomor telepon harus dimulai dari 08 dan terdiri dari 10-15 digit"
            isValid = false
        }

        return isValid
    }

    private fun showToast() {
        Toast.makeText(this@RegisterActivity, "Berhasil Mendaftar", Toast.LENGTH_SHORT).show()
    }
}