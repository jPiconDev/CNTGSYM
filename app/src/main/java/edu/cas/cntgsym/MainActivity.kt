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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import edu.cas.cntgsym.contactos.SeleccionContactosActivity
import edu.cas.cntgsym.contactos.SeleccionContactosActivityPermisos
import edu.cas.cntgsym.util.Constantes
import androidx.core.content.edit
import edu.cas.cntgsym.persistenciavector.SpinnerVectorActivity

class MainActivity : AppCompatActivity() {


    var launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
        Log.d(Constantes.ETIQUETA_LOG, "A la vuelta de ajuste de autonicio")
        //guardarnos que ya le hemos mostrado este menú una vez
        //mira si hay un fichero xml con este nombre
        //si lo hay, me lo devuelve y si no, me lo crea
        val ficheroPreferencias = getSharedPreferences(Constantes.FICHERO_PREFS_AJUSTES, MODE_PRIVATE)
        ficheroPreferencias.edit(commit = true) {
            putBoolean("INICIO_AUTO", true)
        }
    }

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
        startActivity(Intent(this, SpinnerVectorActivity::class.java))
        //Log.v(Constantes.ETIQUETA_LOG, "PRUEBA LOG VERBOSE")
        //mostrarAppsInstaladas()
        val ficheroPreferencias = getSharedPreferences(Constantes.FICHERO_PREFS_AJUSTES, MODE_PRIVATE)
        val inicio_auto = ficheroPreferencias.getBoolean("INICIO_AUTO", false)
        if (!inicio_auto)
        {
            Log.d(Constantes.ETIQUETA_LOG, "USO PRIMERA VEZ APP, pido ajuste auto")
            solicitarInicioAutomatico()
        } else {
            Log.d(Constantes.ETIQUETA_LOG, "YA HEMOS PEDIDO EL AJUSTE UNA VEZ, no mostramos el menú")
        }

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

            //startActivity(intent)
            //lo lanzo como subactividad para tener el control y "apuntarme" que ya le he pedido
            //eso una vez, y no quiero ser pesado
            launcher.launch(intent)
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