package edu.cas.cntgsym.foto

import android.app.Instrumentation
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.cas.cntgsym.R
import edu.cas.cntgsym.databinding.ActivityFotoBinding
import edu.cas.cntgsym.util.Constantes
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class FotoActivity : AppCompatActivity() {

    lateinit var binding: ActivityFotoBinding

    lateinit var uriFotoPrivada: Uri //file:// ruta privada de mi foto app
    lateinit var uriFotoPublica: Uri //content:// ruta pública de mi foto app

    val launcherIntentFoto = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
        resultado ->
        if (resultado.resultCode == RESULT_OK)
        {
            Log.d(Constantes.ETIQUETA_LOG, "La foto fue bien")
            binding.fotoTomada.setImageURI(this.uriFotoPublica)
            //acuatlizarGaleria()
        } else {
            Log.d(Constantes.ETIQUETA_LOG, "La foto fue mal")
        }
    }


    //otra forma de expresarlo con función anónima
    val launcherIntentFoto2 = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        fun(resultado) {
            if (resultado.resultCode == RESULT_OK) {
                Log.d(Constantes.ETIQUETA_LOG, "La foto fue bien")
                binding.fotoTomada.setImageURI(this.uriFotoPublica)
                //acuatlizarGaleria()
            } else {
                Log.d(Constantes.ETIQUETA_LOG, "La foto fue mal")
            }
        }
    )

//TODO revisar fallo
    /*val launcherIntentFoto3 = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        FotoActivity::postIntentFoto //function reference
    )

    fun postIntentFoto (resultado: ActivityResult): Unit
    {
        if (resultado.resultCode == RESULT_OK) {
            Log.d(Constantes.ETIQUETA_LOG, "La foto fue bien")
            binding.fotoTomada.setImageURI(this.uriFotoPublica)
            //acuatlizarGaleria()
        } else {
            Log.d(Constantes.ETIQUETA_LOG, "La foto fue mal")
        }
    }*/


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //este métod o se invoca antes de que la activdad muera / antes de recearse
        outState.putString("URI", this.uriFotoPublica.toString())
    }

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
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(Constantes.ETIQUETA_LOG, "PERMISO CÁMARA CONCEDIDO")
            lanzarCamara()
        } else {
            Log.d(Constantes.ETIQUETA_LOG, "PERMISO CÁMARA NO CONCEDIDO")
            Toast.makeText(this, "SIN PERMISOS PARA HACER FOTOS", Toast.LENGTH_LONG).show()
        }

    }

    private fun lanzarCamara() {
        //1 crear el fichero Destino
        val uri = crearFicheroDestino()
        //2 tirar el intent para hacer la foto con la uir del punto 1
        uri?.let { //si la uri es != null, se ejecuta la función let
            val intentFoto = Intent()
            intentFoto.setAction(MediaStore.ACTION_IMAGE_CAPTURE)
            //intentFoto.putExtra(MediaStore.EXTRA_OUTPUT, this.uriFotoPublica)
            intentFoto.putExtra(MediaStore.EXTRA_OUTPUT, this.uriFotoPublica)
            this.launcherIntentFoto.launch(intentFoto)
        } ?: run { //si la uri es null, se ejecuta la función este otro bloque
            Toast.makeText(this, "NO FUE POSIBLE CREAR EL FICHERO", Toast.LENGTH_LONG).show()
        }
    }

    private fun crearFicheroDestino(): Uri? {
        val fechaActual = Date()
        val momentoActual = SimpleDateFormat("yyyyMMdd_HHmmss").format(fechaActual)
        val nombreFichero = "FOTO_CNT_$momentoActual.jpg"

        //var rutaFoto = "${filesDir.path}/$nombreFichero" //MEMORIA INTERNA /data file:///data/user/0/edu.cas.cntgsym/files/FOTO_CNT_20250930_185107.jpg
        var rutaFoto =
            "${getExternalFilesDir(null)}/$nombreFichero" //MEMORIA EXTERNA PROPIA /sdcard file:///storage/emulated/0/Android/data/edu.cas.cntgsym/files/FOTO_CNT_20250930_185738.jpg
        val ficheroFoto = File(rutaFoto)
        try {
            ficheroFoto.createNewFile()
            this.uriFotoPrivada = ficheroFoto.toUri()
            Log.d(Constantes.ETIQUETA_LOG, "FICHERO CREADO URI PRIVADA $uriFotoPrivada")
            //tradúceme la ruta privada en la pública
            this.uriFotoPublica = FileProvider.getUriForFile(this, "edu.cas.cntgsym", ficheroFoto)
            Log.d(Constantes.ETIQUETA_LOG, "FICHERO CREADO URI PÚBLICA $uriFotoPublica")

        } catch (e: Exception) {
            Log.e(Constantes.ETIQUETA_LOG, "Error al crear el fichero destino de la foto", e)
        }
        return this.uriFotoPublica
    }
}