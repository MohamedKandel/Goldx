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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.correct.goldx.R
import com.correct.goldx.data.auth.Address
import com.correct.goldx.data.auth.RegisterBody
import com.correct.goldx.databinding.FragmentUploadPhotoBinding
import com.correct.goldx.helper.CastException
import com.correct.goldx.helper.Constants.FINAL_STEP
import com.correct.goldx.helper.Constants.OBJECT
import com.correct.goldx.helper.Constants.SOURCE
import com.correct.goldx.helper.FragmentChangeListener
import com.correct.goldx.helper.getBase64
import com.correct.goldx.helper.onBackPressed
import com.correct.goldx.helper.parseBase64
import com.correct.goldx.helper.toast
import com.correct.goldx.room.User
import com.correct.goldx.room.UsersDB
import com.mkandeel.datastore.DataStorage
import kotlinx.coroutines.launch
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


class UploadPhotoFragment : Fragment() {

    private lateinit var binding: FragmentUploadPhotoBinding
    private lateinit var changeListener: FragmentChangeListener
    private lateinit var base64: String
    private lateinit var viewModel: AuthViewModel
    private lateinit var usersDB: UsersDB
    private lateinit var dataStorage: DataStorage

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
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        usersDB = UsersDB.getDBInstance(requireContext())
        dataStorage = DataStorage.getInstance(requireContext())

        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }

        onBackPressed {
            findNavController().navigate(R.id.registerFragment)
        }

        val model = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(OBJECT, RegisterBody::class.java)
        } else {
            requireArguments().getParcelable(OBJECT)
        }

        binding.btnUpload.setOnClickListener {
            if (this::base64.isInitialized) {
                Log.d("Image Base64", base64)
                if (model != null) {
                    model.image = base64

                    Log.d("sent mohamed Image", model.image!!)
                    Log.d("sent mohamed Name", model.name)
                    Log.d("sent mohamed Phone", model.phoneNumber)
                    Log.d("sent mohamed email", model.email)
                    Log.d("sent mohamed password", model.password)
                    Log.d("sent mohamed country", model.address.country)
                    Log.d("sent mohamed city", model.address.city)
                    Log.d("sent mohamed street", model.address.street)
                    Log.d("sent mohamed building", "${model.address.noBuilding}")

                    registerUser(model)
                }
            }
        }

        binding.btnSkip.setOnClickListener {
            if (model != null) {
                registerUser(
                    RegisterBody(
                        name = model.name,
                        email = model.email, password = model.password,
                        phoneNumber = model.phoneNumber,
                        address = Address(
                            city = model.address.city,
                            country = model.address.country,
                            street = model.address.street,
                            noBuilding = model.address.noBuilding
                        )
                    )
                )
            }
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

    private fun registerUser(model: RegisterBody) {
        viewModel.registerUser(model)
        val observer = object : Observer<Boolean> {
            override fun onChanged(value: Boolean) {
                if (value) {
                    lifecycleScope.launch {
                        val user = User(
                            "1", model.name, model.email,
                            model.phoneNumber, model.password, model.address.country,
                            model.address.city, model.address.street, model.address.noBuilding
                        )

                        usersDB.dao().insert(user)

                        dataStorage.putInt(requireContext(), FINAL_STEP,1)

                        val bundle = Bundle()
                        bundle.putInt(SOURCE, R.id.uploadPhotoFragment)
                        findNavController().navigate(R.id.verificationFragment, bundle)
                    }
                } else {
                    toast(resources.getString(R.string.failed_register))
                }
                viewModel.isRegisteredResponse.removeObserver(this)
            }
        }
        viewModel.isRegisteredResponse.observe(viewLifecycleOwner, observer)
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