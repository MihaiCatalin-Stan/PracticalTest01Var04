package com.example.practicaltest01var04

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PracticalTest01Var04SecondaryActivity : AppCompatActivity() {
    private lateinit var buttonOkVar: Button
    private lateinit var buttonCancelVar: Button

    private lateinit var secTextVar1: TextView
    private lateinit var secTextVar2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_var04_secondary)

        buttonOkVar = findViewById<Button>(R.id.buttonOk)
        buttonCancelVar = findViewById<Button>(R.id.buttonCancel)

        secTextVar1 = findViewById<TextView>(R.id.secText1)
        secTextVar2 = findViewById<TextView>(R.id.secText2)

        val intent = getIntent()
        if (intent != null && intent.extras?.containsKey(Constants.TEXT1_KEY) == true) {
            secTextVar1.text = intent.getStringExtra(Constants.TEXT1_KEY)
        }

        if (intent != null && intent.extras?.containsKey(Constants.TEXT2_KEY) == true) {
            secTextVar2.text = intent.getStringExtra(Constants.TEXT2_KEY)
        }

        buttonOkVar.setOnClickListener {
            setResult(Constants.RESULT_OK, null)
            finish()
        }

        buttonCancelVar.setOnClickListener {
            setResult(Constants.RESULT_CANCEL, null)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}