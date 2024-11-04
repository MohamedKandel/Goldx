package com.correct.goldx.ui

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.correct.goldx.R
import com.correct.goldx.databinding.FragmentSplashBinding
import com.correct.goldx.helper.CastException
import com.correct.goldx.helper.Constants.FINAL_STEP
import com.correct.goldx.helper.Constants.LOGGED_IN
import com.correct.goldx.helper.Constants.PROFILE_COMPLETED
import com.correct.goldx.helper.Constants.REMEMBER
import com.correct.goldx.helper.FragmentChangeListener
import com.correct.goldx.helper.isDarkTheme
import com.correct.goldx.helper.onBackPressed
import com.mkandeel.datastore.DataStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private val TAG: String = "SplashScreen Mohamed"
    private lateinit var binding: FragmentSplashBinding
    private lateinit var changeListener: FragmentChangeListener
    private lateinit var dataStorage: DataStorage
    private var timeUp = false

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
        if (timeUp) {
            lifecycleScope.launch {
                val isCompleted =
                    dataStorage.getBoolean(requireContext(), PROFILE_COMPLETED) ?: false
                val isRemember = dataStorage.getBoolean(requireContext(), REMEMBER) ?: false
                if (isCompleted) {
                    if (isRemember) {
                        findNavController().navigate(R.id.homeFragment)
                    } else {
                        findNavController().navigate(R.id.loginFragment)
                    }
                } else {
                    val step = dataStorage.getInt(requireContext(), FINAL_STEP) ?: -1
                    when (step) {
                        0 -> {
                            findNavController().navigate(R.id.registerFragment)
                        }

                        1 -> {
                            findNavController().navigate(R.id.verificationFragment)
                        }

                        2 -> {
                            findNavController().navigate(R.id.completeProfileFragment)
                        }

                        3 -> {
                            findNavController().navigate(R.id.homeFragment)
                        }

                        else -> {
                            findNavController().navigate(R.id.askingFragment)
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        dataStorage = DataStorage.getInstance(requireContext())

        binding.root.keepScreenOn = true

        if (requireActivity().isDarkTheme()) {
            binding.splash.setImageResource(R.drawable.splash_dark)
            binding.main.setBackgroundColor(
                resources.getColor(
                    R.color.black,
                    requireContext().theme
                )
            )
        } else {
            binding.splash.setImageResource(R.drawable.splash_light)
            binding.main.setBackgroundColor(
                resources.getColor(
                    R.color.background_light,
                    requireContext().theme
                )
            )
        }

        startSplash()

        onBackPressed {
            requireActivity().finish()
        }

        return binding.root
    }

    private fun startSplash() {
        lifecycleScope.launch {
            delay(2500)
            timeUp = true
            val isCompleted =
                dataStorage.getBoolean(requireContext(), PROFILE_COMPLETED) ?: false
            val isLogged = dataStorage.getBoolean(requireContext(), LOGGED_IN) ?: false
            if (isResumed) {
                if (isCompleted) {
                    if (isLogged) {
                        findNavController().navigate(R.id.homeFragment)
                    } else {
                        findNavController().navigate(R.id.loginFragment)
                    }
                } else {
                    val step = dataStorage.getInt(requireContext(), FINAL_STEP) ?: -1
                    when (step) {
                        0 -> {
                            findNavController().navigate(R.id.registerFragment)
                        }

                        1 -> {
                            findNavController().navigate(R.id.verificationFragment)
                        }

                        2 -> {
                            findNavController().navigate(R.id.completeProfileFragment)
                        }

                        3 -> {
                            findNavController().navigate(R.id.homeFragment)
                        }

                        else -> {
                            findNavController().navigate(R.id.askingFragment)
                        }
                    }
                }
            }
        }
    }
}