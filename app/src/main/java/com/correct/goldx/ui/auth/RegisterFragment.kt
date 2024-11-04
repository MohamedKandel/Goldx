package com.correct.goldx.ui.auth

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.correct.goldx.R
import com.correct.goldx.data.auth.Address
import com.correct.goldx.data.auth.RegisterBody
import com.correct.goldx.databinding.FragmentRegisterBinding
import com.correct.goldx.helper.CastException
import com.correct.goldx.helper.Constants.FINAL_STEP
import com.correct.goldx.helper.Constants.OBJECT
import com.correct.goldx.helper.Constants.SOURCE
import com.correct.goldx.helper.FragmentChangeListener
import com.correct.goldx.helper.hide
import com.correct.goldx.helper.hidePassword
import com.correct.goldx.helper.isContainSpecialCharacter
import com.correct.goldx.helper.onBackPressed
import com.correct.goldx.helper.setSpinnerAdapter
import com.correct.goldx.helper.show
import com.correct.goldx.helper.showPassword
import com.correct.goldx.helper.toast
import com.mkandeel.datastore.DataStorage
import kotlinx.coroutines.launch
import me.srodrigo.androidhintspinner.HintAdapter
import me.srodrigo.androidhintspinner.HintSpinner

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var changeListener: FragmentChangeListener
    private var sourceFragment = -1
    private lateinit var viewModel: CitiesViewModel
    private lateinit var cities: MutableList<String>
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private var country = ""
    private var city = ""
    private var isPasswordValid = false
    private lateinit var dataStore: DataStorage

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
        dataStore = DataStorage.getInstance(requireContext())
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

                // check if have number or not
                if (password.any { it.isDigit() }) {
                    binding.passwordNumberIcon.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.success
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.txtNumberCharacter.setTextColor(
                        resources.getColor(
                            R.color.success,
                            requireContext().theme
                        )
                    )
                } else {
                    binding.passwordNumberIcon.setColorFilter(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray
                        ), android.graphics.PorterDuff.Mode.SRC_IN
                    )
                    binding.txtNumberCharacter.setTextColor(
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

                isPasswordValid = (password.length >= 6
                        && password.any { it.isDigit() }
                        && password.isContainSpecialCharacter()
                        && (password.any { it.isUpperCase() } && password.any { it.isLowerCase() }))
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

        binding.txtPhone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()) {
                    binding.txtPhone.keyListener = DigitsKeyListener.getInstance("0123456789+")
                } else {
                    if (s.toString().startsWith("+")) {
                        binding.txtPhone.keyListener = DigitsKeyListener.getInstance("0123456789")
                    } else {
                        binding.txtPhone.keyListener = DigitsKeyListener.getInstance("0123456789")
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (!s.toString().startsWith("+")) {
                    toast(resources.getString(R.string.phone_contain_plus))
                }
            }
        })

        binding.btnSignUp.setOnClickListener {
            val name = binding.txtName.text.toString()
            val email = binding.txtMail.text.toString()
            val phone = binding.txtPhone.text.toString()
            val street = binding.txtStreet.text.toString()
            val building = binding.txtBuilding.text.toString()
            val password = binding.txtPassword.text.toString()
            val address = Address(
                city = city,
                country = country, street = street,
                noBuilding = building.toInt()
            )

            if (name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && street.isNotEmpty()
                && building.isNotEmpty() && city.isNotEmpty() && country.isNotEmpty() && password.isNotEmpty()
            ) {
                if (phone.length < 13) {
                    toast(resources.getString(R.string.phone_length))
                } else if (!isPasswordValid) {
                    toast(resources.getString(R.string.password_not_valid))
                } else {
                    val model = RegisterBody(
                        name = name,
                        email = email, password = password, phoneNumber = phone,
                        address = address
                    )
                    lifecycleScope.launch {
                        dataStore.putInt(requireContext(), FINAL_STEP,0)
                    }
                    val bundle = Bundle()
                    bundle.putParcelable(OBJECT, model)
                    bundle.putInt(SOURCE, R.id.registerFragment)
                    findNavController().navigate(R.id.uploadPhotoFragment, bundle)
                }
            } else {
                toast(resources.getString(R.string.fill_required))
            }


            //findNavController().navigate(R.id.uploadPhotoFragment, bundle)
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
                        country = "egypt"
                        city = ""
                    }

                    2 -> {
                        Log.d("Selected item", "Greece")
                        getCities("greece")
                        country = "greece"
                        city = ""
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
            binding.citySpn.setSpinnerAdapter(cities, requireContext())
            //arrayAdapter.notifyDataSetChanged()
            binding.citySpn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val value = parent!!.getItemAtPosition(position).toString()
                    if (value == cities[0]) {
                        (view as TextView).setTextColor(Color.GRAY)
                    } else {
                        Log.v("Selected city", value)
                        city = value
                    }
                }

            }
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