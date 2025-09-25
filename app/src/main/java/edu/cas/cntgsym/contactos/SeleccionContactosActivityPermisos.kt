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
import edu.cas.cntgsym.util.Constantes

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
            //leerTodosLosTelefonos()
            mostrarContactos()
        } else {
            Log.d("MIAPP", "Permiso de Leer contactos DENEGADO")
            Toast.makeText(this, "Permiso de Leer contactos DENEGADO", Toast.LENGTH_LONG).show()
        }

    }

    private fun mostrarContactos() {
        val uri_contactos = ContactsContract.Contacts.CONTENT_URI//content://com.android.contacts/contacts
        val cursor_contactos = contentResolver.query(
            uri_contactos,
            null,
            null,
            null,
            null
        )

        cursor_contactos.use {
            if (it?.moveToFirst() == true)
            {
                do {
                    Log.d(Constantes.ETIQUETA_LOG, "NUM CONTACTOS = " + it.count)

                    val numColId = it.getColumnIndex(ContactsContract.Contacts._ID)
                    val numColNombre = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

                    val id = it.getLong(numColId)
                    val nombre = it.getString(numColNombre)

                    Log.d(Constantes.ETIQUETA_LOG, "Nombre = $nombre ID = $id")
                    mostrarCuentaRaw(id)
                    //mostrarCuentasRaw(id)
                } while (it.moveToNext())
            }
        }


    }

    fun mostrarCuentaRaw (id:Long):Unit
    {
        var cursor_raw = contentResolver.query(
            ContactsContract.RawContacts.CONTENT_URI,
            null,
            ContactsContract.RawContacts.CONTACT_ID + " = " +id,
            null,
            null
        )
        if (cursor_raw?.moveToFirst()==true){
            do {
                val columnaIdRaw = cursor_raw.getColumnIndex(ContactsContract.RawContacts._ID)
                val id_raw = cursor_raw.getLong(columnaIdRaw)

                val tipoIdRaw = cursor_raw.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_TYPE)
                val tipo_raw = cursor_raw.getString (tipoIdRaw)

                val nombreCuentaIdRaw = cursor_raw.getColumnIndex(ContactsContract.RawContacts.ACCOUNT_NAME)
                val nombreCuenta_raw = cursor_raw.getString(nombreCuentaIdRaw)

                Log.d(Constantes.ETIQUETA_LOG, "    (RAW) NOMBRE CUENTA = $nombreCuenta_raw TIPO CUENTA = $tipo_raw ID = $id_raw")

                mostrarDetalle(id_raw)


            } while (cursor_raw.moveToNext())
        }
        cursor_raw?.close()
    }

    fun mostrarDetalle (id_raw :Long):Unit
    {
        val cursor_data = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            ContactsContract.Data.RAW_CONTACT_ID +" = " + id_raw,
            null,
            null
        )

        cursor_data?.use {
            //it es el cursor

            if (it.moveToFirst()==true)
            {
                do {
                    val tipoMimeCol = it.getColumnIndex(ContactsContract.Data.MIMETYPE)
                    val tipoMime = it.getString(tipoMimeCol)
                    val dataCol = it.getColumnIndex(ContactsContract.Data.DATA1)
                    val data = it.getString(dataCol)

                    Log.d(Constantes.ETIQUETA_LOG, "        (DATA) MIME = $tipoMime DATA = $data")

                } while (it.moveToNext())
            }
        } //se cierra el cursor automáticamente si lo uso con use

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