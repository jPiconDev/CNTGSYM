package edu.cas.cntgsym.contactos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.cas.cntgsym.R

/**
 * EN ESTA ACTIVIDAD VAMOS A SELECCIONAR 1 CONTACTO, CONSUMIENDO
 * EL CONTENT PROVIDER DE MANERA INDERECTA
 * NO HACE FALTA QUE PIDA PERMISOS
 *
 */
class SeleccionContactosActivity : AppCompatActivity() {

    //TODO falta parte visual formulario con los datos del contacto seleccionado

    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
        result: ActivityResult ->
        Log.d("MIAPP", "A la vuelta de la selección de un contacto")
        if (result.resultCode==RESULT_OK)
        {
            Log.d("MIAPP", "Contacto seleccionado OK")
            //ANDROID NOS DA PERMISOS PARA CONSULTAR ESTA URI TEMPORALMENTE
            // CUANDO ESTE PROCESO (ESTA INSTANCIA DE NUESTRA APP) FINALIZA
            //YA NO PUEDO VOLVER A ACCERDER. DEBO LANZAR EL INTENT DE NUEVO
            mostrarContacto(result.data!!)
        } else {
            Log.d("MIAPP", "Contacto seleccionado MAL/CANCELADO")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seleccion_contactos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        selectContacto()
        //PRUEBA :: conocida la URI de un contacto, vamos a intentar recuperar la info directamente (FALLA POR FALTA DE PERMISOS)
        //mostrarContacto("content://com.android.contacts/data/6996".toUri())

    }

    fun mostrarContacto(intentDatosContacto: Intent) {
        Log.d("MIAPP", "CONTACTO = ${intentDatosContacto.data}")
        val cursor = contentResolver.query(intentDatosContacto.data!!, null, null, null, null)
        if (cursor!!.moveToFirst()) //si la consulta ha devuelto resultados
        {
            with(cursor) //me despreocupo de cerrar - Autoclosable se cierra solo con el with
            {
                //cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME) // con with me ahorro además nombrar al objeto en este ámbito {}
                val colNombre = getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val colNumero = getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val nombre = getString(colNombre)
                val numero = getString(colNumero)

                Log.d("MIAPP", "NOMBRE = $nombre y NÚMERO = $numero CONTACTO SELECCIONADO")
            }
        }
    }

    fun mostrarContacto (uriContacto: Uri)
    {
        Log.d("MIAPP", "CONTACTO = ${uriContacto}")
        val cursor = contentResolver.query(uriContacto, null, null, null, null)
        if (cursor!!.moveToFirst()) //si la consulta ha devuelto resultados
        {
            with(cursor) //me despreocupo de cerrar - Autoclosable se cierra solo con el with
            {
                //cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME) // con with me ahorro además nombrar al objeto en este ámbito {}
                val colNombre = getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val colNumero = getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val nombre = getString(colNombre)
                val numero = getString(colNumero)

                Log.d("MIAPP", "NOMBRE = $nombre y NÚMERO = $numero CONTACTO SELECCIONADO")
            }
        }
    }


    fun selectContacto() {
        Log.d("MIAPP", "URI CONTENT PROVIDER TELFS ${ContactsContract.CommonDataKinds.Phone.CONTENT_URI}")


        val intentContactos = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        if (intentContactos.resolveActivity(packageManager)!=null)
        {
            //hay una app de contactos
            Log.d("MIAPP", "Lanzando app contactos")
            launcher.launch(intentContactos)
        } else {
            Log.d("MIAPP", "Sin app de contactos")
        }

    }


}