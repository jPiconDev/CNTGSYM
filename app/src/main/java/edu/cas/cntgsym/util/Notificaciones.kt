package edu.cas.cntgsym.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import edu.cas.cntgsym.MainActivity
import edu.cas.cntgsym.R

object Notificaciones {

    const val NOTIFICATION_CHANNEL_ID = "UNO"
    const val NOTIFICATION_CHANNEL_NAME = "CANAL_CNTGSYM"

    const val NOTIFICATION_CHANNEL_ID2 = "DOS"
    const val NOTIFICATION_CHANNEL_NAME2 = "CANAL_CNTGSYM2"

    //lanzar notificación

    //crear canal
    @RequiresApi(Build.VERSION_CODES.O)//ME PERMITE USAR EL API 8 Y SUPERIOR DENTRO DE LA F() ANOTADA ASÍ Y ADEMÁS, CONTROLA QUE EL LLAMANTE GESTIONE LA VERSIÓN
    //@TargetApi(Build.VERSION_CODES.O)//IGUAL QUE EL ANTERIOR, PERO NO CONTROLA QUE EL LLAMANTE GESTIONE LA VERSIÓN
    private fun crearCanalNotificacion (context: Context
    ): NotificationChannel?
    {
        var notificationChannel : NotificationChannel? = null


        notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT )
        notificationChannel.enableLights(true)
        notificationChannel.enableVibration(true)
        //vibración patron suena 500 ms, no vibra otros 500 ms
        notificationChannel.vibrationPattern = longArrayOf(
            500,
            500,
            500,
            500,
            500,
            500,
        )
        notificationChannel.lightColor = context.applicationContext.getColor(R.color.rojo)
        //sonido de  la notificación si el api es inferior a la 8, hay que setear el sonido aparte
        //si es igual o superior, la notificación "hereda" el sonido del canal asociado
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()

        notificationChannel.setSound(
            ("android.resource://" + context.packageName + "/" + R.raw.snd_noti).toUri(),
            audioAttributes
        )

        notificationChannel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC

        return notificationChannel
    }


    fun lanzarNotificacion (context:Context)


    {

        Log.d(Constantes.ETIQUETA_LOG, "Lanzando notificación ...")

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val notificationChannel =
                Notificaciones.crearCanalNotificacion(context)
            notificationManager.createNotificationChannel(notificationChannel!!)
        }

        //CREAMOS LA NOTIFICACIÓN
        val nb = NotificationCompat.Builder(context,
            NOTIFICATION_CHANNEL_ID
        )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.outline_casino_24)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.emoticono_risa))
            .setContentTitle("BUENOS DÍAS")
            .setSubText("aviso")
            .setContentText("Vamos a entrar en CNTGSYM app")
            .setAutoCancel(true)//es para que cuando toque la noti, desaparezca
            .setDefaults(Notification.DEFAULT_ALL)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
        {
            //nb.setSound(Uri.parse("android.resource://" + context.packageName + "/" + R.raw.snd_noti))
            nb.setSound(("android.resource://" + context.packageName + "/" + R.raw.snd_noti).toUri())
        }

        val intentDestino = Intent(context, MainActivity::class.java)
        //pendingIntent -- iNTENT "SECURIZADO" -- permite lanzar el intent, como si estuviera dentro de mi app
        val pendingIntent = PendingIntent.getActivity(context, 100,
            intentDestino, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE )
        nb.setContentIntent(pendingIntent)//asocio el intent a la notificación
        //si estoy en api anteriore, debo setea el sonido fuera porque no hay canal



        val notificacion = nb.build()

        //ADD PERMISOS
        notificationManager.notify(500, notificacion)
        Log.d(Constantes.ETIQUETA_LOG, "Notificación Lanzada")

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun crearCanalNotificacionForegroundService (context:Context, id: String, nombre:String): NotificationChannel {
        var notificationChannel : NotificationChannel? = null



        notificationChannel = NotificationChannel(
            id,
            nombre,
            NotificationManager.IMPORTANCE_LOW  // IMPORTANTE: usa LOW para servicios persistentes
        )
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

        return notificationChannel

    }
}