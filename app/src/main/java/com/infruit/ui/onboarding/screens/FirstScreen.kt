package com.infruit.ui.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.infruit.R
import com.infruit.databinding.FragmentFirstScreenBinding

class FirstScreen : Fragment() {
    private lateinit var binding: FragmentFirstScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstScreenBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupClickListeners()
        return root
    }

    private fun setupClickListeners() {
        val viewPager = activity?.findViewById<ViewPager2>(R.id.vw_onboarding)

        binding.btnNext.setOnClickListener {
            viewPager?.currentItem = 1
        }
    }
}