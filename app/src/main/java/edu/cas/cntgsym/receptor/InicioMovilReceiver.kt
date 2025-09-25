package edu.cas.cntgsym.receptor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import edu.cas.cntgsym.util.Constantes

class InicioMovilReceiver : BroadcastReceiver() {

    //mi idea que cuando se inicie el teléfono este métod sea invocado automáticamente
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(Constantes.ETIQUETA_LOG, "en InicioMovilReceiver")
        //TODO lanzaremos una notifación Y SOLICITAR ACTIVACIÓN DE INICIO AUTOMÁTICO POR intent en ajustes

    }
}