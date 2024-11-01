package com.correct.goldx.ui.auth

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.navigation.fragment.findNavController
import com.correct.goldx.R
import com.correct.goldx.databinding.FragmentPasswordChangedBinding
import com.correct.goldx.helper.CastException
import com.correct.goldx.helper.Constants.SOURCE
import com.correct.goldx.helper.FragmentChangeListener
import com.correct.goldx.helper.hide
import com.correct.goldx.helper.onBackPressed
import com.correct.goldx.helper.show

class PasswordChangedFragment : Fragment() {

    private lateinit var binding: FragmentPasswordChangedBinding
    private lateinit var changeListener: FragmentChangeListener
    private lateinit var animation: ObjectAnimator

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
        binding = FragmentPasswordChangedBinding.inflate(inflater, container, false)

        animation = ObjectAnimator.ofFloat(binding.icon, "rotationY", 0.0f, 360f)
        animation.setDuration(700)
        animation.repeatCount = 0
        animation.interpolator = AccelerateDecelerateInterpolator()

        binding.btnLogin.hide()
        Handler(Looper.getMainLooper()).postDelayed({
            binding.btnLogin.show()
        }, 650)

        animation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                animation.cancel()
                animation.removeAllListeners()
                binding.icon.clearAnimation()
                binding.icon.rotationY = 0f
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })

        animation.start()

        onBackPressed {
            requireActivity().finish()
        }

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }


        return binding.root
    }

    override fun onPause() {
        super.onPause()
        animation.cancel()
        binding.icon.clearAnimation()
    }
}