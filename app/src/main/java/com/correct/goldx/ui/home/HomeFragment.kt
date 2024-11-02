package com.correct.goldx.ui.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.correct.goldx.R
import com.correct.goldx.databinding.FragmentHomeBinding
import com.correct.goldx.helper.CastException
import com.correct.goldx.helper.FragmentChangeListener
import com.correct.goldx.helper.onBackPressed
import com.google.android.material.bottomsheet.BottomSheetDialog

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
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
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        onBackPressed {
            requireActivity().finish()
        }

        binding.contactIcon.setOnClickListener {
            // contact us bottom sheet
            contactUs()
        }

        return binding.root
    }

    private fun contactUs() {
        val btmSheet = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.contact_us_btmsheet,null)

        val wa = view.findViewById<ImageView>(R.id.wa_icon)
        val chat = view.findViewById<ImageView>(R.id.chat_icon)
        val close = view.findViewById<ImageView>(R.id.close_icon)
        // buttons click listener here
        chat.setOnClickListener {
            btmSheet.dismiss()
            btmSheet.cancel()
            findNavController().navigate(R.id.contactUsFragment)
        }

        wa.setOnClickListener {

        }

        close.setOnClickListener {
            btmSheet.dismiss()
            btmSheet.cancel()
        }

        btmSheet.setContentView(view)
        btmSheet.setCancelable(true)
        btmSheet.setCanceledOnTouchOutside(true)
        btmSheet.show()
    }

}