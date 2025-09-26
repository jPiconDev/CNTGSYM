package edu.cas.cntgsym.persistenciavector

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.cas.cntgsym.R

class SpinnerVectorActivity : AppCompatActivity() {


    //TODO Usar ViewBinding / DataBinding
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_spinner_vector)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        iniciarSpinner()
    }

    private fun iniciarSpinner() {
        val adapter = ArrayAdapter.createFromResource(
            this, R.array.visibilidad, //los valores de persistencia
            android.R.layout.simple_spinner_item //estilo de la fila
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) //estilo lista desplegada

       val spinner = findViewById<Spinner>(R.id.spinnerVisibilidad)
        spinner.adapter = adapter
    }
}