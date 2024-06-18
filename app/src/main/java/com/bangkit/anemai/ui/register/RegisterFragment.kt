package com.bangkit.anemai.ui.register

import android.content.Intent
import android.os.Bundle
import android.transition.ChangeBounds
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.bangkit.anemai.R
import com.bangkit.anemai.databinding.FragmentRegisterBinding
import com.bangkit.anemai.ui.ViewModelFactory
import com.bangkit.anemai.ui.main.MainActivity
import com.bangkit.anemai.ui.welcome.WelcomeActivity
import com.bangkit.anemai.utils.ProgressBarHandler

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(requireContext(), requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 750
        }
        sharedElementReturnTransition= ChangeBounds().apply {
            duration = 750
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        actionRegisterButton()
    }

    private fun actionRegisterButton() {
        binding.apply {
            btnActionRegister.setOnClickListener {
                val name = edRegisterFullName.text.toString()
                val birthDate = edRegisterBirthDate.text.toString()
                val gender = edRegisterGender.text.toString()
                val email = edRegisterEmail.text.toString()
                val password = edRegisterPassword.text.toString()

                viewModel.registerUser(name, birthDate, gender, email, password)
                showLoading(true)
            }
        }

        viewModel.registrationResult.observe(viewLifecycleOwner) { result ->
            result.fold(
                onSuccess = { message ->
                    showLoading(false)
                    showAlertDialogSuccess("Yes!", message)
                }, onFailure = { exception ->
                    showLoading(false)
                    showAlertDialogFailure("Oh no!", exception.message ?: "Unknown error")
                }
            )
        }
    }

    private fun showAlertDialogSuccess(title: String, message: String) {
        AlertDialog.Builder(this.requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Login now") { dialog, _ ->
                dialog.dismiss()
                val extrasLogin = FragmentNavigatorExtras(
                    binding.cardRegister to "card_login",
                    binding.headlineWelcome to "headline_welcome",
                    binding.bodycopyWelcome to "bodycopy_welcome",
                    binding.ivWelcome to "iv_welcome"
                )
                view?.findNavController()?.navigate(R.id.action_registerFragment_to_loginFragment, null, null, extrasLogin)
            }
            .create()
            .show()
    }

    private fun showAlertDialogFailure(title: String, message: String) {
        AlertDialog.Builder(this.requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Retry") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            (activity as? WelcomeActivity)?.showLoading(true)
        } else {
            (activity as? WelcomeActivity)?.showLoading(false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}