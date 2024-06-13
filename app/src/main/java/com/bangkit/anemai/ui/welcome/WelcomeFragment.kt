package com.bangkit.anemai.ui.welcome

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Bundle
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
    private lateinit var setOffsetImage: WelcomeImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOffsetImage = binding.ivWelcome

        setupAction(view)
    }

    private fun setupAction(view: View) {
        val extras = FragmentNavigatorExtras(
            binding.welcomeFragment to "transition_welcome"
        )
        binding.btnLogin.setOnClickListener {
            setOffsetImage.imageVerticalOffset = 250f
            setOffsetImage.invalidate()
            view.findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment, null, null, extras)
        }
        binding.btnRegister.setOnClickListener {
            setOffsetImage.imageVerticalOffset = 250f
            setOffsetImage.invalidate()
            view.findNavController().navigate(R.id.action_welcomeFragment_to_registerFragment, null, null, extras)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()

        // Show the action bar when the fragment is destroyed
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }

}