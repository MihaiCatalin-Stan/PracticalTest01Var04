package com.example.practicaltest01var04

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.provider.SyncStateContract
import android.util.Log
import java.util.Date
import kotlin.math.sqrt

class PracticalTest01Var04Service : Service() {

    private lateinit var processingThread: ProcessingThread

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var firstText = intent?.getStringExtra(Constants.TEXT1_KEY_SERVICE) ?: ""
        var secondText = intent?.getStringExtra(Constants.TEXT2_KEY_SERVICE) ?: ""

        Log.d("[ColocviuModelOCW]", "Service started")

        processingThread = ProcessingThread(this, firstText, secondText)

        processingThread.start()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder ?= null

    override fun onDestroy() {
        processingThread.stopThread()
        Log.d("[ColocviuModelOCW]", "Service stopped")
        super.onDestroy()
    }
}

private class ProcessingThread(
    private val context: Context, var text1rec: String, var text2rec: String
): Thread() {

    @Volatile
    var isRunning = true

    override fun run() {
        Log.d("[ColocviuModelOCW]", "Thread started!")

        while (isRunning) {
            sendMessage()
            sleepThread()
        }
        Log.d("[ColocviuModelOCW]", "Thread stopped!!!")
    }

    private fun sleepThread() {
        try {
            sleep(5000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun sendMessage() {
        var intent = Intent().apply {
            action = Constants.SERVICE_ACTIONS.random()
            putExtra(Constants.BROADCAST_KEY, "$text1rec $text2rec")
        }

        context.sendBroadcast(intent)
    }

    fun stopThread() {
        isRunning = false
    }
}