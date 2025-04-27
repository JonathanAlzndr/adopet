package com.adopet.app.ui.post

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adopet.app.R
import com.adopet.app.databinding.ActivityPostBinding

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)?.toUri()
        val resultList = intent.getStringArrayListExtra(EXTRA_RESULTS)
        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imageView.setImageURI(it)
        }
        val resultsText = resultList?.joinToString("\n") ?: "No results"
        binding.resultTextView.text = resultsText
        Log.d("PostActivity", resultsText)
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULTS = "extra_result"
    }
}