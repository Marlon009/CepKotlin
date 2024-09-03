package com.example.cep

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL

class RespActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resp)

        val btnClose = findViewById<Button>(R.id.btnClose)
        val tvResp = findViewById<TextView>(R.id.tvResp) // Referência ao TextView
        val pbCep = findViewById<View>(R.id.pbCep) // Referência ao ProgressBar
        val cep = intent.getStringExtra("cep") ?: ""

        getCep(cep)

        btnClose.setOnClickListener {
            finish()
        }
    }

    private fun getCep(cep: String) {
        val urlString = "https://viacep.com.br/ws/$cep/json/"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = URL(urlString)
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = 7000

                val content = urlConnection.inputStream.bufferedReader().use(BufferedReader::readText)
                val json = JSONObject(content)

                withContext(Dispatchers.Main) {
                    // Atualize a UI com o JSON aqui
                    val city = json.optString("localidade", "Não disponível")
                    val state = json.optString("uf", "Não disponível")

                    // Atualizando a UI
                    val tvResp = findViewById<TextView>(R.id.tvResp)
                    val pbCep = findViewById<View>(R.id.pbCep)

                    tvResp.text = "Cidade: $city\nEstado: $state"
                    pbCep.visibility = View.GONE // Ocultar o ProgressBar quando terminar
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    // Atualize a UI com uma mensagem de erro
                    val tvResp = findViewById<TextView>(R.id.tvResp)
                    val pbCep = findViewById<View>(R.id.pbCep)

                    tvResp.text = "Erro ao obter dados"
                    pbCep.visibility = View.GONE // Ocultar o ProgressBar em caso de erro
                }
            }
        }
    }

    class Anime {
        private fun fadeIn(view: View) {
            val animation = AlphaAnimation(0f, 1f)
            animation.duration = 500L
            view.startAnimation(animation)
        }

        private fun fadeOut(view: View) {
            val animation = AlphaAnimation(1f, 0f)
            animation.duration = 500L
            view.startAnimation(animation)
        }

        fun tradeView(view1: View, view2: View) {
            fadeOut(view1)
            Handler().postDelayed({
                view1.visibility = View.INVISIBLE
                view2.visibility = View.VISIBLE
                fadeIn(view2)
            }, 500L)
        }
    }
}
