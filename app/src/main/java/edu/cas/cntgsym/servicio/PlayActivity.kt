package edu.cas.cntgsym.servicio

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.cas.cntgsym.R

class PlayActivity : AppCompatActivity() {

    lateinit var startButton: Button
    lateinit var stopButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        this.startButton = findViewById<Button>(R.id.botonstart)
        this.stopButton = findViewById<Button>(R.id.botonstop)

        //TODO programad los listener on click sobre los botones de 3 maneras:

        // 1. con una clase anónima
        startButton.setOnClickListener(object : android.view.View.OnClickListener {
            override fun onClick(v: android.view.View?) {
                // Iniciar el servicio
                val intent = Intent(this@PlayActivity, PlayService::class.java)
                startService(intent)
            }
        })
        stopButton.setOnClickListener(object : android.view.View.OnClickListener {
            override fun onClick(v: android.view.View?) {
                // Detener el servicio
                val intent = Intent(this@PlayActivity, PlayService::class.java)
                stopService(intent)
            }
        })

        // 2. con una función anónima
        /*startButton.setOnClickListener(fun(v: android.view.View?) {
            val intent = Intent(this@PlayActivity, PlayService::class.java)
            startService(intent)
        })

        stopButton.setOnClickListener(fun(v: android.view.View?) {
            val intent = Intent(this@PlayActivity, PlayService::class.java)
            stopService(intent)
        })*/

        // 3. con una función lambda
        /*startButton.setOnClickListener {
            val intent = Intent(this@PlayActivity, PlayService::class.java)
            startService(intent)
        }

        stopButton.setOnClickListener {
            val intent = Intent(this@PlayActivity, PlayService::class.java)
            stopService(intent)
        }*/

        // 4. con una function reference
        // startButton.setOnClickListener(this::startServiceClick)
        // stopButton.setOnClickListener(this::stopServiceClick)
    }

    // Funciones reference para el método 4
    fun startServiceClick(v: android.view.View?) {
        val intent = Intent(this@PlayActivity, PlayService::class.java)
        startService(intent)
    }

    fun stopServiceClick(v: android.view.View?) {
        val intent = Intent(this@PlayActivity, PlayService::class.java)
        stopService(intent)
    }
}