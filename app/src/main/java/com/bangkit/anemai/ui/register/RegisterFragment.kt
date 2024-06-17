package com.bangkit.anemai.ui.register

import android.os.Bundle
import android.transition.ChangeBounds
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.bangkit.anemai.databinding.FragmentRegisterBinding
import com.bangkit.anemai.ui.ViewModelFactory

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}