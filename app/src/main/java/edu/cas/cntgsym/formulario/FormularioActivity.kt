package edu.cas.cntgsym.formulario

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import edu.cas.cntgsym.R
import edu.cas.cntgsym.databinding.ActivityFormularioBinding
import edu.cas.cntgsym.formulario.model.Usuario
import edu.cas.cntgsym.formulario.preferences.PreferenciasUsuario
import edu.cas.cntgsym.util.Constantes

class FormularioActivity : AppCompatActivity() {

    lateinit var binding: ActivityFormularioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //implementamos validación campo nombre TextInputLayout Material
        binding.editTextNombreFormulario.setOnFocusChangeListener {
            vista, tieneFoco ->
            if (tieneFoco)
            {
                Log.d(Constantes.ETIQUETA_LOG, "La caja de nombre tiene el foco")
            } else {
                Log.d(Constantes.ETIQUETA_LOG, "La caja de nombre ha perdido el foco")
                if (!esNombreValido(this.binding.editTextNombreFormulario.text.toString()))
                {
                    binding.tilnombre.error = "Nombre incorrecto"
                } else {
                    binding.tilnombre.isErrorEnabled = false//si es válido
                }
            }

        }
    }

    fun esNombreValido (nombre:String): Boolean
    {
        return nombre.length>2
    }

    fun guardarUsuario(view: View) {
        val nombre:String = binding.editTextNombreFormulario.text.toString()
        val edad:Int = binding.editTextEdadFormulario.text.toString().toInt()
        val sexo:Char = if (binding.radioButtonHombre.isChecked)
        {
            'M'
        } else {
            'F'
        }
        val uriFoto = if (binding.imagenFormulario.tag == null)
        {
            "" //sin foto
        } else {
            binding.imagenFormulario.tag as String
        }
        val usuario = Usuario(nombre, edad, sexo, uriFoto)
        PreferenciasUsuario.guardarUsuarioPreferences(usuario, this)
        var snackbar = Snackbar.make(binding.main, "USUARIO GUARDADO", BaseTransientBottomBar.LENGTH_LONG )
        snackbar.setAction("DESHACER") {
                borrarUsuario(it)
            }

        snackbar.show()

    }
    fun borrarUsuario(view: View) {
        PreferenciasUsuario.borrarUsuarioPreferences(this)
        Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show()
        binding.editTextEdadFormulario.setText("")
        binding.editTextNombreFormulario.setText("")
        binding.radioGroup.clearCheck()
    }



}