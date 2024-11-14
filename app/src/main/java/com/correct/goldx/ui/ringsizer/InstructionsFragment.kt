package com.correct.goldx.ui.ringsizer

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.correct.goldx.R
import com.correct.goldx.adapter.ViewPagerAdapter
import com.correct.goldx.databinding.FragmentInstructionsBinding
import com.correct.goldx.helper.CastException
import com.correct.goldx.helper.FragmentChangeListener
import com.correct.goldx.helper.hide
import com.correct.goldx.helper.show

class InstructionsFragment : Fragment() {

    private lateinit var binding: FragmentInstructionsBinding
    private lateinit var changeListener: FragmentChangeListener
    private var pageNumber = 0
    private val StepsCount = 2

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
        binding = FragmentInstructionsBinding.inflate(inflater, container, false)

        val fragmentList = arrayListOf(
            FirstStepFragment(),
            SecondStepFragment(),
            ThirdStepFragment(),
            FourthStepFragment()
        )

        val adapter = ViewPagerAdapter(
            requireActivity().supportFragmentManager,
            lifecycle, fragmentList
        )

        binding.startBtn.setOnClickListener {
            findNavController().navigate(R.id.haveRingFragment)
        }

        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = pageNumber

        binding.btnNext.setOnClickListener {
            if (pageNumber <= StepsCount) {
                pageNumber++
                binding.viewPager.currentItem = pageNumber
            } else {
                // finished
            }
            displayStartBtn(pageNumber)
            Log.v("Page number", "$pageNumber")
        }

        binding.btnPrev.setOnClickListener {
            if (pageNumber >= 1) {
                pageNumber--
                binding.viewPager.currentItem = pageNumber
            } else {
                // no previous pages
            }
            displayStartBtn(pageNumber)
            Log.v("Page number", "$pageNumber")
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                Log.d("page number", "$position")
                pageNumber = position
                displayStartBtn(pageNumber)
            }
        })

        return binding.root
    }

    private fun displayStartBtn(pageNumber: Int) {
        if (pageNumber == 3) {
            binding.btnPrev.hide()
            binding.btnNext.hide()
            binding.startBtn.show()
        } else {
            binding.btnPrev.show()
            binding.btnNext.show()
            binding.startBtn.hide()
        }
    }

}