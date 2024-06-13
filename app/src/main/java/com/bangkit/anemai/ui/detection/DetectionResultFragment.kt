package com.bangkit.anemai.ui.detection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.anemai.R
import com.bangkit.anemai.databinding.FragmentDetectionResultBinding


class DetectionResultFragment : Fragment() {
    private lateinit var binding: FragmentDetectionResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetectionResultBinding.inflate(layoutInflater)
        return binding.root
    }
}