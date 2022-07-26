package fisei.moposita.kotlin_carritocompras.activities.Client.update

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import fisei.moposita.kotlin_carritocompras.R
import fisei.moposita.kotlin_carritocompras.models.ResponseHttp_MCLB
import fisei.moposita.kotlin_carritocompras.models.User_MCLB
import fisei.moposita.kotlin_carritocompras.providers.UsersProvider_MCLB
import fisei.moposita.kotlin_carritocompras.utils.SharedPref_MCLB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ClientUpdateActivity_MCLB : AppCompatActivity() {
    //TODO Variables
    val TAG = "ClientUpdateActivity"
    var circleImageUser: CircleImageView? = null
    var editTextNombre: EditText? = null
    var editTextApellido: EditText? = null
    var editTextDireccion: EditText? = null
    var editTextTelefono: EditText? = null
    var editTextCedula: EditText? = null
    var buttonUpdate: Button? = null

    var sharedPrefMCLB: SharedPref_MCLB? = null
    var userMCLB: User_MCLB? = null
    private var imageFile: File? = null
    var usersProviderMCLB : UsersProvider_MCLB? = null
    var toolbar : Toolbar?  = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_update)

        sharedPrefMCLB = SharedPref_MCLB(this)


        toolbar = findViewById(R.id.toolbar)
        toolbar?.title  = "Editar Perfil "
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.black))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //Mostrar la fecha para poder regresar


        buttonUpdate = findViewById(R.id.btn_update)
        circleImageUser = findViewById(R.id.circleimage_user_update)
        editTextNombre = findViewById(R.id.edittext_name)
        editTextApellido = findViewById(R.id.edittext_lastname)
        editTextDireccion = findViewById(R.id.edittext_direccion)
        editTextCedula = findViewById(R.id.edittext_cedula)
        editTextTelefono = findViewById(R.id.edittext_phone)

        getUserFromSession()
        circleImageUser?.setOnClickListener { selectImage() }
        buttonUpdate?.setOnClickListener { saveData() }


        usersProviderMCLB = UsersProvider_MCLB(userMCLB?.session_token)
        Log.d(TAG, "el Usuario almacenado es : ${userMCLB}")
        editTextNombre?.setText(userMCLB?.nombre)
        editTextApellido?.setText(userMCLB?.apellido)
        editTextDireccion?.setText(userMCLB?.direccion)
        editTextTelefono?.setText(userMCLB?.telefono)
        editTextCedula?.setText(userMCLB?.cedula)
        if (!userMCLB?.image.isNullOrBlank()) {
            Glide.with(this).load(userMCLB?.image).into(circleImageUser!!)
        }
    }


    /*
   *   Para confirmar el cambio de imagen y proceder a subir a firebase
    */
    private fun saveData() {
        //TODO Recoger Datos
        val name = editTextNombre?.text.toString()
        val dni = editTextCedula?.text.toString()
        val adress = editTextDireccion?.text.toString()
        val lastname = editTextApellido?.text.toString()
        val phone = editTextTelefono?.text.toString()

        //TODO Actualizo Usuario Session con los Datos Nuevos
        userMCLB?.nombre = name
        userMCLB?.apellido = lastname
        userMCLB?.cedula = dni
        userMCLB?.direccion = adress
        userMCLB?.telefono = phone


        //  Cuando solo actualizar los datos mas no la imagen
        if (imageFile != null) {
            usersProviderMCLB?.update(imageFile!!, userMCLB!!)?.enqueue(object : Callback<ResponseHttp_MCLB> {
                override fun onResponse(
                    call: Call<ResponseHttp_MCLB>,
                    responseMCLB: Response<ResponseHttp_MCLB>
                ) {
                    Log.d(TAG, "Response $responseMCLB")
                    Log.d(TAG, "BODY ${responseMCLB.body()}")
                    Toast.makeText(this@ClientUpdateActivity_MCLB , "La Actualizacion Correcta❤️", Toast.LENGTH_LONG).show()
                    //TODO Si la respuesta no me devuelve nada controlar el usuario sin datos
                    if(responseMCLB.body()?.isSuccess == true){
                        saveUserInSession(responseMCLB.body()?.data.toString())
                    }
                }

                override fun onFailure(call: Call<ResponseHttp_MCLB>, t: Throwable) {
                    Log.d(TAG, "Error no se pudo enviar la Imagen ${t.message}")
                    Toast.makeText(
                        this@ClientUpdateActivity_MCLB,
                        "Error ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()

                }
            })
        } else {
            usersProviderMCLB?.updateWithoutImage( userMCLB!!)?.enqueue(object : Callback<ResponseHttp_MCLB> {
                override fun onResponse(
                    call: Call<ResponseHttp_MCLB>,
                    responseMCLB: Response<ResponseHttp_MCLB>
                ) {
                    Log.d(TAG, "Response $responseMCLB")
                    Log.d(TAG, "BODY ${responseMCLB.body()}")
                    Toast.makeText(this@ClientUpdateActivity_MCLB , "La Actualizacion Correcta❤️", Toast.LENGTH_LONG).show()
                    //TODO Si la respuesta no me devuelve nada controlar el usuario sin datos
                    if(responseMCLB.body()?.isSuccess == true){
                        saveUserInSession(responseMCLB.body()?.data.toString())
                    }

                }

                override fun onFailure(call: Call<ResponseHttp_MCLB>, t: Throwable) {
                    Log.d(TAG, "Error no se pudo enviar la Imagen ${t.message}")
                    Toast.makeText(
                        this@ClientUpdateActivity_MCLB,
                        "Error ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()

                }
            })
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

    }

    /*
  *  TODO Obtener la data almacena de Session de SharedPref
   */
    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPrefMCLB?.getData("user").isNullOrBlank()) {
            //Si el usuario Existe en Session
            userMCLB = gson.fromJson(sharedPrefMCLB?.getData("user"), User_MCLB::class.java)

        }
    }

}