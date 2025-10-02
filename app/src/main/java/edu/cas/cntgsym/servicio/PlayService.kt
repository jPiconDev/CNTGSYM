package edu.cas.cntgsym.servicio

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import edu.cas.cntgsym.util.Constantes

class PlayService : Service() {

    override fun onCreate() {
        super.onCreate()
        Log.d(Constantes.ETIQUETA_LOG, "En onCreate() de PlayServicio")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
        Log.d(Constantes.ETIQUETA_LOG, "En onStartCommand() de PlayServicio")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(Constantes.ETIQUETA_LOG, "En onDestroy() de PlayServicio")
    }


    override fun onBind(intent: Intent): IBinder? {
        //esto sólo sería necesario programarlo si mi servicio actualiza una pantalla o similar
        //TODO("Return the communication channel to the service.")
        return null
    }
}