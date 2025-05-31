package com.example.afinal

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.afinal.MyService.Companion.interval
import com.example.afinal.databinding.ActivityStopwatchBinding

class StopwatchActivity : AppCompatActivity() {
    lateinit var binding: ActivityStopwatchBinding
    private var serviceBinder: MyService.MYBinder? = null
    private var checkbinder = false

    private val connection = object : ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            serviceBinder = service as MyService.MYBinder
            checkbinder = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            checkbinder = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStopwatchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title="스톱워치"

        val receiver = object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent?.action == "ACTION_TIMER"){
                    binding.timetx.text = intent.getIntExtra("current", -1).toString()
                    binding.dptx.text = "디스플레이 주기(${interval ?: "--"}초)"

                }else{
                    binding.timetx.text = "Timer"
                    binding.dptx.text = "디스플레이 주기(--초)"
                }
            }
        }

        registerReceiver(receiver, IntentFilter().apply {
            addAction("ACTION_TIMER")
            addAction("ACTION_TIMER_STOP")
        }, RECEIVER_EXPORTED)

        binding.startbt.setOnClickListener {
            if(serviceBinder == null){
                bindService(Intent(this,MyService::class.java), connection, Context.BIND_AUTO_CREATE )
            }else{
                serviceBinder?.start()
            }
            binding.startbt.text = "START"
        }

        binding.stopbt.setOnClickListener {
            binding.startbt.text = "START"
            serviceBinder?.stop()
            unbindService(connection)
            serviceBinder = null
        }

        binding.pausebt.setOnClickListener {
            binding.startbt.text = "RESTART"
            serviceBinder?.pause()
        }

        binding.plusbt.setOnClickListener {
            serviceBinder?.increaseInterval()
            binding.dptx.text = "디스플레이 주기(${interval ?: "--"}초)"
        }

        binding.minbt.setOnClickListener {
            serviceBinder?.decreaseInterval()
            binding.dptx.text = "디스플레이 주기(${interval ?: "--"}초)"
        }

    }

    override fun onStop() {
        serviceBinder?.stop()
        if(checkbinder){
            unbindService(connection)
            checkbinder=false
        }
        serviceBinder = null
        super.onStop()
    }

}