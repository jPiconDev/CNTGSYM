package edu.cas.cntgsym.receptor

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import edu.cas.cntgsym.util.Constantes
import edu.cas.cntgsym.util.Notificaciones

class InicioMovilReceiver : BroadcastReceiver() {

    //EN LOS PIXEL comportamiento curioso: NO se lanza la señal de boot_completed
    //si en algunos al instalar
    //mi idea que cuando se inicie el teléfono este métod sea invocado automáticamente
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(Constantes.ETIQUETA_LOG, "en InicioMovilReceiver action = ${intent.action}")
        //TODO ver el inicio automático en PIXEL
        //EVITAR PEDIR AL USUARIO EL INICIO AUTOMÁTICO CADA VEZ
        Notificaciones.lanzarNotificacion(context)

    }
}