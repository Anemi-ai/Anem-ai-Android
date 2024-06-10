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
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.anemai.R
import com.bangkit.anemai.databinding.FragmentMainBinding
import com.bangkit.anemai.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hide the action bar
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Show the action bar when the fragment is destroyed
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }

}