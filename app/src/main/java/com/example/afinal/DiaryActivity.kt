package com.example.afinal

import android.app.DatePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.afinal.databinding.ActivityDiaryBinding
import com.google.android.material.datepicker.DateSelector
import java.io.File

class DiaryActivity : AppCompatActivity() {
    lateinit var binding: ActivityDiaryBinding
    private var fileName: String = ""

    private fun showDatePicker(onDateSelector: (Int, Int, Int) -> Unit){
        val today = Calendar.getInstance()
        DatePickerDialog(this,
            { _, year, month, dayOfMonth ->
                onDateSelector(year, month, dayOfMonth)
            },
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun readDiary(name: String): String{
        return try{
            val file = File(filesDir, name)
            file.bufferedReader(Charsets.UTF_8).use { it.readText() }
        } catch (e:Exception){
            "작성된 일기가 없습니다."
        }
    }

    private fun saveDiary(name:String, content: String){
        try{
            val file = File(filesDir, name)
            file.bufferedWriter(Charsets.UTF_8).use { it.write(content) }
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = "일기장"

        binding.save.isEnabled = false

        binding.newday.setOnClickListener {
            showDatePicker{year, month, day ->
                binding.inputtext.setText("")
                binding.inputtext.isEnabled = true
                fileName = "${year}년 ${month + 1}월 ${day}일"
                binding.diaryday.text = "작성날짜 : $fileName"
                binding.save.isEnabled = true
            }
        }

        binding.day.setOnClickListener {
            binding.save.isEnabled = false
            showDatePicker { year, month, day ->
                fileName = "${year}년 ${month + 1}월 ${day}일"
                binding.diaryday.text = "작성날짜 : $fileName"
                val content = readDiary(fileName)
                binding.inputtext.setText(content)
                binding.inputtext.isEnabled = false
            }
        }

        binding.save.setOnClickListener {
            saveDiary(fileName, binding.inputtext.text.toString())
            binding.save.isEnabled = false
            binding.inputtext.isEnabled = false
        }
    }
}