package com.correct.goldx.ui.review

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import androidx.navigation.fragment.findNavController
import com.correct.goldx.R
import com.correct.goldx.databinding.FragmentCommentsBinding
import com.correct.goldx.helper.CastException
import com.correct.goldx.helper.FragmentChangeListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import me.zhanghai.android.materialratingbar.MaterialRatingBar

class CommentsFragment : Fragment() {

    private lateinit var binding: FragmentCommentsBinding
    private lateinit var changeListener: FragmentChangeListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is FragmentChangeListener) {
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
        binding = FragmentCommentsBinding.inflate(inflater,container,false)

        binding.reviewBtn.setOnClickListener {
            sendReview()
        }

        return binding.root
    }

    private fun sendReview() {
        val btmSheet = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.add_review_bottom_dialog,null)
        val btnSend = view.findViewById<MaterialButton>(R.id.send_review_btn)
        val rateBar = view.findViewById<MaterialRatingBar>(R.id.rating_bar)
        val reviewTxt = view.findViewById<EditText>(R.id.txt_review)

        // buttons click listener here
        btnSend.setOnClickListener {
            val review = reviewTxt.text.toString()
            val rating = rateBar.rating
            // send review here
            btmSheet.cancel()
            btmSheet.dismiss()
        }

        btmSheet.setContentView(view)
        btmSheet.setCancelable(true)
        btmSheet.setCanceledOnTouchOutside(true)
        btmSheet.show()
    }
}