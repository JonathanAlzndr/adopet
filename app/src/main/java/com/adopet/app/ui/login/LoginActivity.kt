package com.adopet.app.ui.login

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.adopet.app.R
import com.adopet.app.data.model.UserModel
import com.adopet.app.databinding.ActivityLoginBinding
import com.adopet.app.ui.MainActivity
import com.adopet.app.ui.home.HomeFragment
import com.adopet.app.ui.register.RegisterActivity
import com.adopet.app.utils.Result
import com.adopet.app.utils.ViewModelFactory
import android.Manifest

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast("Permission request granted")
            } else {
                showToast("Permission request denied")
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.hide()

        if(!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        viewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }

        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (validateInput(username, password)) {
                performLogin(username, password)
            }
        }

        binding.registerTextView.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }


    }

    private fun validateInput(username: String, password: String): Boolean {
        var isValid = true
        if (username.isBlank() || username.isEmpty()) {
            binding.usernameEditText.error = "username tidak boleh kosong"
            isValid = false
        }
        if (password.isBlank() || password.isEmpty()) {
            binding.passwordEditText.error = "password tidak boleh kosong"
            isValid = false
        }
        return isValid
    }

    private fun performLogin(username: String, password: String) {
        viewModel.login(username, password).observe(this) { result ->
            when (result) {
                is Result.Error -> {
                    showToast(result.error)
                }

                Result.Loading -> {}
                is Result.Success -> {
                    // setLoading(false)
                    result.data.let {
                        val user = UserModel(username, it.token!!, true)
                        viewModel.saveSession(user)
                        HomeFragment.name = user.username
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}