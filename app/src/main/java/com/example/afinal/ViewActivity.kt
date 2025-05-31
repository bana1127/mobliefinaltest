package com.example.afinal

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.afinal.databinding.ActivityViewBinding
import com.google.android.material.tabs.TabLayoutMediator

class ViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityViewBinding
    companion object{
        val fragments = listOf(
            GalleryFragment(),
            CameraFragment(),
            WebFragment(),
            BatteryFragment(),
            SettingsFragment()

        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "뷰 페이지"

        binding.viewPager.adapter = MyPagerAdapter(this)

        TabLayoutMediator(binding.tabs, binding.viewPager){tab,position->
            tab.text = when(position){
                0->"갤러리"
                1->"카메라"
                2->"웹"
                3->"배터리량"
                4->"뷰 설정"
                else->"탭"
            }
        }.attach()
    }
}

class MyPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity){
    override fun getItemCount(): Int{
        return ViewActivity.fragments.size
    }

    override fun createFragment(Position: Int): Fragment {
        return ViewActivity.fragments[Position]
    }
}
