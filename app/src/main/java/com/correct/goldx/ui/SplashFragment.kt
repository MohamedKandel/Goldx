package com.correct.goldx.ui

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.correct.goldx.R
import com.correct.goldx.databinding.FragmentSplashBinding
import com.correct.goldx.helper.CastException
import com.correct.goldx.helper.FragmentChangeListener
import com.correct.goldx.helper.isDarkTheme
import com.correct.goldx.helper.onBackPressed

class SplashFragment : Fragment() {

    private val TAG: String = "SplashScreen Mohamed"
    private lateinit var binding: FragmentSplashBinding
    private var milliFinished: Long = 0
    private var countDownTimer: CountDownTimer? = null
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
        binding = FragmentSplashBinding.inflate(inflater,container,false)

        binding.root.keepScreenOn = true

        if (requireActivity().isDarkTheme()) {
            binding.splash.setImageResource(R.drawable.splash_dark)
            binding.main.setBackgroundColor(resources.getColor(R.color.black,requireContext().theme))
        } else {
            binding.splash.setImageResource(R.drawable.splash_light)
            binding.main.setBackgroundColor(resources.getColor(R.color.background_light,requireContext().theme))
        }

        startSplash()

        onBackPressed {
            requireActivity().finish()
        }

        return binding.root
    }

    private fun startSplash() {
        countDownTimer = object : CountDownTimer(2500, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                milliFinished = millisUntilFinished
                Log.i(TAG, "onTick: millisUntilFinished=$millisUntilFinished")
            }

            override fun onFinish() {
                // navigate to asking
                findNavController().navigate(R.id.askingFragment)
                /*if (helper.isFirstStartApp(requireContext())) {
                    findNavController().navigate(R.id.onBoardingFragment)
                } else {
                    findNavController().navigate(R.id.registerFragment)
                }*/
            }
        }.start()
    }

    override fun onDetach() {
        super.onDetach()
        countDownTimer?.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
    }
}