package com.adopet.app.ui.history.detail

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.adopet.app.R
import com.adopet.app.data.model.PostsItem
import com.adopet.app.databinding.ActivityHistoryDetailBinding
import com.adopet.app.utils.DateHelper
import com.adopet.app.utils.Result
import com.adopet.app.utils.ViewModelFactory
import com.bumptech.glide.Glide

class HistoryDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryDetailBinding
    private lateinit var viewModel: HistoryDetailViewModel
    private var petId: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(this)
        )[HistoryDetailViewModel::class.java]

        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_DATA, PostsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DATA)
        }
        data?.let {
            binding.tvPostDate.text =
                data.postDate?.let { it1 -> DateHelper.formatDate(it1).toString() }
            binding.tvPetAge.text = getString(R.string.pet_age, data.petAge.toString())
            binding.tvPetName.text = getString(R.string.pet_name, data.petName.toString())
            binding.tvPetType.text = getString(
                R.string.pet_type,
                if (data.petType.equals("Cat", true)) "Kucing" else "Anjing"
            )
            binding.tvPetBreed.text = getString(
                R.string.pet_breed_info,
                data.petBreed.toString(),
                data.confidenceScore.toString() + "%"
            )
            binding.tvPetDescription.text = data.description.toString()

            binding.switchAvailability.isChecked = data.isAvailable ?: false
            petId = (data.postId ?: 1).toLong()

            Glide.with(this)
                .load(data.imageUrl)
                .into(binding.ivPet)
        }

        binding.btnChangeAvailability.setOnClickListener {
            val currentAvailability = binding.switchAvailability.isChecked
            Log.d(TAG, "onCreate: petId: $petId")
            viewModel.changeAvailability(petId, currentAvailability).observe(this) { result ->
                when(result) {
                    is Result.Error -> {
                        showToast(result.error)
                    }
                    Result.Loading -> { }
                    is Result.Success<*> -> {
                        showToast(result.data.toString())
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "HistoryDetailActivity"
        const val EXTRA_DATA = "EXTRA_DATA"
    }
}