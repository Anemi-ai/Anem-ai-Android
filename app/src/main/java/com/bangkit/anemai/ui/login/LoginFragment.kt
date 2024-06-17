package com.bangkit.anemai.ui.login

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
import com.bangkit.anemai.R
import com.bangkit.anemai.databinding.FragmentLoginBinding
import com.bangkit.anemai.ui.UserViewModelFactory
import com.bangkit.anemai.ui.ViewModelFactory
import com.bangkit.anemai.ui.main.MainActivity
import com.bangkit.anemai.ui.main.MainViewModel
import com.bangkit.anemai.ui.welcome.WelcomeImageView
import com.bangkit.anemai.utils.ProgressBarHandler

class LoginFragment : Fragment() {

    private var progressBarHandler: ProgressBarHandler? = null
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var setOffsetImage: WelcomeImageView
    private val viewModel by viewModels<LoginViewModel> {
        UserViewModelFactory.getInstance(this.requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
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

        initialSetOffsetImage()

        actionLoginButton()

    }

    private fun actionLoginButton() {
        binding.apply {
            btnActionLogin.setOnClickListener {
                val email = edEmail.text.toString()
                val password = edPassword.text.toString()

                viewModel.login(email, password)
                progressBarHandler?.showLoading(true)
            }
        }

        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            result.fold(
                onSuccess = { _ ->
                    progressBarHandler?.showLoading(false)
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                }, onFailure = { exception ->
                    progressBarHandler?.showLoading(false)
                    showAlertDialogFailure("Oops!", exception.toString())
                }
            )
        }
    }

    private fun initialSetOffsetImage() {
        setOffsetImage = binding.ivWelcome
        setOffsetImage.imageVerticalOffset = 450f
        setOffsetImage.invalidate()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}