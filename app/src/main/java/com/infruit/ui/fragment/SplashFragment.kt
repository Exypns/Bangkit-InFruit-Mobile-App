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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.infruit.R
import com.infruit.data.datastore.UserDataPreferences
import com.infruit.databinding.FragmentSplashBinding
import com.infruit.factory.OnBoardViewModelFactory
import com.infruit.viewmodel.OnBoardViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var onBoardViewModel: OnBoardViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = UserDataPreferences.getInstance(requireContext().dataStore)
        onBoardViewModel =
            ViewModelProvider(this, OnBoardViewModelFactory(pref))[OnBoardViewModel::class.java]
        Handler().postDelayed({
            val isOnBoardingFinished = onBoardingFinished()


            onBoardViewModel.getTokenData().observe(viewLifecycleOwner) { token ->
                when {
                    isOnBoardingFinished && token != null -> {
                        findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                    }
                    isOnBoardingFinished && token == null -> {
                        findNavController().navigate(R.id.action_splashFragment_to_loginActivity)
                    }
                    else -> {
                        findNavController().navigate(R.id.action_splashFragment_to_onboardingFragment)
                    }
                }
            }

        }, 3000)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun onBoardingFinished(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }
}
