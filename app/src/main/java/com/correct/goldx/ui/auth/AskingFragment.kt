package com.correct.goldx.ui.auth

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.correct.goldx.R
import com.correct.goldx.databinding.FragmentAskingBinding
import com.correct.goldx.helper.CastException
import com.correct.goldx.helper.Constants.SOURCE
import com.correct.goldx.helper.FragmentChangeListener
import com.correct.goldx.helper.onBackPressed

class AskingFragment : Fragment() {

    private lateinit var binding: FragmentAskingBinding
    private lateinit var changeListener: FragmentChangeListener

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
        binding = FragmentAskingBinding.inflate(inflater, container, false)

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }

        onBackPressed {
            requireActivity().finish()
        }

        binding.btnLogin.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(SOURCE, R.id.askingFragment)
            findNavController().navigate(R.id.loginFragment, bundle)
        }

        return binding.root
    }
}