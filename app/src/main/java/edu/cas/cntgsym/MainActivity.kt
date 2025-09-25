package edu.cas.cntgsym

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
        mostrarAppsInstaladas()
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
}