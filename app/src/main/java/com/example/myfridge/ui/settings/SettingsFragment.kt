package com.example.myfridge.ui.settings

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import com.example.myfridge.R
import com.quickersilver.themeengine.ThemeChooserDialogBuilder
import com.quickersilver.themeengine.ThemeEngine
import com.takisoft.preferencex.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var themeEngine: ThemeEngine
    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        themeEngine = ThemeEngine.getInstance(requireContext())
        val activity = (requireActivity() as AppCompatActivity)
        var toolbar: Toolbar = activity.findViewById(R.id.toolbar)
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val button = findPreference<Preference>("Color")
        button?.onPreferenceClickListener= Preference.OnPreferenceClickListener {
            val destination = SettingsFragmentDirections.actionNavSettingsToThemeSettings()
            findNavController().navigate(destination)
            true
        }
        return super.onCreateView(inflater, container, savedInstanceState)


    }

}