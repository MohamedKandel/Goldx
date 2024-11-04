package com.correct.goldx.ui.auth

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.correct.goldx.R
import com.correct.goldx.data.auth.ConfirmEmailBody
import com.correct.goldx.data.auth.ConfirmEmailResponse
import com.correct.goldx.databinding.FragmentVerificationBinding
import com.correct.goldx.helper.CastException
import com.correct.goldx.helper.Constants.FINAL_STEP
import com.correct.goldx.helper.Constants.SOURCE
import com.correct.goldx.helper.CustomTypefaceSpan
import com.correct.goldx.helper.FragmentChangeListener
import com.correct.goldx.helper.onBackPressed
import com.correct.goldx.helper.toast
import com.correct.goldx.room.UsersDB
import com.mkandeel.datastore.DataStorage
import kotlinx.coroutines.launch

class VerificationFragment : Fragment() {

    private lateinit var binding: FragmentVerificationBinding
    private lateinit var changeListener: FragmentChangeListener
    private var source = -1
    private lateinit var sb: StringBuilder
    private lateinit var viewModel: AuthViewModel
    private lateinit var usersDB: UsersDB
    private lateinit var dataStore: DataStorage

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
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        usersDB = UsersDB.getDBInstance(requireContext())
        dataStore = DataStorage.getInstance(requireContext())

        source = arguments?.getInt(SOURCE, -1) ?: -1

        sb = StringBuilder("000000")

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
                lifecycleScope.launch {
                    val user = usersDB.dao().getOneUser("1")
                    if (user != null) {
                        resend(user.mail)
                    }
                }
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

        disableResend()

        onBackPressed {
            findNavController().navigate(R.id.forgetPasswordFragment)
        }

        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.forgetPasswordFragment)
        }

        binding.btnVerify.setOnClickListener {
            Log.d("Verification code", sb.toString())
            if (source == -1) {
                findNavController().navigate(R.id.newPasswordFragment)
            } else {
                Log.d("Verification code", sb.toString())
                // verify account
                lifecycleScope.launch {
                    val user = usersDB.dao().getOneUser("1")
                    if (user != null) {
                        verify(ConfirmEmailBody(user.mail, sb.toString()))
                    }
                }
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
                        sb.setCharAt(0, s.toString()[0])
                        //sb.insert(0,s.toString())
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
                        sb.setCharAt(1, s.toString()[0])
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
                        sb.setCharAt(2, s.toString()[0])
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
                    if (s?.length == 1) {
                        sb.setCharAt(3, s.toString()[0])
                        txtFifth.requestFocus()
                    }
                }
            })

            txtFifth.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (before > count) {
                        sb.insert(3, "")
                        txtFourth.requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1) {
                        sb.setCharAt(4, s.toString()[0])
                        txtSixth.requestFocus()
                    }
                }
            })

            txtSixth.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (before > count) {
                        txtFifth.requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1) {
                        sb.setCharAt(5, s.toString()[0])
                    }
                }
            })
        }

        return binding.root
    }

    private fun resend(email: String) {
        viewModel.resendOTP(email)
        val observer = object : Observer<Boolean> {
            override fun onChanged(value: Boolean) {
                if (value) {
                    disableResend()
                } else {
                    toast(resources.getString(R.string.error_occurred))
                }
                viewModel.isResendOTPResponse.removeObserver(this)
            }
        }
        viewModel.isResendOTPResponse.observe(viewLifecycleOwner, observer)
    }

    private fun verify(body: ConfirmEmailBody) {
        viewModel.confirmEMail(body)
        val observer = object : Observer<ConfirmEmailResponse> {
            override fun onChanged(value: ConfirmEmailResponse) {
                if (value.success) {
                    lifecycleScope.launch {
                        dataStore.removeIntValue(requireContext(), FINAL_STEP)
                        dataStore.putInt(requireContext(), FINAL_STEP, 2)
                    }
                    findNavController().navigate(R.id.completeProfileFragment)
                } else {
                    toast(value.errorMessage!!)
                }
                viewModel.confirmMailResponse.removeObserver(this)
            }
        }
        viewModel.confirmMailResponse.observe(viewLifecycleOwner, observer)
    }

    private fun disableResend() {
        binding.txtResend.isEnabled = false
        binding.txtResend.isClickable = false

        Handler(Looper.getMainLooper()).postDelayed({
            binding.txtResend.isClickable = true
            binding.txtResend.isEnabled = true
        }, 3000)
    }
}