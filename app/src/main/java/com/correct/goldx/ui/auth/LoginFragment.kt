package com.correct.goldx.ui.auth

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.correct.goldx.R
import com.correct.goldx.databinding.FragmentLoginBinding
import com.correct.goldx.helper.CastException
import com.correct.goldx.helper.Constants.SOURCE
import com.correct.goldx.helper.FragmentChangeListener
import com.correct.goldx.helper.hide
import com.correct.goldx.helper.hidePassword
import com.correct.goldx.helper.onBackPressed
import com.correct.goldx.helper.show
import com.correct.goldx.helper.showPassword

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var changeListener: FragmentChangeListener
    private var sourceFragment = -1

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
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        sourceFragment = arguments?.getInt(SOURCE, -1) ?: -1

        onBackPressed {
            back(sourceFragment)
        }

        binding.backBtn.setOnClickListener {
            back(sourceFragment)
        }

        binding.btnSignUp.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(SOURCE, R.id.loginFragment)
            findNavController().navigate(R.id.registerFragment, bundle)
        }

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

        binding.txtForget.setOnClickListener {
            findNavController().navigate(R.id.forgetPasswordFragment)
        }

        return binding.root
    }

    private fun back(source: Int) {
        if (source != -1) {
            findNavController().navigate(source)
        } else {
            requireActivity().finish()
        }
    }
}