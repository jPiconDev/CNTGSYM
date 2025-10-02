package edu.cas.cntgsym.servicio

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.Browser
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.annotation.RequiresPermission
import edu.cas.cntgsym.R
import edu.cas.cntgsym.util.Constantes

class PlayService : Service() {


    companion object{

        // TODO ya funciona :) método
        // metodo
        var mediaPlayer: MediaPlayer? = null//ExoPlayer
        var sonando: Boolean = false //para controlar el estado de reprodución

        fun play(context: Context)
        {
            if (!sonando)
            {
                mediaPlayer = MediaPlayer.create(context, R.raw.snd_noti)
                mediaPlayer!!.start()
                sonando=true
                mediaPlayer!!.setOnCompletionListener {
                    //detener la canción
                    sonando = false
                    val intent = Intent(context, PlayService::class.java)
                    context.stopService(intent)
                }
            }
        }

        fun stop(context: Context)
        {
            mediaPlayer!!.stop()
            sonando=false
            mediaPlayer=null
            //paro el servicio
            // OJO añadido en esta implementación
            val intent = Intent(context, PlayService::class.java)
            context.stopService(intent)
        }
    }



    override fun onCreate() {
        super.onCreate()
        Log.d(Constantes.ETIQUETA_LOG, "En onCreate() de PlayServicio")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //return
        //super.onStartCommand(intent, flags, startId)
        Log.d(Constantes.ETIQUETA_LOG, "En onStartCommand() de PlayServicio")

        //la apariencia personalizada de la notificación
        val controlNotificacion = RemoteViews(this.packageName, R.layout.controles_reproductor)

        return START_STICKY//en el caso de que se pare el servicio, no se reinicia
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(Constantes.ETIQUETA_LOG, "En onDestroy() de PlayServicio")
        Toast.makeText(this, "Parando servicio onDestroy", Toast.LENGTH_SHORT).show()
    }


    override fun onBind(intent: Intent): IBinder? {
        //esto sólo sería necesario programarlo si mi servicio actualiza una pantalla o similar
        //TODO("Return the communication channel to the service.")
        return null
    }

    //esta clase recibirá la señal de cerrar la notificación
    class NotificationCloseButtonHandler : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context, "Close Seleccionado", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, PlayService::class.java)
            context?.stopService(intent)

            //también podría detener un servicio así
            /*
            //elimino el servicio del "foreground"
            stopForeground(STOP_FOREGROUND_REMOVE)
            //lo detengo
            stopSelf()
            //paro la música
            stop()
             */

        }
    }



    //esta clase recibirá la señal de reproducir la notificación
    class NotificationPlayButtonHandler : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context, "Botón Play seleccionado", Toast.LENGTH_SHORT).show()
            play(context!!)
        }
    }


    //esta clase recibirá la señal de hacia delante la notificación
    class NotificationSkipButtonHandler : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context, "Botón Skip seleccionado", Toast.LENGTH_SHORT).show()
        }
    }

    //esta clase recibirá la señal de hacia atrás la notificación
    class NotificationPrevButtonHandler : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context, "Botón Prev seleccionado", Toast.LENGTH_SHORT).show()
        }
    }
}