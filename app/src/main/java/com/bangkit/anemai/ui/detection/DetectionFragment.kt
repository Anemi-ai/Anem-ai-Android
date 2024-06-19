package com.bangkit.anemai.ui.detection

import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.transition.ChangeBounds
import com.bangkit.anemai.R
import com.bangkit.anemai.databinding.FragmentDetectionBinding
import com.bangkit.anemai.ui.ViewModelFactory
import com.bangkit.anemai.ui.main.MainActivity
import com.bangkit.anemai.ui.main.MainViewModel
import com.bangkit.anemai.utils.Result
import com.bangkit.anemai.utils.createCustomTempFile
import com.bangkit.anemai.utils.uriToFile
import com.google.common.util.concurrent.ListenableFuture
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class DetectionFragment : Fragment() {
    private lateinit var binding: FragmentDetectionBinding
    private lateinit var menuProvider: MenuProvider
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null
    private var currentImgUri: Uri? = null
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext(),requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetectionBinding.inflate(layoutInflater)
        sharedElementEnterTransition = ChangeBounds().apply { duration = 500 }
        sharedElementReturnTransition = ChangeBounds().apply { duration = 500 }
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().actionBar?.hide()
        startCamera()
        setupAction(view)
        setupActionbar()
    }

    override fun onResume() {
        super.onResume()
        startCamera()
    }

    override fun onStop() {
        super.onStop()
        requireActivity().removeMenuProvider(menuProvider)
        stopCamera()
        (activity as AppCompatActivity).supportActionBar?.show()

    }

    override fun onDestroy() {
        super.onDestroy()
        stopCamera()
    }

    private fun setupAction(view: View) {
        binding.btnSwitchCamera.setOnClickListener {
            cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                            else CameraSelector.DEFAULT_BACK_CAMERA


            startCamera()
        }

        binding.btnPicture.setOnClickListener { takePhoto() }

        binding.btnDetect.setOnClickListener {
            predict(view)
        }
        binding.btnCancel.setOnClickListener {
            hideImageView()
        }
    }

    private fun startCamera() {
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_restart),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun stopCamera() {
        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
        cameraProviderFuture.addListener({
            try {
                cameraProvider.unbindAll()
            } catch (_: Exception) {
            }
        }, ContextCompat.getMainExecutor(requireContext()))

        imageCapture = null
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createCustomTempFile(requireContext())
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    currentImgUri = outputFileResults.savedUri
                    showImageView()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(requireContext(), getString(R.string.error_restart), Toast.LENGTH_SHORT).show()
                }

            }
        )
    }

    private fun predict(view: View) {
        currentImgUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext())

            val requestImage = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "my_image",
                imageFile.name,
                requestImage
            )

            viewModel.predict(multipartBody).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when(result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)

                            val extrasDetectionResult = FragmentNavigatorExtras(
                                binding.cameraCard to "image_view_layout",
                                binding.resultLayout to "result_layout"
                            )
                            val toDetailResultFragment = DetectionFragmentDirections.actionDetectionFragmentToDetectionResultFragment(result.data)
                            view.findNavController().navigate(toDetailResultFragment, extrasDetectionResult)
                        }
                        is Result.Error -> {
                            showLoading(false)

                            AlertDialog.Builder(context).apply {
                                setMessage(result.error)
                                setPositiveButton(getString(R.string.ok)) { _, _ ->
                                }

                                create()
                                show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showImageView() {
        stopCamera()
        binding.apply {
            imageViewDetection.visibility = View.VISIBLE
            btnCancelDetect.visibility = View.VISIBLE

            viewFinder.visibility = View.GONE
            btnSwitchCamera.visibility = View.GONE
            btnPicture.visibility = View.GONE
            eye.visibility = View.GONE

            currentImgUri?.let {
                imageViewDetection.setImageURI(it)
            }
        }
    }

    private fun hideImageView() {
        binding.apply {
            imageViewDetection.visibility = View.GONE
            btnCancelDetect.visibility = View.GONE

            viewFinder.visibility = View.VISIBLE
            btnSwitchCamera.visibility = View.VISIBLE
            btnPicture.visibility = View.VISIBLE
            eye.visibility = View.VISIBLE
        }

        currentImgUri = null
        startCamera()
    }

    private fun setupActionbar() {
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = getString(R.string.anemia_detection)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            setHomeButtonEnabled(true)
        }

        menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    android.R.id.home -> {
                        requireActivity().supportFragmentManager.popBackStack()
                        return true
                    }
                }

                return false
            }

        }

        requireActivity().addMenuProvider(menuProvider)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            (activity as? MainActivity)?.showLoading(true)
        } else {
            (activity as? MainActivity)?.showLoading(false)
        }
    }

}