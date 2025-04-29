package com.adopet.app.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adopet.app.R
import com.adopet.app.data.model.DataItem
import com.adopet.app.databinding.ActivityPostDetailBinding
import com.adopet.app.utils.DateHelper
import com.bumptech.glide.Glide

class PostDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val topInset = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            v.setPadding(0, topInset, 0, 0)
            insets
        }

        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_DATA, DataItem::class.java)
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
            binding.tvAvailability.text =
                if (data.available == true) "Tersedia" else "Tidak tersedia"
            binding.tvAvailability.setTextColor(
                ContextCompat.getColor(
                    this,
                    if (data.available == true) R.color.green else R.color.red
                )
            )
            binding.tvOwnerName.text = data.petOwner?.username
            binding.btnContactOwner.setOnClickListener {
                val localPhone = data.petOwner?.phoneNumber
                val phoneNumber = if (!localPhone.isNullOrBlank() && localPhone.startsWith("08")) {
                    "62" + localPhone.substring(1)
                } else {
                    localPhone
                }

                if (!phoneNumber.isNullOrBlank()) {
                    val message =
                        "Halo ${data.petOwner?.username}, saya tertarik dengan hewan peliharaan Anda."
                    val url = "https://wa.me/${phoneNumber}?text=${Uri.encode(message)}"
                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                    try {
                        startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(
                            this,
                            "WhatsApp tidak tersedia di perangkat ini",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Nomor pemilik tidak tersedia atau tidak valid",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            Glide.with(this)
                .load(data.imageUrl)
                .into(binding.ivPet)
        }

    }

    companion object {
        const val EXTRA_DATA = "EXTRA_DATA"
    }
}