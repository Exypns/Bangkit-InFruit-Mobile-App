package com.infruit.ui.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.fragment.findNavController
import com.infruit.R
import com.infruit.databinding.FragmentOnboardingBinding
import com.infruit.databinding.FragmentSplashBinding
import com.infruit.viewmodel.OnBoardViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var onBoardViewModel: OnBoardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Handler().postDelayed({
            val isOnBoardingFinished = onBoardingFinished()

            onBoardViewModel.getTokenData().observe(viewLifecycleOwner) { token ->
                val isOnBoardingFinished = onBoardingFinished() // Pastikan ini sesuai dengan logika Anda untuk memeriksa onboarding

                when {
                    isOnBoardingFinished && token != null -> {
                        findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                    }
                    !isOnBoardingFinished && token == null -> {
                        findNavController().navigate(R.id.action_splashFragment_to_loginActivity)
                    }
                    !isOnBoardingFinished -> {
                        findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
                    }
                    else -> {
                        // Handle any other cases if necessary, but logically it should not reach here
                    }
                }
            }

        }, 3000)
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    private fun onBoardingFinished(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }
}