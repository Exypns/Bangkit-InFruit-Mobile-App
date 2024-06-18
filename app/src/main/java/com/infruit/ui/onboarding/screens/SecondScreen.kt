package com.infruit.ui.onboarding.screens

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.infruit.R
import com.infruit.databinding.FragmentSecondScreenBinding
import com.infruit.viewmodel.OnBoardViewModel

class SecondScreen : Fragment() {
    private lateinit var binding: FragmentSecondScreenBinding
    private lateinit var onBoardViewModel: OnBoardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSecondScreenBinding.inflate(inflater, container, false)
        val root: View = binding.root

        nextClick()
        finishClick()

        return root
    }

    private fun nextClick() {
        val viewPager = activity?.findViewById<ViewPager2>(R.id.vw_onboarding)

        binding.tvPrev.setOnClickListener {
            viewPager?.currentItem = 0
        }
    }

    private fun finishClick() {
        onBoardViewModel.getTokenData().observe(viewLifecycleOwner) { token ->
            if (token != null) {
                binding.btnFinish.setOnClickListener {
                    findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                    onBoardingFinished()
                }
            } else {
                binding.btnFinish.setOnClickListener {
                    findNavController().navigate(R.id.action_splashFragment_to_loginActivity)
                    onBoardingFinished()
                }
            }
        }
    }

    private fun onBoardingFinished() {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }

}