package edu.cas.cntgsym.foto

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.cas.cntgsym.R
import edu.cas.cntgsym.databinding.ActivityFotoBinding
import edu.cas.cntgsym.util.Constantes

class FotoActivity : AppCompatActivity() {

    lateinit var binding: ActivityFotoBinding

    lateinit var uriFotoPrivada: Uri //file:// ruta privada de mi foto app
    lateinit var uriFotoPublica: Uri //content:// ruta pública de mi foto app

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun tomarFoto(view: View) {
        pedirPermisos()
    }

    private fun pedirPermisos() {
        requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 632)
    }

    //OJO SÓLO USAR ESTE MÉTOD O EN DISPOISITVOS CON API 35
    /*override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
    }*/

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            Log.d(Constantes.ETIQUETA_LOG, "PERMISO CÁMARA CONCEDIDO")
            lanzarCamara()
        } else {
            Log.d(Constantes.ETIQUETA_LOG, "PERMISO CÁMARA NO CONCEDIDO")
            Toast.makeText(this, "SIN PERMISOS PARA HACER FOTOS", Toast.LENGTH_LONG).show()
        }

    }

    private fun lanzarCamara() {
        //1 crear el fichero Destino
        //2 tirar el intent para hacer la foto con la uir del punto 1
    }
}