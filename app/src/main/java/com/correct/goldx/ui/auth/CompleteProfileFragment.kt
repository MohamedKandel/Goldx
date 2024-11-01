package com.correct.goldx.ui.auth

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.correct.goldx.R
import com.correct.goldx.databinding.FragmentCompleteProfileBinding
import com.correct.goldx.helper.CastException
import com.correct.goldx.helper.FragmentChangeListener
import com.correct.goldx.helper.onBackPressed

class CompleteProfileFragment : Fragment() {

    private lateinit var binding: FragmentCompleteProfileBinding
    private lateinit var changeListener: FragmentChangeListener
    private var countrySelected = ""
    private var languageSelected = ""

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
        binding = FragmentCompleteProfileBinding.inflate(inflater,container,false)

        onBackPressed {
            findNavController().navigate(R.id.registerFragment)
        }

        binding.backBtn.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }

        binding.greeceCard.setOnClickListener {
            selectGreece()
        }

        binding.greeceRadio.setOnClickListener {
            selectGreece()
        }

        binding.egyptRadio.setOnClickListener {
            selectEgypt()
        }

        binding.egyptCard.setOnClickListener {
            selectEgypt()
        }

        binding.englishCard.setOnClickListener {
            selectEng()
        }

        binding.englishRadio.setOnClickListener {
            selectEng()
        }

        binding.arabicCard.setOnClickListener {
            selectAr()
        }

        binding.arabicRadio.setOnClickListener {
            selectAr()
        }

        binding.greekCard.setOnClickListener {
            selectGreek()
        }

        binding.greekRadio.setOnClickListener {
            selectGreek()
        }


        return binding.root
    }

    private fun selectGreece() {
        binding.egyptCard.setStrokeColor(ColorStateList.valueOf(resources.getColor(R.color.border_color,requireContext().theme)))
        binding.greeceCard.setStrokeColor(ColorStateList.valueOf(resources.getColor(R.color.black,requireContext().theme)))
        binding.greeceRadio.isChecked = true
        binding.egyptRadio.isChecked = false
        countrySelected = "gr"
    }

    private fun selectEgypt() {
        binding.egyptCard.setStrokeColor(ColorStateList.valueOf(resources.getColor(R.color.black,requireContext().theme)))
        binding.greeceCard.setStrokeColor(ColorStateList.valueOf(resources.getColor(R.color.border_color,requireContext().theme)))
        binding.greeceRadio.isChecked = false
        binding.egyptRadio.isChecked = true
        countrySelected = "eg"
    }

    private fun selectEng() {
        binding.englishCard.setStrokeColor(ColorStateList.valueOf(resources.getColor(R.color.black,requireContext().theme)))
        binding.arabicCard.setStrokeColor(ColorStateList.valueOf(resources.getColor(R.color.border_color,requireContext().theme)))
        binding.greekCard.setStrokeColor(ColorStateList.valueOf(resources.getColor(R.color.border_color,requireContext().theme)))

        binding.englishRadio.isChecked = true
        binding.arabicRadio.isChecked = false
        binding.greekRadio.isChecked = false

        languageSelected = "en"
    }

    private fun selectAr() {
        binding.englishCard.setStrokeColor(ColorStateList.valueOf(resources.getColor(R.color.border_color,requireContext().theme)))
        binding.arabicCard.setStrokeColor(ColorStateList.valueOf(resources.getColor(R.color.black,requireContext().theme)))
        binding.greekCard.setStrokeColor(ColorStateList.valueOf(resources.getColor(R.color.border_color,requireContext().theme)))

        binding.englishRadio.isChecked = false
        binding.arabicRadio.isChecked = true
        binding.greekRadio.isChecked = false

        languageSelected = "ar"
    }

    private fun selectGreek() {
        binding.englishCard.setStrokeColor(ColorStateList.valueOf(resources.getColor(R.color.border_color,requireContext().theme)))
        binding.arabicCard.setStrokeColor(ColorStateList.valueOf(resources.getColor(R.color.border_color,requireContext().theme)))
        binding.greekCard.setStrokeColor(ColorStateList.valueOf(resources.getColor(R.color.black,requireContext().theme)))

        binding.englishRadio.isChecked = false
        binding.arabicRadio.isChecked = false
        binding.greekRadio.isChecked = true

        languageSelected = "gr"
    }
}