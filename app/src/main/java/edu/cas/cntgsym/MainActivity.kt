package edu.cas.cntgsym

import android.content.ComponentName
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.cas.cntgsym.contactos.SeleccionContactosActivity
import edu.cas.cntgsym.contactos.SeleccionContactosActivityPermisos
import edu.cas.cntgsym.util.Constantes

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

       // startActivity(Intent(this, SeleccionContactosActivity::class.java))
       // startActivity(Intent(this, SeleccionContactosActivityPermisos::class.java))
        Log.v(Constantes.ETIQUETA_LOG, "PRUEBA LOG VERBOSE")
        //mostrarAppsInstaladas()
        solicitarInicioAutomatico()
        gestionarPermisosNotis()
    }

    fun mostrarAppsInstaladas ()
    {
        var apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        Log.d(Constantes.ETIQUETA_LOG, "TIENES ${apps.size} aplicaciones instaladas")
        var listaOrdenadaApps = apps.sortedBy { it.packageName }
        listaOrdenadaApps.forEach {
            Log.d(Constantes.ETIQUETA_LOG, "Paquete = ${it.packageName} Nombre = ${packageManager.getApplicationLabel(it)}")
        }
    }

    fun solicitarInicioAutomatico()
    {
        val manufacturer = Build.MANUFACTURER
        try {
            val intent = Intent()
            if ("xiaomi".equals(manufacturer, ignoreCase = true)) {
                intent.setComponent(
                    ComponentName(
                        "com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity"
                    )
                )
            }
            //TODO controlar otros fabricantes

            startActivity(intent)
            //launcher.launch(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun gestionarPermisosNotis ()
    {
        val areNotificationsEnabled = NotificationManagerCompat.from(this).areNotificationsEnabled()

        if (!areNotificationsEnabled) {
            // Mostrar un diálogo al usuario explicando por qué necesita habilitar las notificaciones
            mostrarDialogoActivarNotis()
        }
        else {
            Log.d(Constantes.ETIQUETA_LOG, "Notis activas")
        }
    }

    private fun mostrarDialogoActivarNotis() {
        var dialogo = AlertDialog.Builder(this)
            .setTitle("AVISO NOTIFICACIONES") //i18n
            //.setTitle("AVISO")
            .setMessage("Para que la app funcione, debe ir a ajustes y activar las notificaciones")
            .setIcon(R.drawable.ic_launcher_foreground)
            .setPositiveButton("IR"){ dialogo, opcion ->
                Log.d(Constantes.ETIQUETA_LOG, "Opción positiva salir =  $opcion")
                //me lleva a los ajustes de notificaciones
                val intent = Intent().apply {
                    action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                }
                startActivity(intent)

            }
            .setNegativeButton("CANCELAR"){ dialogo: DialogInterface, opcion: Int ->
                Log.d(Constantes.ETIQUETA_LOG, "Opción negativa  =  $opcion")
                dialogo.dismiss()
            }


        dialogo.show()//lo muestro
    }
}