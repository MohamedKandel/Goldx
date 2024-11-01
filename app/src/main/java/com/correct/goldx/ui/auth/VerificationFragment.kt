package com.correct.goldx.ui.auth

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.correct.goldx.R
import com.correct.goldx.databinding.FragmentVerificationBinding
import com.correct.goldx.helper.CastException
import com.correct.goldx.helper.Constants.SOURCE
import com.correct.goldx.helper.CustomTypefaceSpan
import com.correct.goldx.helper.FragmentChangeListener
import com.correct.goldx.helper.onBackPressed

class VerificationFragment : Fragment() {

    private lateinit var binding: FragmentVerificationBinding
    private lateinit var changeListener: FragmentChangeListener
    private var source = -1

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
        binding = FragmentVerificationBinding.inflate(inflater, container, false)

        source = arguments?.getInt(SOURCE,-1) ?: -1


        val spannableString = SpannableString(binding.txtResend.text.toString())
        val customTF = Typeface.createFromAsset(requireActivity().assets, "poppins_bold.ttf")

        val startIndex = binding.txtResend.text.indexOf("Resend")
        val endIndex = startIndex + "Resend".length

        spannableString.setSpan(
            CustomTypefaceSpan(customTF),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableString.setSpan(
            UnderlineSpan(),
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // resend OTP again
                Log.d("Clicked mohamed", "ouch")
            }
        }
        spannableString.setSpan(
            clickableSpan,
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.txtResend.text = spannableString
        binding.txtResend.movementMethod = LinkMovementMethod.getInstance()

        onBackPressed {
            findNavController().navigate(R.id.forgetPasswordFragment)
        }

        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.forgetPasswordFragment)
        }

        binding.btnVerify.setOnClickListener {
            if (source == -1) {
                findNavController().navigate(R.id.newPasswordFragment)
            } else {
                findNavController().navigate(R.id.completeProfileFragment)
            }
        }

        binding.verificationLayout.apply {
            txtFirst.requestFocus()
            txtFirst.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1) {
                        txtSecond.requestFocus()
                    }
                }
            })

            txtSecond.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (before > count) {
                        txtFirst.requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1) {
                        txtThird.requestFocus()
                    }
                }
            })

            txtThird.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (before > count) {
                        txtSecond.requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1) {
                        txtFourth.requestFocus()
                    }
                }
            })

            txtFourth.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (before > count) {
                        txtThird.requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })
        }

        return binding.root
    }
}