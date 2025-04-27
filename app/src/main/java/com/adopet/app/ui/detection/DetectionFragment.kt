package com.adopet.app.ui.detection

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.adopet.app.R
import com.adopet.app.databinding.FragmentDetectionBinding
import com.adopet.app.databinding.FragmentHomeBinding
import com.adopet.app.ui.camera.CameraActivity
import com.adopet.app.ui.camera.CameraActivity.Companion.CAMERAX_RESULT
import com.adopet.app.ui.home.HomeViewModel
import com.adopet.app.ui.post.PostActivity
import com.adopet.app.utils.InterpreterImageClassifierHelper
import com.adopet.app.utils.ViewModelFactory
import com.adopet.app.utils.getImageUri


class DetectionFragment : Fragment() {

    private var _binding: FragmentDetectionBinding? = null
    private lateinit var viewModel: DetectionViewModel
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var currentImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity())
        viewModel = ViewModelProvider(this, factory)[DetectionViewModel::class.java]

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.cameraXButton.setOnClickListener { startCameraX() }
        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                analyzeImage(it)
            } ?: run {
                showToast(getString(R.string.empty_image_warning))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun startCameraX() {
        val intent = Intent(requireActivity(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage(uri: Uri) {
        try {
            val bitmap = com.adopet.app.utils.uriToBitmap(uri, requireContext())
            val classifier = InterpreterImageClassifierHelper(requireContext(), listener = object: InterpreterImageClassifierHelper.ResultListener {
                override fun onResults(results: List<Pair<String, Float>>, inferenceTime: Long) {
                    goToPostActivity(results)
                }

                override fun onError(error: String) {
                    showToast("Error : $error")
                    Log.d("Detection", "onError: $error")
                }
            })
            classifier.classify(bitmap)
        } catch(e: Exception) {
            showToast("Failed to load image: ${e.message}")
            Log.d("Detection", "${e.message}")
        }
    }


    private fun goToPostActivity(results: List<Pair<String, Float>>) {
        val highestResult = results.maxByOrNull { it.second }
        val highestClass = highestResult?.first ?: "Unknown"
        val highestScore = highestResult?.second ?: 0.0f
        val formattedScore = String.format("%.2f", highestScore * 100)

        val intent = Intent(requireActivity(), PostActivity::class.java).apply {
            putExtra(PostActivity.EXTRA_IMAGE_URI, currentImageUri.toString())
            putExtra(PostActivity.EXTRA_RESULTS, arrayListOf("$highestClass: $formattedScore%"))
        }
        startActivity(intent)
    }


    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}