package edu.cas.cntgsym.formulario

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
    var lanzadorIntentImagen: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        //gestionar la vuelta de la selección de foto
        result ->
        if (result.resultCode==RESULT_OK){
            Log.d(Constantes.ETIQUETA_LOG, "El usuario ha seleccionado una foto")
            Log.d(Constantes.ETIQUETA_LOG, "URI Foto seleccianada ${result.data?.data} ")
            binding.imagenFormulario.setImageURI(result.data?.data)
            binding.imagenFormulario.scaleType = ImageView.ScaleType.CENTER_CROP


        }
    }

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

        binding.imagenFormulario.setOnClickListener {
            imagen -> seleccionarFoto()
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

private fun FormularioActivity.seleccionarFoto() {
   //TODO lanzar el intent para seleccionar una foto de la galería/del dispoistivo
    val intentGaleria = Intent(Intent.ACTION_PICK)
    //val intentGaleria = Intent(Intent.ACTION_GET_CONTENT)//seleccionar un documento
    intentGaleria.type = "image/*" //quiero obtener una foto

    if (intentGaleria.resolveActivity(packageManager)!=null)
    {
        Log.d(Constantes.ETIQUETA_LOG, "Hay al menos una app que puede ofrecer imágenes")
        lanzadorIntentImagen.launch(intentGaleria)
    } else {
        Log.d(Constantes.ETIQUETA_LOG, "NO HAY una app que puede ofrecer imágenes")
        Toast.makeText(this, "SIN APPS DE IMÁGENES", Toast.LENGTH_LONG).show()
    }
}
