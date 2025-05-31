package com.example.afinal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.afinal.databinding.FragmentBatteryBinding

class BatteryFragment : Fragment() {
    private var _binding : FragmentBatteryBinding? = null
    private val binding get() = _binding!!
    private var batteryReceiver: BroadcastReceiver? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBatteryBinding.inflate(inflater, container, false)

        batteryReceiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.let {
                    val level = it.getIntExtra("level", -1)
                    val scale = it.getIntExtra("scale", -1)
                    if(level != -1 && scale != -1){
                        val batteryPct = (level.toFloat() / scale.toFloat()) * 100
                        binding.batterytx.text = "배터리량 : ${batteryPct.toInt()}%"
                    }
                }
            }
        }
        activity?.registerReceiver(batteryReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))


        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val colorString = sharedPreferences.getString("s_color", "#000000") ?: "#000000"
        binding.batterytx.setTextColor(Color.parseColor(colorString))

    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.unregisterReceiver(batteryReceiver)
        _binding = null
    }


}