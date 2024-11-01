package com.correct.goldx.ui.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.correct.goldx.R
import com.correct.goldx.databinding.FragmentHomeBinding
import com.correct.goldx.helper.CastException
import com.correct.goldx.helper.FragmentChangeListener
import com.correct.goldx.helper.onBackPressed

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

        return binding.root
    }

}