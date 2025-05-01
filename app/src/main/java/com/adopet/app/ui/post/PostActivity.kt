package com.adopet.app.ui.post

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.adopet.app.R
import com.adopet.app.databinding.ActivityPostBinding
import com.adopet.app.utils.Result
import com.adopet.app.utils.ViewModelFactory
import com.adopet.app.utils.reduceFileImage
import com.adopet.app.utils.uriToFile

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    private lateinit var viewModel: PostViewModel
    private var imageUri: Uri? = null
    private var decideType: String = ""
    private var confidenceScore: Double = 0.0
    private var normalizedBreed: String = ""

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

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        )[PostViewModel::class.java]

        imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)?.toUri()
        imageUri?.let { binding.ivPet.setImageURI(it) }

        val resultList = intent.getStringArrayListExtra(EXTRA_RESULTS)

        if (resultList != null) {
            for(result in resultList) {
                val splitResult = splitResult(result)
                normalizedBreed = normalizeLabelText(splitResult.first)
                Log.d(TAG, "onCreate: $normalizedBreed")
                decideType = decideType(normalizedBreed)
                confidenceScore = splitResult.second
                binding.tvPetBreed.text = normalizedBreed + "\n" + splitResult.second
                binding.tvPetType.text = if(decideType == "Cat") "Kucing" else "Anjing"
            }
        }

        binding.btnUpload.setOnClickListener {
            val petType = decideType
            val petName = binding.etPetName.text.toString()
            val petAgeText = binding.etPetAge.text?.toString()?.trim()
            val petAge = petAgeText?.toIntOrNull() ?: -1
            val petDescription = binding.etPetDescription.text.toString()
            Log.d(TAG, "onCreate: $normalizedBreed upload")
            uploadImage(
                petName, normalizedBreed, petType, petAge, petDescription, confidenceScore
            )
        }
    }


    private fun uploadImage(
        petName: String,
        petBreed: String,
        petType: String,
        petAge: Int,
        description: String,
        confidenceScore: Double
    ) {
        imageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()

            viewModel.postAdoption(
                petName, petBreed, petType, petAge, description, confidenceScore, imageFile
            ).observe(this) { result ->
                when (result) {
                    is Result.Success<*> ->  {
                        showDialog("Upload Berhasil", "Data berhasil ditambahkan")
                    }
                    is Result.Error -> showDialog("Upload Gagal", "Data gagal ditambahkan")
                    Result.Loading -> {

                    }
                }
            }
        }
    }

    private fun splitResult(result: String): Pair<String, Double> {
        val parts = result.split(":")
        if (parts.size == 2) {
            val breed = parts[0].trim()
            val confidenceScore = parts[1].replace("%", "").trim().toDoubleOrNull()
            if (confidenceScore != null) {
                return Pair(breed, confidenceScore)
            }
        }

        return Pair("unknown", 0.0)
    }

    private fun showDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun normalizeLabelText(text: String): String {
        val replaced = text.replace("_", " ")
        return replaced.substring(0, 1).uppercase() + replaced.substring(1)
    }

    private fun decideType(breed: String): String {
        return when (breed) {
            "American bulldog",
            "American pit bull",
            "Chihuahua",
            "Pomeranian",
            "Pug" -> "Dog"

            "British",
            "Persian",
            "Ragdoll",
            "Russian",
            "Sphynx" -> "Cat"

            else -> "Unknown"
        }
    }


    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULTS = "extra_result"
        const val TAG = "PostActivity"
    }
}