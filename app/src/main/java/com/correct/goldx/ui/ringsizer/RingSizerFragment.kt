package com.correct.goldx.ui.ringsizer

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import com.correct.goldx.R
import com.correct.goldx.databinding.FragmentRingSizerBinding
import com.correct.goldx.helper.CastException
import com.correct.goldx.helper.Constants.MmSizes
import com.correct.goldx.helper.Constants.RingSizes
import com.correct.goldx.helper.Constants.RingValues
import com.correct.goldx.helper.FragmentChangeListener
import com.correct.goldx.helper.dpToPx

class RingSizerFragment : Fragment() {

    private lateinit var binding: FragmentRingSizerBinding
    private lateinit var changeListener: FragmentChangeListener
    private var sizeInDp: Double = 0.0

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

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRingSizerBinding.inflate(inflater, container, false)

        binding.seekbar.max = RingValues.size - 1

        binding.btnAdd.setOnClickListener {
            if (binding.seekbar.progress < binding.seekbar.max) {
                binding.seekbar.progress++
                val progress = binding.seekbar.progress
                Log.v("Current value","${RingValues[progress]}")
                sizeInDp = RingValues[progress]
                //Log.v("Current value mohamed", "$currentValue")
                val sizeInMm = MmSizes[progress]
                binding.txtSize.text = "${sizeInMm}mm"
                binding.txtRingSize.text = RingSizes[sizeInDp]?.get(0) ?: "-"
                resizeCircleInMm(sizeInDp)
            }
        }

        binding.btnShow.setOnClickListener {
            // initial state
            if (sizeInDp == 0.0) {
                sizeInDp = 88.63
            }
            val result = RingSizes[sizeInDp]
            if (result != null) {
                for (r in result) {
                    println(r)
                }
            }
        }

        binding.btnMinus.setOnClickListener {
            if (binding.seekbar.progress > 0) {
                binding.seekbar.progress--

                val progress = binding.seekbar.progress
                sizeInDp = RingValues[progress]
                //Log.v("Current value mohamed", "$currentValue")
                val sizeInMm = MmSizes[progress]
                binding.txtSize.text = "${sizeInMm}mm"
                binding.txtRingSize.text = RingSizes[sizeInDp]?.get(0) ?: "-"
                resizeCircleInMm(sizeInDp)
            }
        }

        binding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    sizeInDp = RingValues[progress]
                    //Log.v("Current value mohamed", "$currentValue")
                    val sizeInMm = MmSizes[progress]
                    binding.txtSize.text = "${sizeInMm}mm"
                    binding.txtRingSize.text = RingSizes[sizeInDp]?.get(0) ?: "-"
                    resizeCircleInMm(sizeInDp)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        return binding.root
    }

    fun resizeCircleInMm(sizeInDp: Double) {
        val sizeInPx = dpToPx(sizeInDp.toFloat())
        val layoutParams = binding.ringIcon.layoutParams
        layoutParams.width =
            sizeInPx.toInt()  // This may round the value slightly, but it's still responsive
        layoutParams.height = sizeInPx.toInt()
        // Update the view with new layout parameters
        binding.ringIcon.layoutParams = layoutParams
    }


}