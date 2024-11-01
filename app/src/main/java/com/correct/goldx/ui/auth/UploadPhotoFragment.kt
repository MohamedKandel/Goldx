package com.correct.goldx.ui.auth

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.correct.goldx.R
import com.correct.goldx.databinding.FragmentUploadPhotoBinding
import com.correct.goldx.helper.CastException
import com.correct.goldx.helper.FragmentChangeListener
import com.correct.goldx.helper.getBase64
import com.correct.goldx.helper.onBackPressed
import com.correct.goldx.helper.parseBase64
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


class UploadPhotoFragment : Fragment() {

    private lateinit var binding: FragmentUploadPhotoBinding
    private lateinit var changeListener: FragmentChangeListener
    private lateinit var base64: String

    private val arl: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val dataIntent = it.data
            if (dataIntent != null) {
                val uri = dataIntent.data
                if (uri != null) {
                    val correctedBitmap = getCorrectedBitmap(requireContext(), uri)
                    binding.uploadPic.setImageBitmap(correctedBitmap) // Display the rotated image in the ImageView
                    //Log.d("Image Base64",uri.getBase64(requireContext()))
                    base64 = uri.getBase64(requireContext())
                }
            }
        }
    }

    private val permissions: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
        ActivityResultCallback<Boolean> {
            if (it) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                arl.launch(intent)
            }
        }
    )


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentChangeListener) {
            changeListener = context
        } else {
            throw ClassCastException(CastException)
        }
    }

    override fun onResume() {
        super.onResume()
        changeListener.onFragmentChangeListener(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentUploadPhotoBinding.inflate(inflater, container, false)

        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }

        onBackPressed {
            findNavController().navigate(R.id.registerFragment)
        }

        binding.btnUpload.setOnClickListener {
            if (this::base64.isInitialized) {
                Log.d("Image Base64", base64)
            }
            //findNavController().navigate(R.id.verificationFragment)
        }

        binding.uploadPic.setOnClickListener {
            // open gallery
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissions.launch(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                permissions.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }


        return binding.root
    }

    // Load and correct image orientation
    private fun getCorrectedBitmap(context: Context, imageUri: Uri): Bitmap? {
        val contentResolver: ContentResolver = context.contentResolver

        // Get bitmap from URI
        val inputStream = contentResolver.openInputStream(imageUri) ?: return null
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()

        // Check for EXIF data and get orientation
        val exifStream = contentResolver.openInputStream(imageUri) ?: return bitmap
        val exifInterface = ExifInterface(exifStream)
        val orientation = exifInterface.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        exifStream.close()

        // Rotate the bitmap if necessary
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
            else -> bitmap
        }
    }

    // Rotate bitmap function
    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}