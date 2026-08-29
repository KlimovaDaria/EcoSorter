package com.example.ecosorter.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.ecosorter.databinding.FragmentStartScreenBinding
import com.example.ecosorter.domain.entity.Level

class StartScreenFragment : Fragment() {
    private var _binding: FragmentStartScreenBinding? = null
    private val binding: FragmentStartScreenBinding
        get() = _binding ?: throw RuntimeException("FragmentStartScreenBinding==null")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            btnChooseEasyLevel.setOnClickListener {
                launchGameFragment(Level.EASY)
            }
            btnChooseNormalLevel.setOnClickListener {
                launchGameFragment(Level.NORMAL)
            }
            btnChooseHardLevel.setOnClickListener {
                launchGameFragment(Level.HARD)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchGameFragment(level: Level) {
        findNavController().navigate(
            StartScreenFragmentDirections.actionStartScreenFragmentToGameFragment(level)
        )
    }
}