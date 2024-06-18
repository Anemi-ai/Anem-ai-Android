package com.bangkit.anemai.ui.welcome

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Bundle
import android.transition.ChangeBounds
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginTop
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bangkit.anemai.R
import com.bangkit.anemai.databinding.ActivityWelcomeBinding
import com.bangkit.anemai.databinding.FragmentMainBinding
import com.bangkit.anemai.databinding.FragmentWelcomeBinding
import com.bangkit.anemai.ui.login.LoginFragment
import com.bangkit.anemai.ui.register.RegisterFragment

class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(layoutInflater)
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

        setupAction(view)
    }

    private fun setupAction(view: View) {
        val extrasLogin = FragmentNavigatorExtras(
            binding.cardLogin to "card_login",
            binding.headlineWelcome to "headline_welcome",
            binding.bodycopyWelcome to "bodycopy_welcome",
            binding.ivWelcome to "iv_welcome"
        )
        val extrasRegister = FragmentNavigatorExtras(
            binding.cardRegister to "card_register",
            binding.headlineWelcome to "headline_welcome",
            binding.bodycopyWelcome to "bodycopy_welcome",
            binding.ivWelcome to "iv_welcome"
        )
        binding.btnLogin.setOnClickListener {
            view.findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment, null, null, extrasLogin)
        }
        binding.btnRegister.setOnClickListener {
            view.findNavController().navigate(R.id.action_welcomeFragment_to_registerFragment, null, null, extrasRegister)
        }
    }

}