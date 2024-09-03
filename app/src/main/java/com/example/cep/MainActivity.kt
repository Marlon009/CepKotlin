package com.example.cep

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnSend = findViewById<Button>(R.id.btnSend)
        val etCep = findViewById<EditText>(R.id.etCep) // Correção: Referenciando como EditText

        btnSend.setOnClickListener {
            val cep = etCep.text.toString()
            if (cep.length == 8) {
                val intent = Intent(this, RespActivity::class.java)
                intent.putExtra("cep", etCep.text.toString()) // Correção: Usando parênteses
                startActivity(intent) // Correção: Passando a intent para startActivity
            }
            else{
                Toast.makeText(this, "O CEP contém 8 números.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
