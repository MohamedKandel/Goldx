package com.correct.goldx.ui.auth

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.correct.goldx.R
import com.correct.goldx.databinding.FragmentRegisterBinding
import com.correct.goldx.helper.CastException
import com.correct.goldx.helper.Constants.SOURCE
import com.correct.goldx.helper.FragmentChangeListener
import com.correct.goldx.helper.hide
import com.correct.goldx.helper.hidePassword
import com.correct.goldx.helper.isContainSpecialCharacter
import com.correct.goldx.helper.onBackPressed
import com.correct.goldx.helper.setSpinnerAdapter
import com.correct.goldx.helper.show
import com.correct.goldx.helper.showPassword
import me.srodrigo.androidhintspinner.HintAdapter
import me.srodrigo.androidhintspinner.HintSpinner

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var changeListener: FragmentChangeListener
    private var sourceFragment = -1
    private lateinit var viewModel: CitiesViewModel
    private lateinit var cities: MutableList<String>
    private lateinit var arrayAdapter: ArrayAdapter<String>
    //private lateinit var hintAdapter: HintAdapter<String>

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
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[CitiesViewModel::class.java]
        cities = mutableListOf()

        arrayAdapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            cities
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                // Set color of the first item to gray to indicate it is disabled
                if (position == 0) {
                    view.setTextColor(Color.GRAY)
                }
                return view
            }
        }

        getCities("egypt", false)

        /*binding.citySpn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    (view as TextView).setTextColor(Color.GRAY)
                }
                Log.d("Selected city", cities[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("Selected city", "Nothing selected")
            }

        }

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.citySpn.adapter = arrayAdapter //hintAdapter //arrayAdapter

        binding.citySpn.setSpinnerAdapter(cities, requireContext())*/

        sourceFragment = arguments?.getInt(SOURCE, -1) ?: -1

        onBackPressed {
            back(sourceFragment)
        }

        binding.backBtn.setOnClickListener {
            back(sourceFragment)
        }

        binding.btnLogin.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(SOURCE, R.id.registerFragment)
            findNavController().navigate(R.id.loginFragment, bundle)
        }

        binding.txtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                // check if password length >= 6 characters
                if (password.length >= 6) {
                    binding.passwordLengthIcon.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.success
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.txtPasswordLength.setTextColor(
                        resources.getColor(
                            R.color.success,
                            requireContext().theme
                        )
                    )
                } else {
                    binding.passwordLengthIcon.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.txtPasswordLength.setTextColor(
                        resources.getColor(
                            R.color.gray,
                            requireContext().theme
                        )
                    )
                }

                // check if have special character or not
                if (password.isContainSpecialCharacter()) {
                    binding.passwordSpecialIcon.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.success
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.txtSpecialCharacter.setTextColor(
                        resources.getColor(
                            R.color.success,
                            requireContext().theme
                        )
                    )
                } else {
                    binding.passwordSpecialIcon.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.txtSpecialCharacter.setTextColor(
                        resources.getColor(
                            R.color.gray,
                            requireContext().theme
                        )
                    )
                }

                // check if contain uppercase and lowercase or not
                if (password.any { it.isUpperCase() } && password.any { it.isLowerCase() }) {
                    binding.passwordCapitalIcon.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.success
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.txtCapitalCharacter.setTextColor(
                        resources.getColor(
                            R.color.success,
                            requireContext().theme
                        )
                    )
                } else {
                    binding.passwordCapitalIcon.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.txtCapitalCharacter.setTextColor(
                        resources.getColor(
                            R.color.gray,
                            requireContext().theme
                        )
                    )
                }
            }
        })

        binding.showIcon.setOnClickListener {
            binding.txtPassword.showPassword()
            binding.showIcon.hide()
            binding.hideIcon.show()
        }

        binding.hideIcon.setOnClickListener {
            binding.txtPassword.hidePassword()
            binding.showIcon.show()
            binding.hideIcon.hide()
        }

        binding.btnSignUp.setOnClickListener {
//            findNavController().navigate(R.id.homeFragment)
            val bundle = Bundle()
            bundle.putInt(SOURCE, R.id.registerFragment)
            findNavController().navigate(R.id.uploadPhotoFragment, bundle)
        }

        val countries = resources.getStringArray(R.array.countries)
        val countriesList = countries.asList()

        val countryAdapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            countriesList
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                // Set color of the first item to gray to indicate it is disabled
                if (position == 0) {
                    view.setTextColor(Color.GRAY)
                }
                return view
            }
        }

        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.countrySpn.adapter = countryAdapter

        binding.countrySpn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        (view as TextView).setTextColor(Color.GRAY)
                    }

                    1 -> {
                        Log.d("Selected item", "Egypt")
                        getCities("egypt")
                    }

                    2 -> {
                        Log.d("Selected item", "Greece")
                        getCities("greece")
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        return binding.root
    }

    private fun getCities(country: String, isEnabled: Boolean = true) {
        viewModel.getCities(country)
        viewModel.citiesResponse.observe(viewLifecycleOwner) {
            cities.clear()
            cities.add(resources.getString(R.string.city_spn_hint))
            cities.addAll(it.data)
            binding.citySpn.setSpinnerAdapter(cities,requireContext())
            //arrayAdapter.notifyDataSetChanged()
            binding.citySpn.isEnabled = isEnabled
        }
    }

    private fun back(source: Int) {
        if (source != -1) {
            findNavController().navigate(source)
        } else {
            requireActivity().finish()
        }
    }
}