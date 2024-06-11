package com.bangkit.anemai.ui.history

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
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.ChangeBounds
import com.bangkit.anemai.R
import com.bangkit.anemai.data.DataDummy
import com.bangkit.anemai.data.adapter.HistoryAdapter
import com.bangkit.anemai.data.model.AnemiaDetection
import com.bangkit.anemai.databinding.FragmentHistoryBinding


class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var menuProvider: MenuProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        sharedElementEnterTransition = ChangeBounds().apply { duration = 750 }
        sharedElementReturnTransition = ChangeBounds().apply { duration = 750 }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        val anemiaList = DataDummy.generateAnemiDetectionList()

        setupActionbar()
//        setupAction(view)
        setupHistoryData(anemiaList, view)
    }

    private fun setupHistoryData(anemiaDetection: List<AnemiaDetection>, view: View) {
        val extras = FragmentNavigatorExtras(
            binding.bgLayout to "bg_layout"
        )
        val toDetailfragment = HistoryFragmentDirections.actionHistoryFragmentToHistoryDetailFragment()
        val adapter = HistoryAdapter { detection ->
            toDetailfragment.apply {
                detectionDate = detection.createdAt.toString()
                detectionResult = detection.result.toString()
                detectionImage = detection.imageUrl.toString()
            }
            view.findNavController().navigate(toDetailfragment, extras)
        }
        binding.rvHistory.adapter = adapter
        adapter.submitList(anemiaDetection)
    }

    private fun setupActionbar() {
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = getString(R.string.history_detection)
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
                        requireActivity().supportFragmentManager.popBackStack()
                        return true
                    }
                }

                return false
            }

        }

        requireActivity().addMenuProvider(menuProvider)
    }

//    private fun setupAction(view: View) {
//        val extras = FragmentNavigatorExtras(
//            binding.bgLayout to "bg_layout"
//        )
//        binding.btnTest.setOnClickListener{
//            view.findNavController().navigate(R.id.action_historyFragment_to_historyDetailFragment, null, null, extras)
//        }
//    }

    override fun onStop() {
        super.onStop()
        requireActivity().removeMenuProvider(menuProvider)
    }
}
