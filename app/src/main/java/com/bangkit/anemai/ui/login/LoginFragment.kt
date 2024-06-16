package com.bangkit.anemai.ui.login

import android.os.Bundle
import android.transition.ChangeBounds
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.bangkit.anemai.R
import com.bangkit.anemai.databinding.FragmentLoginBinding
import com.bangkit.anemai.ui.UserViewModelFactory
import com.bangkit.anemai.ui.ViewModelFactory
import com.bangkit.anemai.ui.main.MainViewModel
import com.bangkit.anemai.ui.welcome.WelcomeImageView

class LoginFragment : Fragment() {

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

    }

    private fun initialSetOffsetImage() {
        setOffsetImage = binding.ivWelcome
        setOffsetImage.imageVerticalOffset = 450f
        setOffsetImage.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}