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
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bangkit.anemai.R
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

        setOffsetImage = view.findViewById(R.id.iv_welcome)

        // Hide the action bar
        (activity as? AppCompatActivity)?.supportActionBar?.hide()

        if (savedInstanceState == null) {
            binding.btnLogin.setOnClickListener {

                val setMarginTop = binding.ivWelcome.layoutParams as LinearLayout.LayoutParams
                setMarginTop.topMargin = -350
                binding.ivWelcome.layoutParams = setMarginTop

                setOffsetImage.imageVerticalOffset = 250f
                setOffsetImage.invalidate()

                binding.layoutBtnLoginRegister.visibility = View.GONE
                binding.logoFooterWelcome.visibility = View.GONE

                parentFragmentManager.beginTransaction().apply {
                    setReorderingAllowed(true)
                    add<LoginFragment>(R.id.fragment_login_register)
                    commit()
                }
            }

             binding.btnRegister.setOnClickListener {
//                 findNavController().navigate(R.id.)
             }

        }

    }
    
    override fun onDestroyView() {
        super.onDestroyView()

        // Show the action bar when the fragment is destroyed
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }

}