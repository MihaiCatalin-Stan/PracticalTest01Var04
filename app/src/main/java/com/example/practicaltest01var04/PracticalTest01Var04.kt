package com.example.practicaltest01var04

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PracticalTest01Var04 : AppCompatActivity() {
    private lateinit var buttonDisp: Button
    private lateinit var buttonNav: Button

    private lateinit var cb1: CheckBox
    private lateinit var cb2: CheckBox

    private lateinit var text1: EditText
    private lateinit var text2: EditText

    private lateinit var textDisp: TextView

    private val messageBroadcastReceiver = MessageBroadcastReceiver()
    var isRegsteredReceiver = false
    private val intentFilter = IntentFilter()

    private var serviceStatus = Constants.SERVICE_STOPPED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_var04_main)

        for (action in Constants.SERVICE_ACTIONS) {
            intentFilter.addAction(action)
        }

        buttonDisp = findViewById<Button>(R.id.dispButton)
        buttonNav = findViewById<Button>(R.id.navSec)

        cb1 = findViewById<CheckBox>(R.id.cb1)
        cb2 = findViewById<CheckBox>(R.id.cb2)

        text1 = findViewById<EditText>(R.id.text1)
        text2 = findViewById<EditText>(R.id.text2)

        textDisp = findViewById<TextView>(R.id.dispText)

        buttonDisp.setOnClickListener {
            var textToShow = ""

            if (cb1.isChecked) {
                if (text1.text.toString().isEmpty())
                    Toast.makeText(this, "First field is empty", Toast.LENGTH_SHORT).show()
                else
                    textToShow += text1.text
            }

            if (cb2.isChecked) {
                if (text2.text.toString().isEmpty())
                    Toast.makeText(this, "Second field is empty", Toast.LENGTH_SHORT).show()
                else
                    textToShow += text2.text
            }

            textDisp.text = textToShow

            if (!text1.text.toString().isEmpty() && !text2.text.toString().isEmpty() &&
                serviceStatus == Constants.SERVICE_STOPPED) {
                Log.d("[ColocviuModelOCW]", "Starting service")
                val intent = Intent(applicationContext, PracticalTest01Var04Service::class.java)
                intent.putExtra(Constants.TEXT1_KEY_SERVICE, text1.text.toString())
                intent.putExtra(Constants.TEXT2_KEY_SERVICE, text2.text.toString())

                applicationContext.startService(intent)
                serviceStatus = Constants.SERVICE_STARTED
            }
        }

        val activityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result ->
            if (result.resultCode == Constants.RESULT_OK) {
                Toast.makeText(this, "User pressed OK", Toast.LENGTH_SHORT).show()
            } else if (result.resultCode == Constants.RESULT_CANCEL) {
                Toast.makeText(this, "User pressed Cancel", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "User pressed ???", Toast.LENGTH_SHORT).show()
            }
        }

        buttonNav.setOnClickListener {
            val intent =
                Intent(this@PracticalTest01Var04, PracticalTest01Var04SecondaryActivity::class.java)
            intent.putExtra(Constants.TEXT1_KEY, text1.text.toString())
            intent.putExtra(Constants.TEXT2_KEY, text2.text.toString())
            activityLauncher.launch(intent)
        }

        if (savedInstanceState != null) {
            var restoredVal = ""

            if (savedInstanceState.containsKey(Constants.TEXT1)) {
                restoredVal = savedInstanceState.getString(Constants.TEXT1, "")
            }
            text1.setText(restoredVal)

            if (savedInstanceState.containsKey(Constants.TEXT2)) {
                restoredVal = savedInstanceState.getString(Constants.TEXT2, "")
            }
            text2.setText(restoredVal)

            if (savedInstanceState.containsKey(Constants.TEXT_DISP)) {
                restoredVal = savedInstanceState.getString(Constants.TEXT_DISP, "")
            }
            textDisp.text = restoredVal
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onSaveInstanceState(
        savedInstanceState: Bundle
    ) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putString(Constants.TEXT1, text1.text.toString())
        savedInstanceState.putString(Constants.TEXT2, text2.text.toString())
        savedInstanceState.putString(Constants.TEXT_DISP, textDisp.text.toString())

        Log.d("[ColocviuModelOCW]",
            "onSaveInstanceState() called and ${text1.text} ${text2.text}"
        )
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d("[ColocviuModelOCW]", "onRestoreInstanceState() called")
        var restoredVal = ""

        if (savedInstanceState.containsKey(Constants.TEXT1)) {
            restoredVal = savedInstanceState.getString(Constants.TEXT1, "")
        }
        text1.setText(restoredVal)

        if (savedInstanceState.containsKey(Constants.TEXT2)) {
            restoredVal = savedInstanceState.getString(Constants.TEXT2, "")
        }
        text2.setText(restoredVal)

        if (savedInstanceState.containsKey(Constants.TEXT_DISP)) {
            restoredVal = savedInstanceState.getString(Constants.TEXT_DISP, "")
        }
        textDisp.text = restoredVal
    }

    override fun onPause() {
        Log.d("[ColocviuModelOCW]", "onPause() called")
        if (isRegsteredReceiver) {
            unregisterReceiver(messageBroadcastReceiver)
            isRegsteredReceiver = false
        }

        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        Log.d("[ColocviuModelOCW]", "onStop() called")
    }

    override fun onStart() {
        super.onStart()
        Log.d("[ColocviuModelOCW]", "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("[ColocviuModelOCW]", "onResume() called")

        if (!isRegsteredReceiver) {
            registerReceiver(messageBroadcastReceiver, intentFilter, RECEIVER_EXPORTED)
            isRegsteredReceiver = true
        }
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("[ColocviuModelOCW]", "onRestart() called")
    }

    override fun onDestroy() {
        Log.d("[ColocviuModelOCW]", "onDestroy() called")
        val intent: Intent = Intent(this, PracticalTest01Var04Service::class.java)
        stopService(intent)
        super.onDestroy()
    }

    private inner class MessageBroadcastReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("[ColocviuModelOCW]", intent?.getStringExtra(Constants.BROADCAST_KEY) ?: "")
        }
    }

}