package com.correct.goldx.ui.ringsizer

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.correct.goldx.R
import com.correct.goldx.databinding.FragmentHaveRingBinding
import com.correct.goldx.helper.CastException
import com.correct.goldx.helper.FragmentChangeListener

class HaveRingFragment : Fragment() {

    private lateinit var binding: FragmentHaveRingBinding
    private lateinit var changeListener: FragmentChangeListener
    private var selectedItem = -1

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
        binding = FragmentHaveRingBinding.inflate(inflater,container,false)

        // check first if theme is dark or light and then select drawable
        val selectedStroke = ContextCompat.getDrawable(requireContext(),R.drawable.gradient_stroke)
        val unSelectedStroke = ContextCompat.getDrawable(requireContext(),R.drawable.gradient_stroke_alpha)

        binding.haveRingLayout.setOnClickListener {
            selectedItem = 0
            binding.haveRingOption.isChecked = true
            binding.haveRingLayout.background = selectedStroke

            binding.measureOption.isChecked = false
            binding.measureLayout.background = unSelectedStroke
        }

        binding.haveRingOption.setOnClickListener {
            selectedItem = 0
            binding.haveRingOption.isChecked = true
            binding.haveRingLayout.background = selectedStroke

            binding.measureOption.isChecked = false
            binding.measureLayout.background = unSelectedStroke
        }

        binding.measureOption.setOnClickListener {
            selectedItem = 1
            binding.haveRingOption.isChecked = false
            binding.haveRingLayout.background = unSelectedStroke

            binding.measureOption.isChecked = true
            binding.measureLayout.background = selectedStroke
        }

        binding.measureLayout.setOnClickListener {
            selectedItem = 1
            binding.haveRingOption.isChecked = false
            binding.haveRingLayout.background = unSelectedStroke

            binding.measureOption.isChecked = true
            binding.measureLayout.background = selectedStroke
        }

        return binding.root
    }
}