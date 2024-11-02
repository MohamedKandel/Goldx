package com.correct.goldx.ui.categories

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.correct.goldx.R
import com.correct.goldx.databinding.FragmentMainCategoriesBinding
import com.correct.goldx.helper.CastException
import com.correct.goldx.helper.FragmentChangeListener

class MainCategoriesFragment : Fragment() {

    private lateinit var binding: FragmentMainCategoriesBinding
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
        binding = FragmentMainCategoriesBinding.inflate(inflater,container,false)

        return binding.root
    }
}