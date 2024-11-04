package com.correct.goldx.ui.auth

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.correct.goldx.R
import com.correct.goldx.data.auth.LoginBody
import com.correct.goldx.data.auth.LoginResponse
import com.correct.goldx.databinding.FragmentLoginBinding
import com.correct.goldx.helper.CastException
import com.correct.goldx.helper.Constants.LOGGED_IN
import com.correct.goldx.helper.Constants.REMEMBER
import com.correct.goldx.helper.Constants.SOURCE
import com.correct.goldx.helper.Constants.TOKEN_KEY
import com.correct.goldx.helper.Constants.TOKEN_VALUE
import com.correct.goldx.helper.FragmentChangeListener
import com.correct.goldx.helper.hide
import com.correct.goldx.helper.hidePassword
import com.correct.goldx.helper.onBackPressed
import com.correct.goldx.helper.show
import com.correct.goldx.helper.showPassword
import com.correct.goldx.helper.toast
import com.correct.goldx.room.User
import com.correct.goldx.room.UsersDB
import com.mkandeel.datastore.DataStorage
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var changeListener: FragmentChangeListener
    private var sourceFragment = -1
    private lateinit var viewModel: AuthViewModel
    private lateinit var dataStore: DataStorage
    private lateinit var usersDB: UsersDB

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
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        dataStore = DataStorage.getInstance(requireContext())
        usersDB = UsersDB.getDBInstance(requireContext())

        sourceFragment = arguments?.getInt(SOURCE, -1) ?: -1

        onBackPressed {
            back(sourceFragment)
        }

        binding.backBtn.setOnClickListener {
            back(sourceFragment)
        }

        binding.btnSignUp.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt(SOURCE, R.id.loginFragment)
            findNavController().navigate(R.id.registerFragment, bundle)
        }

        binding.showIcon.setOnClickListener {
            binding.txtPassword.showPassword()
            binding.showIcon.hide()
            binding.hideIcon.show()
        }

        binding.btnLogin.setOnClickListener {
            val mail = binding.txtMail.text.toString()
            val password = binding.txtPassword.text.toString()
            if (mail.isNotEmpty() && password.isNotEmpty()) {
                login(LoginBody(email = mail, password = password))
            } else {
                toast(resources.getString(R.string.fill_required))
            }
        }

        binding.hideIcon.setOnClickListener {
            binding.txtPassword.hidePassword()
            binding.showIcon.show()
            binding.hideIcon.hide()
        }

        binding.txtForget.setOnClickListener {
            findNavController().navigate(R.id.forgetPasswordFragment)
        }

        return binding.root
    }

    private fun login(body: LoginBody) {
        viewModel.login(body)
        val observer = object : Observer<LoginResponse> {
            override fun onChanged(value: LoginResponse) {
                if (value.success) {
                    val result = value.result
                    if (result != null) {
                        lifecycleScope.launch {
                            dataStore.putString(
                                requireContext(),
                                TOKEN_KEY,
                                "$TOKEN_VALUE ${result.token}"
                            )
                            // get saved user first
                            val oldUser = usersDB.dao().getOneUser("1")
                            // delete save user from database
                            usersDB.dao().deleteUser("1")
                            // create new user with same data and real user id
                            if (oldUser != null) {
                                val user = User(
                                    result.id,
                                    oldUser.name, oldUser.mail, oldUser.phone, oldUser.password,
                                    oldUser.country, oldUser.city, oldUser.street, oldUser.buildNo
                                )
                                usersDB.dao().insert(user)
                            }

                            dataStore.putBoolean(requireContext(), LOGGED_IN, true)
                            dataStore.putBoolean(requireContext(), REMEMBER, binding.rememberSwitch.isChecked)

                            findNavController().navigate(R.id.homeFragment)
                        }
                    }
                } else {
                    toast(value.errorMessage!!)
                }
                viewModel.loginResponse.removeObserver(this)
            }
        }
        viewModel.loginResponse.observe(viewLifecycleOwner, observer)
    }

    private fun back(source: Int) {
        if (source != -1) {
            findNavController().navigate(source)
        } else {
            requireActivity().finish()
        }
    }
}