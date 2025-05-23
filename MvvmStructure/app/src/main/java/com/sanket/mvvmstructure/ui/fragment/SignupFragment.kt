package com.sanket.mvvmstructure.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleObserver
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.sanket.mvvmstructure.R
import com.sanket.mvvmstructure.databinding.LoginFragmentBinding
import com.sanket.mvvmstructure.databinding.SignupFragmentBinding
import com.sanket.mvvmstructure.ui.Activity.baseActivity
import com.sanket.mvvmstructure.ui.viewmodel.Appviewmodel
import dagger.hilt.android.AndroidEntryPoint


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint

class SignupFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: SignupFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: Appviewmodel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.signup_fragment, container, false)
        _binding = SignupFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvSwitch.setOnClickListener {
            it.isFocusable
            view.findNavController().navigate(R.id.action_signup_fragment_to_login_fragment)
        }

        binding.signupbutton.setOnClickListener {
            val name = binding.outlinedEditText.text.toString()
            val password = binding.passwordinput.text.toString()
            val email = binding.emailinput.text.toString()

            var EmailvalidationResult = (activity as baseActivity).emailvalidation(email)
            {
                binding.email.error = it
            }
            var PasswordvalidationResult = (activity as baseActivity).passwordvalidation(password)
            {
                binding.password.error = it

            }
            var NamevalidationResult = (activity as baseActivity).namevalidation(name)
            {
                binding.name.error = it
            }

            if (EmailvalidationResult && PasswordvalidationResult && NamevalidationResult) {
                viewModel.signup(name = name, password = password, email = email)
            }
            viewModel.errorMsgEmail.observe(viewLifecycleOwner) { msg ->
                if(msg.isNotEmpty())
                {
                    binding.email.error = msg
                }
            }

        }
            viewModel.successSignup.observe(viewLifecycleOwner) {

                if(it) {
                    binding.tvSwitch.findNavController().navigate(
                        R.id.action_signup_fragment_to_login_fragment, null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.login_fragment, true)
                            .build()
                    )
                }
            }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}