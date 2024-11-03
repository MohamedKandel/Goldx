package com.correct.goldx.ui.auth

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.correct.goldx.R
import com.correct.goldx.databinding.FragmentNewPasswordBinding
import com.correct.goldx.helper.CastException
import com.correct.goldx.helper.FragmentChangeListener
import com.correct.goldx.helper.hide
import com.correct.goldx.helper.hidePassword
import com.correct.goldx.helper.isContainSpecialCharacter
import com.correct.goldx.helper.onBackPressed
import com.correct.goldx.helper.show
import com.correct.goldx.helper.showPassword

class NewPasswordFragment : Fragment() {

    private lateinit var binding: FragmentNewPasswordBinding
    private lateinit var changeListener: FragmentChangeListener
    private var strength = 0
    private var isLengthAdded = false
    private var isSpecialAdded = false
    private var isLetterAdded = false
    private var isNumberAdded = false
    private lateinit var strengthLiveData: MutableLiveData<Int>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is FragmentChangeListener) {
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
        binding = FragmentNewPasswordBinding.inflate(inflater,container,false)

        strengthLiveData = MutableLiveData(0)

        binding.showIcon.setOnClickListener {
            binding.txtPassword.showPassword()
            binding.showIcon.hide()
            binding.hideIcon.show()
        }

        binding.hideIcon.setOnClickListener {
            binding.txtPassword.hidePassword()
            binding.showIcon.show()
            binding.hideIcon.hide()
        }

        binding.txtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()

                if (password.isEmpty()) {
                    binding.apply {
                        weakIcon.hide()
                        mediumIcon.hide()
                        strongIcon.hide()
                        txtStatus.hide()
                    }
                } else {
                    binding.apply {
                        weakIcon.show()
                        mediumIcon.show()
                        strongIcon.show()
                        txtStatus.show()
                    }
                }

                // check if password length >= 6 characters
                if (password.length >= 6) {
                    binding.passwordLengthIcon.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.success
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.txtPasswordLength.setTextColor(
                        resources.getColor(
                            R.color.success,
                            requireContext().theme
                        )
                    )
                    if (!isLengthAdded) {
                        if (strength < 4) {
                            isLengthAdded = true
                            strength++
                            strengthLiveData.postValue(strength)
                        }
                    }
                } else {
                    binding.passwordLengthIcon.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.txtPasswordLength.setTextColor(
                        resources.getColor(
                            R.color.gray,
                            requireContext().theme
                        )
                    )
                    if (isLengthAdded) {
                        if (strength > 0) {
                            isLengthAdded = false
                            strength--
                            strengthLiveData.postValue(strength)
                        }
                    }
                }

                // check if have number or not
                if (password.any { it.isDigit() }) {
                    binding.passwordNumberIcon.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.success
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.txtNumberCharacter.setTextColor(
                        resources.getColor(
                            R.color.success,
                            requireContext().theme
                        )
                    )

                    if (!isNumberAdded) {
                        if (strength < 4) {
                            strength++
                            isNumberAdded = true
                            strengthLiveData.postValue(strength)
                        }
                    }
                } else {
                    binding.passwordNumberIcon.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.txtNumberCharacter.setTextColor(
                        resources.getColor(
                            R.color.gray,
                            requireContext().theme
                        )
                    )

                    if (isNumberAdded) {
                        if (strength > 0) {
                            isNumberAdded = false
                            strength--
                            strengthLiveData.postValue(strength)
                        }
                    }
                }

                // check if have special character or not
                if (password.isContainSpecialCharacter()) {
                    binding.passwordSpecialIcon.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.success
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.txtSpecialCharacter.setTextColor(
                        resources.getColor(
                            R.color.success,
                            requireContext().theme
                        )
                    )

                    if (!isSpecialAdded) {
                        if (strength < 4) {
                            strength++
                            isSpecialAdded = true
                            strengthLiveData.postValue(strength)
                        }
                    }
                } else {
                    binding.passwordSpecialIcon.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.txtSpecialCharacter.setTextColor(
                        resources.getColor(
                            R.color.gray,
                            requireContext().theme
                        )
                    )

                    if (isSpecialAdded) {
                        if (strength > 0) {
                            isSpecialAdded = false
                            strength--
                            strengthLiveData.postValue(strength)
                        }
                    }
                }

                // check if contain uppercase and lowercase or not
                if (password.any { it.isUpperCase() } && password.any { it.isLowerCase() }) {
                    binding.passwordCapitalIcon.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.success
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.txtCapitalCharacter.setTextColor(
                        resources.getColor(
                            R.color.success,
                            requireContext().theme
                        )
                    )

                    if (!isLetterAdded) {
                        if (strength < 4) {
                            strength++
                            isLetterAdded = true
                            strengthLiveData.postValue(strength)
                        }
                    }
                } else {
                    binding.passwordCapitalIcon.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.txtCapitalCharacter.setTextColor(
                        resources.getColor(
                            R.color.gray,
                            requireContext().theme
                        )
                    )

                    if (isLetterAdded) {
                        if (strength > 0) {
                            strength--
                            isLetterAdded = false
                            strengthLiveData.postValue(strength)
                        }
                    }
                }
            }
        })

        strengthLiveData.observe(viewLifecycleOwner) {
            println(it)
            updateStrengthUI(it)
            when(it) {
                0 -> {
                    binding.txtStatus.text = ""
                }
                1-> {
                    binding.txtStatus.text = resources.getString(R.string.weak)
                    Log.v("Password strength","Weak")
                }
                2-> {
                    binding.txtStatus.text = resources.getString(R.string.medium)
                    Log.v("Password strength","Medium")
                }
                3-> {
                    binding.txtStatus.text = resources.getString(R.string.medium)
                    Log.v("Password strength","Medium")
                }
                4-> {
                    binding.txtStatus.text = resources.getString(R.string.strong)
                    Log.v("Password strength","Strong")
                }
            }
        }

        onBackPressed {
            findNavController().navigate(R.id.forgetPasswordFragment)
        }

        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.forgetPasswordFragment)
        }

        binding.btnChange.setOnClickListener {
            // change it in server
            findNavController().navigate(R.id.passwordChangedFragment)
        }

        return binding.root
    }

    private fun updateStrengthUI(strength:Int) {
        when(strength) {
            0 -> {
                binding.weakIcon.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.normal_strength
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                binding.mediumIcon.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.normal_strength
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                binding.strongIcon.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.normal_strength
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            1 -> {
                binding.weakIcon.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.weak
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                binding.mediumIcon.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.normal_strength
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                binding.strongIcon.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.normal_strength
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            2 -> {
                binding.weakIcon.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.gold
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                binding.mediumIcon.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.gold
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                binding.strongIcon.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.normal_strength
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            3 -> {
                binding.weakIcon.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.gold
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                binding.mediumIcon.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.gold
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                binding.strongIcon.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.normal_strength
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
            4 -> {
                binding.weakIcon.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.success
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                binding.mediumIcon.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.success
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
                binding.strongIcon.setColorFilter(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.success
                    ), android.graphics.PorterDuff.Mode.SRC_IN
                )
            }
        }
    }
}