package com.example.bmicalculator

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    lateinit var sf: SharedPreferences
    lateinit var editor: Editor

    lateinit var weightText: EditText
    lateinit var heightText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sf = getSharedPreferences("sf_main", Context.MODE_PRIVATE)
        editor = sf.edit()
        weightText = findViewById(R.id.etWeight)
        heightText = findViewById(R.id.etHeight)

        val calButton = findViewById<Button>(R.id.btnCalculate)

        calButton.setOnClickListener{
            val weight = weightText.text.toString()
            val height = heightText.text.toString()
            if (validateInputs(weight, height)){
                val bmi = weight.toFloat() / ((height.toFloat()/100) * (height.toFloat()/100))
                val bmi2Digits = String.format("%.2f", bmi).toFloat()
                displayResult(bmi2Digits)
            }

        }
    }
    private fun validateInputs(weight: String?, height: String?) : Boolean{
        return when{
            weight.isNullOrEmpty() -> {
                Toast.makeText(this, "Please provide a weight", Toast.LENGTH_SHORT).show()
                return false
            }
            height.isNullOrEmpty() -> {
                Toast.makeText(this, "Please provide a height", Toast.LENGTH_SHORT).show()
                return false
            }
            else -> {
                return true
            }
        }
    }
    private fun displayResult(bmi: Float){
        val resultIndex = findViewById<TextView>(R.id.tvIndex)
        val resultDescription = findViewById<TextView>(R.id.tvResult)
        val info = findViewById<TextView>(R.id.tvInfo)

        resultIndex.text = bmi.toString()
        info.text = "(Normal range is 18.5 - 24.9)"

        var resultText = ""
        var colour = 0

        when {
            bmi < 18.50 -> {
                resultText = "Underweight"
                colour = R.color.under_weight
            }
            bmi in 18.50..24.99 ->{
                resultText = "Healthy"
                colour = R.color.normal_weight
            }
            bmi in 25.00..29.99 ->{
                resultText = "Overweight"
                colour = R.color.over_weight
            }
            bmi > 29.99 ->{
                resultText = "Obese"
                colour = R.color.obese_weight
            }
        }
        resultDescription.setTextColor(ContextCompat.getColor(this, colour))
        resultDescription.text = resultText
    }
    override fun onPause() {
        super.onPause()
        val weight = weightText.text.toString().toFloat()
        val height = heightText.text.toString().toFloat()
        editor.apply{
            putFloat("sf_weight", weight)
            putFloat("sf_height", height)
            commit()
        }
    }

    override fun onResume() {
        super.onResume()
        val weight = sf.getFloat("sf_weight", 0f)
        val height = sf.getFloat("sf_height", 0f)
        if (weight > 0){
            weightText.setText(weight.toString())
        }
        if (height > 0){
            heightText.setText(height.toString())
        }
    }
}