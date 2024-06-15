package com.bangkit.anemai.ui.detection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.bangkit.anemai.R
import com.bangkit.anemai.databinding.FragmentDetectionResultBinding


class DetectionResultFragment : Fragment() {
    private lateinit var binding: FragmentDetectionResultBinding
    private lateinit var menuProvider: MenuProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetectionResultBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupActionbar()
    }

    private fun setupActionbar() {
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = getString(R.string.detection_result)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            setHomeButtonEnabled(true)
        }

        menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    android.R.id.home -> {
                        view?.findNavController()?.navigate(R.id.action_detectionResultFragment_to_mainFragment)
                        return true
                    }
                }

                return false
            }

        }

        requireActivity().addMenuProvider(menuProvider)
    }
}