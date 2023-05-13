package com.nurfauzi.storyapp.presentation.add

import android.Manifest.permission.CAMERA
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.nurfauzi.storyapp.data.StoryResult
import com.nurfauzi.storyapp.data.preferences.LoginPreferences
import com.nurfauzi.storyapp.data.preferences.ViewModelFactory
import com.nurfauzi.storyapp.databinding.ActivityAddStoryBinding
import com.nurfauzi.storyapp.domain.User
import com.nurfauzi.storyapp.utils.reduceFileImage
import com.nurfauzi.storyapp.utils.rotateFile
import com.nurfauzi.storyapp.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "token")

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var addStoryViewModel: AddStoryViewModel

    private lateinit var desc: String
    private lateinit var description: RequestBody
    private lateinit var imageMultipart: MultipartBody.Part
    private lateinit var token: String
    private var check: Boolean = false
    private lateinit var user: User

    private var getFile: File? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        val pref = LoginPreferences.getInstance(dataStore)
        val factory = ViewModelFactory.getInstance(this, pref)
        addStoryViewModel = ViewModelProvider(this, factory).get(
            AddStoryViewModel::class.java
        )

        addStoryViewModel.getToken().observe(this) {
            user = it
            token = user.token
        }

        addStoryViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.btnCamera.setOnClickListener { startCameraX() }

        binding.btnGaleri.setOnClickListener { startGallery() }

        binding.buttonAdd.setOnClickListener {
            desc = binding.edAddDescription.text.toString()
            when {
                desc == "" -> {
                    Toast.makeText(
                        this@AddStoryActivity,
                        "Isi deskripsi dahulu!.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                else -> {
                    uploadImage()
                }
            }
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            myFile?.let { file ->
                val reduceFile = reduceFileImage(file)
                rotateFile(file, isBackCamera)
                val requestImageFile = reduceFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                imageMultipart =
                    MultipartBody.Part.createFormData(
                        "photo",
                        file.name,
                        requestImageFile
                    )
                check = true
                binding.previewImageView.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@AddStoryActivity)
                getFile = myFile
                val requestImageFile = myFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                imageMultipart =
                    MultipartBody.Part.createFormData(
                        "photo",
                        myFile.name,
                        requestImageFile
                    )
                binding.previewImageView.setImageURI(uri)
                check = true
            }
        }
    }

    private fun uploadImage() {
        description = desc.toRequestBody("text/plain".toMediaType())
        Log.d("IMAGE", imageMultipart.toString())
        Log.d("DESCRIPTION", description.toString())
        Log.d("token", token)
        if (check) {
            addStoryViewModel.postStory(imageMultipart, description, token)
                .observe(this) { result ->
                    when (result) {
                        is StoryResult.Success -> {
                            showLoading(false)
                            Toast.makeText(this, "Upload Berhasil", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        is StoryResult.Loading -> {
                            showLoading(true)
                        }
                        is StoryResult.Error -> {
                            Toast.makeText(this, "Upload gagal.", Toast.LENGTH_SHORT).show()
                            showLoading(false)
                        }
                    }

                }

        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            finish()
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}