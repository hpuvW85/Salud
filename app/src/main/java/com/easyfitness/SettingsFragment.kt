package com.easyfitness

import android.content.Context
import android.content.SharedPreferences
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.fitworkoutfast.MainActivity
import xyz.aprildown.ultimateringtonepicker.RingtonePickerDialog
import xyz.aprildown.ultimateringtonepicker.UltimateRingtonePicker

class SettingsFragment : PreferenceFragmentCompat() {
    private var mActivity: MainActivity? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as MainActivity?

        val myPref = findPreference<Preference>("prefShowMP3")
        myPref!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _: Preference?, newValue: Any? ->
            if (newValue is Boolean) {
                mActivity!!.showMP3Toolbar(newValue)
            }
            true
        }
        val myPref2 = findPreference<Preference>("defaultUnit")
        myPref2!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference: Preference?, newValue: Any? ->
            val listPreference = preference as ListPreference?
            if (newValue is String) {
                //find the index of changed value in settings.
                updateSummary(listPreference, newValue as String?, getString(R.string.pref_preferredUnitSummary))
            }
            true
        }
        val myPref3 = findPreference<Preference>("defaultDistanceUnit")
        myPref3!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference: Preference?, newValue: Any? ->
            val listPreference = preference as ListPreference?
            if (newValue is String) {
                //find the index of changed value in settings.
                updateSummary(listPreference, newValue as String?, getString(R.string.pref_preferredUnitSummary))
            }
            true
        }
        val dayNightModePref = findPreference<Preference>("dayNightAuto")
        dayNightModePref!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference: Preference?, newValue: Any? ->
            val listPreference = preference as ListPreference?
            if (newValue is String) {
                updateSummary(listPreference, newValue as String?, "")
            }
            true
        }
        val playRestSound = findPreference<Preference>("playRestSound")
        playRestSound!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _: Preference?, newValue: Any? ->
            if (newValue is Boolean) {
                saveToPreference("playRestSound", newValue as Boolean?)
            }
            true
        }
        val playStaticExerciseFinishSound = findPreference<Preference>("playStaticExerciseFinishSound")
        playStaticExerciseFinishSound!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _: Preference?, newValue: Any? ->
            if (newValue is Boolean) {
                saveToPreference("playStaticExerciseFinishSound", newValue as Boolean?)
            }
            true
        }
        val nextExerciseSwitch = findPreference<Preference>("nextExerciseSwitch")
        nextExerciseSwitch!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _: Preference?, newValue: Any? ->
            if (newValue is Boolean) {
                saveToPreference("nextExerciseSwitch", newValue as Boolean?)
            }
            true
        }
        val swipeGesturesSwitch = findPreference<Preference>("swipeGesturesSwitch")
        swipeGesturesSwitch!!.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _: Preference?, newValue: Any? ->
            if (newValue is Boolean) {
                saveToPreference("swipeGesturesSwitch", newValue as Boolean?)
            }
            true
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, param: String?) {
        setPreferencesFromResource(R.xml.settings2, param)
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val myPref2 = findPreference<Preference>("defaultUnit") as ListPreference?
        val boolVal = sharedPreferences.getString("defaultUnit", "0")
        updateSummary(myPref2, boolVal, getString(R.string.pref_preferredUnitSummary))
        val myPref3 = findPreference<Preference>("defaultDistanceUnit") as ListPreference?
        val boolVal3 = sharedPreferences.getString("defaultDistanceUnit", "0")
        updateSummary(myPref3, boolVal3, getString(R.string.pref_preferredUnitSummary))
        val dayNightModePref = findPreference<Preference>("dayNightAuto") as ListPreference?
        val dayNightValue = sharedPreferences.getString("dayNightAuto", "1")
        updateSummary(dayNightModePref, dayNightValue, "")

        val dialogRestSound = preferenceScreen.findPreference("dialog_rest_sound") as Preference?
        if (dialogRestSound != null) {
            dialogRestSound.onPreferenceClickListener = Preference.OnPreferenceClickListener { // dialog code here
                val settings=createStandardSettings()
                RingtonePickerDialog.createEphemeralInstance(
                        settings = settings,
                        dialogTitle = "Choose rest finished sound",
                        listener = object : UltimateRingtonePicker.RingtonePickerListener {
                            override fun onRingtonePicked(ringtones: List<UltimateRingtonePicker.RingtoneEntry>) {
                                handleResult(ringtones,"restSound")
                            }
                        }
                ).show(this.childFragmentManager, null)
                true
            }
        }
        val dialogStaticSound = preferenceScreen.findPreference("dialog_static_sound") as Preference?
        if (dialogStaticSound != null) {
            dialogStaticSound.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val settings=createStandardSettings()
                RingtonePickerDialog.createEphemeralInstance(
                    settings = settings,
                    dialogTitle = "Choose static exercise finish sound",
                    listener = object : UltimateRingtonePicker.RingtonePickerListener {
                        override fun onRingtonePicked(ringtones: List<UltimateRingtonePicker.RingtoneEntry>) {
                            handleResult(ringtones,"staticSound")
                        }
                    }
                ).show(this.childFragmentManager, null)
                true
            }
        }
    }


    private var currentSelectedRingTones = listOf<UltimateRingtonePicker.RingtoneEntry>()

    private fun createStandardSettings(): UltimateRingtonePicker.Settings =
        UltimateRingtonePicker.Settings(
            preSelectUris = currentSelectedRingTones.map { it.uri },
            systemRingtonePicker = UltimateRingtonePicker.SystemRingtonePicker(
                customSection = UltimateRingtonePicker.SystemRingtonePicker.CustomSection(),
                defaultSection = UltimateRingtonePicker.SystemRingtonePicker.DefaultSection(
                    showSilent = true,
                    defaultUri = UltimateRingtonePicker.createRawRingtoneUri(
                        requireContext(),
                        R.raw.chime
                    ),
                    defaultTitle = "Default short sound"
                ),
                ringtoneTypes = listOf(
//                    RingtoneManager.TYPE_RINGTONE,
                    RingtoneManager.TYPE_NOTIFICATION,
                    RingtoneManager.TYPE_ALARM
                )
            ),
            deviceRingtonePicker = UltimateRingtonePicker.DeviceRingtonePicker(
                deviceRingtoneTypes = listOf(
                    UltimateRingtonePicker.RingtoneCategoryType.All,
                    UltimateRingtonePicker.RingtoneCategoryType.Artist,
                    UltimateRingtonePicker.RingtoneCategoryType.Album,
                    UltimateRingtonePicker.RingtoneCategoryType.Folder
                )
            )
        )

    private fun handleResult(ringtones: List<UltimateRingtonePicker.RingtoneEntry>, prefName: String) {
        currentSelectedRingTones = ringtones
        val myUri: Uri = ringtones[0].uri
        saveToPreference(prefName, myUri.toString())
    }

    private fun updateSummary(pref: ListPreference?, `val`: String?, prefix: String) {
        val prefIndex = pref!!.findIndexOfValue(`val`)
        if (prefIndex >= 0) {
            //finally set's it value changed
            pref.summary = prefix + pref.entries[prefIndex]
        }
    }

    private fun saveToPreference(prefName: String?, prefBoolToSet: Boolean?) {
        val sharedPref = requireContext().getSharedPreferences(prefName, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(prefName, prefBoolToSet!!)
        editor.apply()
    }

    private fun saveToPreference(prefName: String?, prefStringToSet: String) {
        val sharedPref = requireContext().getSharedPreferences(prefName, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(prefName, prefStringToSet)
        editor.apply()
    }

    companion object {
        const val WEIGHT_UNIT_PARAM = "defaultUnit"
        const val DISTANCE_UNIT_PARAM = "defaultDistanceUnit"

        /**
         * Create a new instance of DetailsFragment, initialized to
         * show the text at 'index'.
         */
        @JvmStatic
        fun newInstance(name: String?, id: Int): SettingsFragment {
            name+id // just to warning skip
            return SettingsFragment()
        }
    }
}
