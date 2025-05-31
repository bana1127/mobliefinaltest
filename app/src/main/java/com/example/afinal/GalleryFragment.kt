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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.afinal.databinding.FragmentGalleryBinding
import java.io.InputStream

class GalleryFragment : Fragment() {
    private var _binding : FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private var bitmap: Bitmap? = null
    lateinit var request : ActivityResultLauncher<Intent>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        request = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            val intent = result.data
            val uri: Uri? = intent?.data
            val contentResolver: ContentResolver? = activity?.contentResolver
            val inputStream: InputStream? = uri?.let{contentResolver?.openInputStream(it)}

            bitmap = inputStream?.use {
                BitmapFactory.decodeStream(it)
            }

            bitmap?.let {
                binding.image.setImageBitmap(it)
            }
        }
        binding.importbt.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            request.launch(intent)
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