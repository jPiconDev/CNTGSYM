package edu.cas.cntgsym.contactos

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
        //SI TENGO PERMISOS, LEO LOS CONTACTOS
        //SI NO, PIDO PERMISOS
        //OJO: si mi app va orientada minSdk a apps anteriores a 23, no tengo que pedir permisos
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), 500)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0]== PackageManager.PERMISSION_GRANTED)
        {
            Log.d("MIAPP", "Permiso de Leer contactos concedidos")
            leerTodosLosTelefonos()
        } else {
            Log.d("MIAPP", "Permiso de Leer contactos DENEGADO")
            Toast.makeText(this, "Permiso de Leer contactos DENEGADO", Toast.LENGTH_LONG).show()
        }

    }

    private fun leerTodosLosTelefonos() {
        //VAMOS A LEER TODOS LOS CONTACTOS
        val cursorTelefonos = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null)

        var nColumna = 0
        var numero = ""
        while (cursorTelefonos!!.moveToNext())
        {
            nColumna = cursorTelefonos.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            numero = cursorTelefonos.getString(nColumna)
            Log.d("MIAPP", "TELEFONO $numero")
        }
        cursorTelefonos.close()
    }
}