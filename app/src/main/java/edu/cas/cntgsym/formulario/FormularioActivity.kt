package edu.cas.cntgsym.formulario

import android.os.Bundle
import android.view.View
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

class FormularioActivity : AppCompatActivity() {

    lateinit var binding: ActivityFormularioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormularioBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        //TODO hacer una acción en el SNACKBAR para deshacer el guardado (borrar el fichero)
        snackbar.show()

    }
    fun borrarUsuario(view: View) {}
}