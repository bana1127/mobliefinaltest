package com.example.afinal

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.SystemClock
import kotlin.concurrent.thread

class MyService : Service() {
    companion object {
        var interval = 1
        var currentTime = 0
        var bPaused = false
        var bStarted = true
    }
    override fun onBind(intent: Intent): IBinder {
        bStarted = true
        thread {
            while (bStarted){
                SystemClock.sleep(interval * 1000L)
                if(!bPaused){
                    currentTime += interval
                    val intent = Intent("ACTION_TIMER")
                    intent.putExtra("current", currentTime)
                    sendBroadcast(intent)
                }
            }
            sendBroadcast(Intent("ACTION_TIMER_STOP"))
        }
        return MYBinder()
    }

    override fun onDestroy() {
        bStarted = false
        super.onDestroy()
    }

    inner class MYBinder: Binder(){
        fun increaseInterval(){
            interval++
        }

        fun decreaseInterval(){
            if(interval > 1){
                interval--
            }
        }

        fun getInterval(): Int = interval

        fun pause() {
            bPaused = true
        }

        fun start(){
            bPaused = false
        }

        fun stop(){
            bStarted = false
            currentTime = 0
            interval = 1
        }
    }
}