package edu.cas.cntgsym.biometrico

import android.app.KeyguardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import edu.cas.cntgsym.R
import edu.cas.cntgsym.util.Constantes

class BioActivity : AppCompatActivity() {

    lateinit var biometricPrompt: BiometricPrompt //el diálogo
    lateinit var promptInfo: BiometricPrompt.PromptInfo //el mensaje

    val launcherPromptPinOPatron = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
        if (it.resultCode == RESULT_OK)
        {
            Toast.makeText(this, "Autenticación Correcta", Toast.LENGTH_LONG).show()
            //TODO darle acceso a la actividad protegida
        } else {
            Toast.makeText(this, "Autenticación INCorrecta", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bio)

        mostrarDialogoBiometrico()

    }

    private fun mostrarDialogoBiometrico() {
        val biometricManager = BiometricManager.from(this)

        //vamos a comprobar qué biometría tiene configurada el dispositivo

        when (biometricManager.canAuthenticate
            (BiometricManager.Authenticators.BIOMETRIC_WEAK or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL))
        {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                //tienes la huella configurada
                prepararDialogoBiometrico()
                biometricPrompt.authenticate(promptInfo)
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE,
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
                 ->{
                     //PROBAR PATRÓN
                    Log.d(Constantes.ETIQUETA_LOG, "SIN BIOMETRÍA")
                intentarAutenticacionBasica()
                 }
        }
    }

    private fun prepararDialogoBiometrico() {
        val executor= ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(this@BioActivity, "Autenticación exitosa", Toast.LENGTH_LONG).show()
                    //TODO aquí ya, lanzo la tarea o la actividad protegida
                }

                //superamos el número de intentos fallidos
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(this@BioActivity, "onAuthenticationError", Toast.LENGTH_LONG).show()

                    if ((errorCode == BiometricPrompt.ERROR_LOCKOUT) ||
                        (errorCode == BiometricPrompt.ERROR_LOCKOUT_PERMANENT))
                    {
                        //probar con el patrón/pin
                        Log.d(Constantes.ETIQUETA_LOG, "FALLO BIOMETRÍA probar otra")
                        intentarAutenticacionBasica()
                    }
                }

                //huella incorrecta
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(this@BioActivity, "Huella incorrecta", Toast.LENGTH_LONG).show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticación requerida")
            .setSubtitle("Usa tu huella o el PIN")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()
    }

    fun intentarAutenticacionBasica()
    {
       //patrón o pin disponible desde la 6 api 23, como nuestra minsdk 24...sin problema
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (keyguardManager.isKeyguardSecure)
        {
            val intent = keyguardManager.createConfirmDeviceCredentialIntent(
                "Autenticación Requerida",
                "Confirma tu pin o contraseña"
            )

            launcherPromptPinOPatron.launch(intent)

        } else {
            Toast.makeText(this, "Sin PIN o patrón configurados", Toast.LENGTH_LONG).show()
            //finish()
            finishAffinity()//con esto cierro mi app
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(Constantes.ETIQUETA_LOG, "ON DESTROY")
    }
}