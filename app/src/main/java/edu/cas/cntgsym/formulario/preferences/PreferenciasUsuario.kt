package edu.cas.cntgsym.formulario.preferences

import android.content.Context
import androidx.annotation.UiContext
import androidx.core.content.edit
import edu.cas.cntgsym.formulario.model.Usuario

//En este objeto, se gestionan los datos del usuario
object PreferenciasUsuario {

    const val NOMBRE_FICHERO_USUARIO = "usuario"

    fun guardarUsuarioPreferences (usuario: Usuario, context: Context)
    {
        val fichero_prefs = context.getSharedPreferences(NOMBRE_FICHERO_USUARIO, Context.MODE_PRIVATE)
        fichero_prefs.edit(commit = true) {
            putString("NOMBRE", usuario.nombre)
            putInt("EDAD", usuario.edad)
            putString("FOTO", usuario.uriFoto)
            putString("SEXO", usuario.sexo.toString())
        }
    }

    fun borrarUsuarioPreferences (context: Context)
    {
        val fichero_prefs = context.getSharedPreferences(NOMBRE_FICHERO_USUARIO, Context.MODE_PRIVATE)
        fichero_prefs.edit(commit = true) { clear() }
    }

    fun ficheroUsuarioVacio (context: Context): Boolean
    {
        val fichero_prefs = context.getSharedPreferences(NOMBRE_FICHERO_USUARIO, Context.MODE_PRIVATE)
        return fichero_prefs.all.isEmpty()
    }

    fun cargarUsuario (context: Context): Usuario
    {

        val fichero_prefs = context.getSharedPreferences(NOMBRE_FICHERO_USUARIO, Context.MODE_PRIVATE)
        val nombre =  fichero_prefs.getString("NOMBRE", "")
        val edad =  fichero_prefs.getInt("EDAD", 0)
        val sexo =  fichero_prefs.getString("SEXO", "F")
        val foto =  fichero_prefs.getString("FOTO", "")

        val sexochar = if (sexo=="M")
        {
            'M'
        } else {
            'F'
        }
        val usuario = Usuario(nombre!!, edad, sexochar, foto!!)
        return usuario
    }
  }