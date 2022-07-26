package fisei.moposita.kotlin_carritocompras.activities


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import fisei.moposita.kotlin_carritocompras.R
import fisei.moposita.kotlin_carritocompras.activities.Client.home.ClientHomeActivity_MCLB
import fisei.moposita.kotlin_carritocompras.models.ResponseHttp_MCLB
import fisei.moposita.kotlin_carritocompras.models.User_MCLB
import fisei.moposita.kotlin_carritocompras.providers.UsersProvider_MCLB
import fisei.moposita.kotlin_carritocompras.utils.SharedPref_MCLB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SaveImageActivity_MCLB : AppCompatActivity() {

    val TAG = "SaveImageActivity"
    var circleImageUser: CircleImageView? = null
    var buttonNext: Button? = null
    var buttonConfirm: Button? = null
    private var imageFile: File? = null

    var usersProviderMCLB : UsersProvider_MCLB? = null
    var userMCLB: User_MCLB? = null
    var sharedPrefMCLB: SharedPref_MCLB? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_image)

        sharedPrefMCLB = SharedPref_MCLB(this)
        getUserFromSession()
        usersProviderMCLB = UsersProvider_MCLB(userMCLB?.session_token)
        circleImageUser = findViewById(R.id.circleimage_user)
        buttonConfirm = findViewById(R.id.btn_confirm)
        buttonNext = findViewById(R.id.btn_next)

        circleImageUser?.setOnClickListener { selectImage() }
        buttonNext?.setOnClickListener { goToClientHome() }
        buttonConfirm?.setOnClickListener {saveImage()}
    }

    /*
    *   Para confirmar el cambio de imagen y proceder a subir a firebase
     */
    private fun saveImage() {
        if (imageFile != null && userMCLB != null) {
            usersProviderMCLB?.update(imageFile!!, userMCLB!!)?.enqueue(object : Callback<ResponseHttp_MCLB> {
                override fun onResponse(
                    call: Call<ResponseHttp_MCLB>,
                    responseMCLB: Response<ResponseHttp_MCLB>
                ) {
                    Log.d(TAG, "Response $responseMCLB")
                    Log.d(TAG, "BODY ${responseMCLB.body()}")
                    saveUserInSession(responseMCLB.body()?.data.toString())
                }

                override fun onFailure(call: Call<ResponseHttp_MCLB>, t: Throwable) {
                    Log.d(TAG, "Error no se pudo enviar la Imagen ${t.message}")
                    Toast.makeText(this@SaveImageActivity_MCLB, "Error ${t.message}", Toast.LENGTH_LONG)
                        .show()

                }
            })
        }
        else {
            Toast.makeText(this, "Imagen no puede se Nula ni tampoco el Usuario", Toast.LENGTH_LONG).show()
        }

    }


    /*
    * Saltar el paso de tomar foto
     */
    /*
  *   Navegar a la pantalla de Home si es la Autenticacion es Correcta
   */
    private fun goToClientHome() {
        val i = Intent(this, ClientHomeActivity_MCLB::class.java)
        i.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Elimina el historial de Pantallas
        startActivity(i)
    }

    /*
   *   Obtener la data almacena de Session de SharedPref
    */
    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPrefMCLB?.getData("user").isNullOrBlank()) {
            //Si el usuario Existe en Session
            userMCLB = gson.fromJson(sharedPrefMCLB?.getData("user"), User_MCLB::class.java)
        }
    }

    /*
    *   Para llamar a la accion de tomar de galeria o tomar foto
    *   Variable para captutar la imagen
     */
    private val startImageForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data?.data
                imageFile = File(fileUri?.path) // El archivo que vamos a guardar en servidor
                circleImageUser?.setImageURI(fileUri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Tarea cancela", Toast.LENGTH_LONG).show()
            }
        }

    private fun selectImage() {
        //ImagePicker Permite seleccionar de galeria o tomar foto
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent { intent ->
                startImageForResult.launch(intent)
            }
    }

    /*
   *   Almacenar el Session
    */
    private fun saveUserInSession(data: String) {
        Log.d("SharedPred", "Save : $data")

        val gson = Gson()
        val user = gson.fromJson(data, User_MCLB::class.java)
        Log.d("SharedPred", "Trasformacion : $user")
        sharedPrefMCLB?.save("user", user)
        goToClientHome()

    }
}