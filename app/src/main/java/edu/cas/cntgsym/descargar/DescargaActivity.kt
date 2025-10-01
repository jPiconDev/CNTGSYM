package edu.cas.cntgsym.descargar

import android.app.DownloadManager
import android.content.Context
import android.os.Bundle
import android.os.Environment
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.cas.cntgsym.R

class DescargaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_descarga)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun descargarCancion (){

        var urlCancion = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview122/v4/1e/5d/4c/1e5d4c43-b1e5-ab2a-92dd-74317c1f4d0d/mzaf_6717404211778358689.plus.aac.p.m4a"

        //PREPARO LA DESCARGA REQUEST
        val peticionDescarga = prepararDescarga(urlCancion)
        //ACCEDO AL SERVICIO DE DESCARGAS
        val downloadService = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    fun prepararDescarga(urlCancion:String): DownloadManager.Request
    {
        var peticion: DownloadManager.Request

            peticion = DownloadManager.Request(urlCancion.toUri())//qué descargo
            peticion.setMimeType("audio/mp3")
            peticion.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            peticion.setTitle("canción")
            peticion.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "cancionjose.mp3" )


        return peticion
    }
}