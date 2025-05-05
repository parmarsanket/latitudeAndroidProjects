package com.sanket.mvvmstructure.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.sanket.mvvmstructure.R
import com.sanket.mvvmstructure.databinding.LoginFragmentBinding
import com.sanket.mvvmstructure.ui.Activity.baseActivity
import com.sanket.mvvmstructure.ui.viewmodel.Appviewmodel
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: Appviewmodel by activityViewModels()

    private var param1: String? = null
    private var param2: String? = null

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

        _binding = LoginFragmentBinding.inflate(inflater, container, false)

        //val view = inflater.inflate(R.layout.login_fragment, container, false)



        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.tvSwitch.setOnClickListener {
            view.findNavController().navigate(R.id.action_login_fragment_to_signup_fragment)
        }
        viewModel.autoLoginIfAvailable()
        binding.loginButton.setOnClickListener {
            val email = binding.outlinedEditText.text.toString()
            val password = binding.passwordinput.text.toString()

            var EmailvalidationResult = (activity as baseActivity).emailvalidation(email)
            {
                binding.email.error = it
            }
            var PasswordvalidationComplete = (activity as baseActivity).passwordvalidation(password)
            {
                binding.password.error = it
            }

            if (EmailvalidationResult && PasswordvalidationComplete) {
                viewModel.login(email = email, password =  password)

            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner){
            if(!it.isNullOrEmpty())
            {
                Toast.makeText(requireContext(),"${viewModel.errorMessage.value}",Toast.LENGTH_SHORT).show()

            }
        }
        viewModel.errorMsgEmail.observe(viewLifecycleOwner) { msg ->
            binding.email.error = msg
        }
        viewModel.errorMsgPassworld.observe(viewLifecycleOwner){msg->
            binding.password.error = msg

        }
        viewModel.loginSuccess.observe(viewLifecycleOwner){


            if (it) {
                view.findNavController().navigate(
                    R.id.action_login_fragment_to_main_fragment,
                    null, NavOptions.Builder().setPopUpTo(R.id.login_fragment, true).build()
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
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}