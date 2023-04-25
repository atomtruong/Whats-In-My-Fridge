package com.example.myfridge.ui.expiring

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfridge.R
import com.example.myfridge.data.fridge.FridgeContent
import com.example.myfridge.databinding.FragmentExpiringBinding
import com.example.myfridge.ui.database.DatabaseViewModel
import java.time.LocalDateTime
import java.time.ZoneOffset

class ExpiringFragment: Fragment() {
    private var _binding: FragmentExpiringBinding? = null
    private lateinit var expiringAdapter: ExpiringAdapter
    private lateinit var expiringRv: RecyclerView
    private val viewModel: DatabaseViewModel.FridgeItemInfoViewModel by viewModels()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentExpiringBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val prefs = PreferenceManager.getDefaultSharedPreferences((activity as AppCompatActivity))
        val prefDays = prefs.getString((activity as AppCompatActivity).getString(R.string.pref_expiration_key), "7")
        val expDate = LocalDateTime.now().plusDays(Integer.parseInt(prefDays!!).toLong()).toInstant(
            ZoneOffset.UTC).toEpochMilli()
        expiringRv = binding.rvExpiring
        expiringRv.layoutManager = LinearLayoutManager(container?.context)
        expiringAdapter = ExpiringAdapter()
        expiringRv.adapter = expiringAdapter
        viewModel.getExpiringSoon(expDate).observe(viewLifecycleOwner){
            expiringAdapter.updateExpiringList(FridgeContent(it.orEmpty()))
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}