package edu.cas.cntgsym.foto

import android.net.Uri
import androidx.lifecycle.ViewModel

//aquí se guarda el estado (los datos en un momento dado) de la actividad
//igual que el extends en Java FotoViewModel extends de ViewModel
class FotoViewModel: ViewModel() {

    var uriFoto: Uri? = null
}