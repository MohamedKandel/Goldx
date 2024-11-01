package com.correct.goldx.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.text.method.PasswordTransformationMethod
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream


val Fragment.CastException: String
    get() = "MainActivity doesn't implement interface"

fun EditText.showPassword() {
    this.transformationMethod = null
    this.setSelection(this.text.length)
}

fun EditText.hidePassword() {
    this.transformationMethod = PasswordTransformationMethod()
    this.setSelection(this.text.length)
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun Activity.isDarkTheme(): Boolean {
    return this.resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
}

fun Fragment.onBackPressed(navFunction: () -> Unit) {
    (this.activity as AppCompatActivity).supportFragmentManager
    this.requireActivity().onBackPressedDispatcher.addCallback(
        this.requireActivity() /* lifecycle owner */,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navFunction()
            }
        })
}

private fun containsAnyElement(array: Array<String>, text: String): Boolean {
    return array.any { text.contains(it) }
}

fun String.isContainSpecialCharacter(): Boolean {
    val special = arrayOf("@", "&", "#", "$", "%")
    return containsAnyElement(special, this)
}

fun Spinner.setSpinnerAdapter(stringArray: MutableList<String>, context: Context) {
    //val items = context.resources.getStringArray(stringArray)
    val spinnerAdapter = object : ArrayAdapter<String>(
        context,
        android.R.layout.simple_spinner_item, stringArray
    ) {

        override fun isEnabled(position: Int): Boolean {
            // Disable the first item from Spinner
            // First item will be used for hint
            return position != 0
        }

        override fun getDropDownView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
        ): View {
            val view: TextView =
                super.getDropDownView(position, convertView, parent) as TextView
            //set the color of first item in the drop down list to gray
            if (position == 0) {
                view.setTextColor(Color.GRAY)
            } else {
                view.setTextColor(Color.BLACK)
            }
            return view
        }
    }
    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    this.adapter = spinnerAdapter

    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            val value = parent!!.getItemAtPosition(position).toString()
            if (value == stringArray[0]) {
                (view as TextView).setTextColor(Color.GRAY)
            }
        }

    }
}

fun String.parseBase64(): Bitmap {
    val decodedString: ByteArray = Base64.decode(this, Base64.DEFAULT)
    val decodedByte: Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    return decodedByte
}

fun Uri.getBase64(context: Context): String {
    return try {
        // Step 1: Get InputStream from URI
        val inputStream: InputStream? = context.contentResolver.openInputStream(this)
        // Step 2: Read bytes from the InputStream
        val byteArrayOutputStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (inputStream?.read(buffer).also { bytesRead = it ?: -1 } != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead)
        }
        inputStream?.close()
        // Step 3: Convert bytes to Base64
        val imageBytes = byteArrayOutputStream.toByteArray()
        Base64.encodeToString(imageBytes, Base64.DEFAULT)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}