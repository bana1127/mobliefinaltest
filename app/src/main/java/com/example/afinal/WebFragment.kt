package com.example.afinal

import android.content.ContentResolver
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.PixelCopy.Request
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.Volley
import com.example.afinal.databinding.FragmentCameraBinding
import com.example.afinal.databinding.FragmentGalleryBinding
import com.example.afinal.databinding.FragmentWebBinding
import java.io.InputStream

class WebFragment : Fragment() {
    private var _binding : FragmentWebBinding? = null
    private val binding get() = _binding!!
    private var bitmap: Bitmap? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWebBinding.inflate(inflater, container, false)

        val queue: RequestQueue = Volley.newRequestQueue(activity)

        binding.importbt.setOnClickListener {
            val url = binding.edittx.text.toString()
            val imageRequest = ImageRequest(
                url,
                { response ->
                    bitmap = response
                    binding.image.setImageBitmap(bitmap)
                },
                0,
                0,
                ImageView.ScaleType.CENTER_CROP,
                null,
                { error ->
                    Toast.makeText(activity, "ERROR: ${error.message}", Toast.LENGTH_SHORT).show()
                    error.printStackTrace()
                    Log.d("kkang","${error.message}")
                }

            )
            queue.add(imageRequest)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        bitmap?.let {
            binding.image.setImageBitmap(it)
        }

        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val scaleString: String? = sharedPreferences.getString("scale_type", "Center_Crop")
        binding.image.scaleType = when(scaleString){
            "Center"->ImageView.ScaleType.CENTER
            "Fit_End"->ImageView.ScaleType.FIT_END
            else-> ImageView.ScaleType.CENTER_CROP
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}