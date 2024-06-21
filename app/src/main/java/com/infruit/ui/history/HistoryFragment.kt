package com.infruit.ui.history

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.infruit.data.TypesResponse
import com.infruit.data.datastore.UserDataPreferences
import com.infruit.data.model.history.DataItem
import com.infruit.databinding.FragmentHistoryBinding
import com.infruit.factory.HistoryViewModelFactory
import com.infruit.viewmodel.history.HistoryViewModel


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var tokenData: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        val pref = UserDataPreferences.getInstance(requireContext().dataStore)

        historyViewModel = ViewModelProvider(this, HistoryViewModelFactory(pref))[HistoryViewModel::class.java]

        historyViewModel.listHistory.observe(viewLifecycleOwner) { history ->
            Log.d("History", history.data.toString())
        }

        observeToken()

        val layoutManager = LinearLayoutManager(requireContext().applicationContext)
        binding.rvHistory.layoutManager = layoutManager

        return binding.root
    }

    private fun observeToken() {
        historyViewModel.getToken().observe(viewLifecycleOwner) { token ->
            token.let {
                if (token != null) {
                    tokenData = token
                    Log.d("Token in history", token)
                    getAllHistory()
                }
            }

        }
    }

    private fun getAllHistory() {
        historyViewModel.getAllHistory(tokenData)
        historyViewModel.listHistory.observe(viewLifecycleOwner) { result ->
            when (result) {
                is TypesResponse.Loading -> {
                    showLoading(true)
                }
                is TypesResponse.Success -> {
                    showLoading(false)
                    Log.d("Get History Success", result.data?.data.toString())
                    setHistoryData(result.data?.data?.data)
                }
                is TypesResponse.Error -> {
                    showLoading(false)
                    Log.d("Get History Error", result.data?.message.toString())
                }
            }
        }
    }

    private fun setHistoryData(historyData: List<DataItem?>?) {
        val adapter = ItemHistoryAdapter()
        adapter.submitList(historyData)
        binding.rvHistory.adapter = adapter
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }


}