package edu.cas.cntgsym.descargar

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import edu.cas.cntgsym.util.Constantes

class DescargarReceiver : BroadcastReceiver() {

    var idDescarga: Long =-1

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(Constantes.ETIQUETA_LOG, "La descarga ha finalizado")

        //consultar el estado de la descarga
        val consulta = DownloadManager.Query()
        consulta.setFilterById(idDescarga)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val cursor = downloadManager.query(consulta)

        cursor.use {
            if (cursor.moveToFirst())
            {
                val numColStatus = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val numColUri = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                var status = cursor.getInt(numColStatus)
                var uri = cursor.getString(numColUri)

                Log.d(Constantes.ETIQUETA_LOG, "Estado descarga $status URI $uri")
                if (status == DownloadManager.STATUS_FAILED)
                {
                    Log.d(Constantes.ETIQUETA_LOG, "La descarga fue mal")
                //} else if (status == DownloadManager.STATUS_SUCCESSFUL) {
                } else  {
                    Log.d("MIAPP", "La descarga fue bien")
                }
                //podría retornar a la actividad
                //podría reproducir
            }

        }


        //desregistar el receptor
        context.unregisterReceiver(this)

    }
}