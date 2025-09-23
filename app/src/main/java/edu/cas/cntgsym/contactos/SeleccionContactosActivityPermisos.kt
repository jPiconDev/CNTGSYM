package edu.cas.cntgsym.contactos

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.cas.cntgsym.R

/**
 * AQUÍ VAMOS A CONSULTAR LA AGENDA DE CONTACTOS ÍNTEGRAMENTE (CONSULTA GLOBAL)
 * DE CADA CADA CONTACTO VOY A OBTENER SUS CUENTAS (WHTASAPP, TELEGRAM)
 * DE CADA CUENTA, VOY A SACAR SU DETALLES (DATA)
 * SÍ NECESITARÉ PERMISOS
 */
class SeleccionContactosActivityPermisos : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seleccion_contactos_permisos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}