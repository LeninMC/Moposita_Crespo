package fisei.moposita.kotlin_carritocompras.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.gson.Gson
import fisei.moposita.kotlin_carritocompras.R
import fisei.moposita.kotlin_carritocompras.models.ResponseHttp_MCLB
import fisei.moposita.kotlin_carritocompras.models.User_MCLB
import fisei.moposita.kotlin_carritocompras.providers.UsersProvider_MCLB
import fisei.moposita.kotlin_carritocompras.utils.SharedPref_MCLB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity_MCLB : AppCompatActivity() {
    val TAG = "RegisterActivity"
    var imageViewGoToLogin: ImageView? = null
    var editTextEmail: EditText? = null
    var editTextName: EditText? = null
    var editTextLastName: EditText? = null

    var editTextCedula: EditText? = null
    var editTextDireccion: EditText? = null
    var editTextPhone: EditText? = null
    var editTextPassword: EditText? = null
    var editTextConfirmPassword: EditText? = null
    var buttonRegister: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        /*
        *   INSTANCIAR LAS VARIABLES DECLARADAS ANTERIORMENTE
         */
        imageViewGoToLogin = findViewById(R.id.imageview_go_to_login)
        editTextEmail = findViewById(R.id.edittext_email)
        editTextName = findViewById(R.id.edittext_name)
        editTextLastName = findViewById(R.id.edittext_lastname)
        editTextCedula = findViewById(R.id.edittext_cedula)
        editTextDireccion = findViewById(R.id.edittext_direccion)
        editTextPhone = findViewById(R.id.edittext_phone)
        editTextPassword = findViewById(R.id.edittext_password)
        editTextConfirmPassword = findViewById(R.id.edittext_confirm_password)
        buttonRegister = findViewById(R.id.btn_register)

        /*
        *   FUNCION QUE LLAMA AL ACTIVITY DE REGRESO AL LOGIN
         */
        imageViewGoToLogin?.setOnClickListener {
            goToLogin()
        }
        /*
        *   REALIZAR EL REGISTRO DEL USUARIO
         */
        buttonRegister?.setOnClickListener { register() }
    }

    /*
    *   REGISTRO DE USUARIO
     */
    private fun register() {
        val name = editTextName?.text.toString()
        val cedula = editTextCedula?.text.toString()
        val adress = editTextDireccion?.text.toString()
        val lastname = editTextLastName?.text.toString()
        val email = editTextEmail?.text.toString()
        val phone = editTextPhone?.text.toString()
        val password = editTextPassword?.text.toString()
        val confirmPassword = editTextConfirmPassword?.text.toString()

        //Para lanzar la peticion
        var userProvider = UsersProvider_MCLB()

        if (isValidarForm(
                phone = phone,
                lastName = lastname,
                cedula = cedula,
                email = email,
                password = password,
                confirmPassword = confirmPassword,
                name = name
            )
        ) {
            // Toast.makeText(this, "El formulario  es valido", Toast.LENGTH_SHORT).show()
            //Para crear el usuario
            val user = User_MCLB(
                nombre = name,
                apellido = lastname,
                direccion = adress,
                email = email,
                telefono = phone,
                password = password
            )

            userProvider.register(user)?.enqueue(object : Callback<ResponseHttp_MCLB> {
                override fun onResponse( //Responde Bien
                    call: Call<ResponseHttp_MCLB>,
                    response: Response<ResponseHttp_MCLB>
                ) {
                    if (response.body()?.isSuccess == true) {
                        //Si el Registro es Exitoso guardamos las datos del Usuario y lo redirigimos a HomeClient
                        saveUserInSession(response.body()?.data.toString())

                        //Navega a la Activity de Home
                        goToClientHome()
                    }
                    Toast.makeText(
                        this@RegisterActivity_MCLB,
                        response.body()?.message,
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d(TAG, "Response Correcto : ${response}")
                    Log.d(TAG, "Response Cuerpo : ${response.body()}")


                }


                override fun onFailure(call: Call<ResponseHttp_MCLB>, t: Throwable) { //Cuando falla
                    Log.d(TAG, "Se Produjo un error en la peticion HTTP ${t.message}")
                    Toast.makeText(
                        this@RegisterActivity_MCLB,
                        "Se Produjo un error en la peticion HTTP 🙁 ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
        }
    }


    /*
    *  TODO Validar Celuda
     */
    private fun validarCedula(cedula: String): Boolean {
        if (cedula.length == 10) {
            //Obtenemos el digito de la region que sonlos dos primeros digitos
            var digito_region = cedula.substring(0, 2);
            ///TODO
            //Pregunto si la region existe ecuador se divide en 24 regiones
            if (digito_region.toInt() >= 1 && digito_region.toInt() <= 24) {

                // Extraigo el ultimo digito
                var ultimo_digito = cedula.substring(9, 10);

                //Agrupo todos los pares y los sumo
                var uno = cedula.substring(1, 2).toInt()
                var dos = cedula.substring(3, 4).toInt()
                var tres = cedula.substring(5, 6).toInt()
                var cuatro = cedula.substring(7, 8).toInt()
                //var cinco = cedula.substring(9, 10).toInt()
                var pares = uno + dos+ tres+ cuatro
//                var pares = cedula.substring(1, 2).toInt() + cedula.substring(3, 4).toInt()
//                + cedula.substring(5, 6).toInt() + cedula.substring(7, 8).toInt()

                //Agrupo los impares, los multiplico por un factor de 2, si la resultante es > que 9 le restamos el 9 a la resultante
                var numero1 = cedula.substring(0, 1)
                numero1 = (numero1.toInt() * 2).toString()
                if (numero1.toInt() > 9) {
                    numero1= (numero1.toInt() - 9).toString() }

                var numero3 = cedula.substring(2, 3)
                numero3 = (numero3.toInt() * 2).toString()
                if (numero3.toInt() > 9) {
                    numero3 = (numero3.toInt() - 9).toString() }

                var numero5 = cedula.substring(4, 5)
                numero5 = (numero5.toInt() * 2).toString()
                if (numero5.toInt() > 9) {
                    numero5 = (numero5.toInt() - 9).toString() }

                var numero7 = cedula.substring(6, 7)
                numero7 = (numero7.toInt() * 2).toString()
                if (numero7.toInt() > 9) {
                    numero7 = (numero7.toInt() - 9).toString() }

                var numero9 = cedula.substring(8, 9)
                numero9 = (numero9.toInt() * 2).toString()
                if (numero9.toInt() > 9) {
                    numero9 = (numero9.toInt() - 9).toString() }

                var impares = numero1.toInt() + numero3.toInt() + numero5.toInt() + numero7.toInt() + numero9.toInt()

                //Suma total
                var suma_total = (pares + impares)

                //extraemos el primero digito
                var primer_digito_suma = (suma_total).toString().substring(0, 1)

                //Obtenemos la decena inmediata
                var decena = (primer_digito_suma.toInt() + 1) * 10

                //Obtenemos la resta de la decena inmediata - la suma_total esto nos da el digito validador
                var digito_validador = decena - suma_total

                //Si el digito validador es = a 10 toma el valor de 0
                if (digito_validador == 10) {
                    digito_validador = 0
                }


                //Validamos que el digito validador sea igual al de la cedula
                if (digito_validador == ultimo_digito.toInt()) {

                    Toast.makeText(this, "La Cedula es Correcta 👍🏻👍🏻", Toast.LENGTH_SHORT)
                        .show()
                    return true
                } else {

                    Toast.makeText(this, "La Cedula es InCorrecta ⚠️⚠️", Toast.LENGTH_SHORT).show()
                    return false
                }
            } else {
                Toast.makeText(this, "La cedula no pertenerce a ninguna region ⚠️", Toast.LENGTH_SHORT)
                    .show()
                return false
            }


            //TODO

        } else {
            return false
        }
    }


    /*
    *   Almacenar el Session
    */
    private fun saveUserInSession(data: String) {
        Log.d("SharedPred", "Save : $data")
        val sharedPref = SharedPref_MCLB(this)
        val gson = Gson()
        val user = gson.fromJson(data, User_MCLB::class.java)
        Log.d("SharedPred", "Trasformacion : $user")
        sharedPref.save("user", user)

    }

    /*
    *   Navegar a la pantalla de Home si es la Autenticacion es Correcta
    */
    private fun goToClientHome() {
        //val i = Intent(this, ClientHomeActivity::class.java)
        val i = Intent(this, SaveImageActivity_MCLB::class.java)
        i.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Elimina el historial de Pantallas
        startActivity(i)
    }

    /*
    *   FUNCION PARA VALIDAR SI ES CORREO
    *   String.isEmailValid hace que aplique para todos campos de tipo String
     */
    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    /*
    *   VALIDACION DEL FORMULARIO
     */
    private fun isValidarForm(
        name: String,
        lastName: String,
        cedula: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (name.isBlank()) {
            Toast.makeText(this, "El Nombre esta En blanco", Toast.LENGTH_SHORT).show()
            return false
        }
        if (lastName.isBlank()) {
            Toast.makeText(this, "El Apellido esta En blanco", Toast.LENGTH_SHORT).show()
            return false
        }
        if (cedula.isBlank()) {
            Toast.makeText(this, "La cedula esta En blanco", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!validarCedula(cedula)) {
            //Toast.makeText(this, "Cedula Incorrecta", Toast.LENGTH_SHORT).show()
            return false
        }
        if (email.isBlank()) {
            Toast.makeText(this, "El Email vacio ", Toast.LENGTH_SHORT).show()
            return false
        }
        if (phone.isBlank()) {
            Toast.makeText(this, "El Telefono esta En blanco", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isBlank()) {
            Toast.makeText(this, "El Password Vacio", Toast.LENGTH_SHORT).show()
            return false
        }
        if (confirmPassword.isBlank()) {
            Toast.makeText(this, "El Verificacion Password esta En blanco", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        if (password != confirmPassword) {
            Toast.makeText(this, "El Password no coinscide verificalo", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        if (!validar_Password(password)) {
            Toast.makeText(this, "El password no cumple 1 mayuscula, 1 minuscula, 1numero, 1 caracter", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        if (!email.isEmailValid()) {
            Toast.makeText(this, "El Email no es Valido", Toast.LENGTH_SHORT).show()
            return false
        }


        return true
    }




    /*
    *   Validar Password
     */

    fun validar_Password(password: String): Boolean {
        var continuos = 0
        var numero = 0
        var especial = 0
        var fin = 0xFF.toChar()
        var minuscula = 0
        var mayuscula = 0
        var es_validar = true
        if (password.length < 6 || password.length > 10) return false
        for (i in 0 until password.length) {
            val c = password[i]
            if (c <= ' ' || c > '~') {
                es_validar = false
                break
            }
            if (c > ' ' && c < '0' || c >= ':' && c < 'A' || c >= '[' && c < 'a' || c >= '{' && c.toInt() < 127) {
                especial++
            }
            if (c >= '0' && c < ':') numero++
            if (c >= 'A' && c < '[') mayuscula++
            if (c >= 'a' && c < '{') minuscula++
            continuos = if (c == fin) continuos + 1 else 0
            if (continuos >= 7) {
                es_validar = false
                break
            }
            fin = c
        }
        es_validar = es_validar && especial > 0 && numero > 0 && minuscula > 0 && mayuscula > 0
        return es_validar
    }


    /*
    * Correo repetidos
     */
    fun verificarEmailBD(email: String){

    }
    /*
    * VOLVER A LA ACTIVITY DE LOGIN
     */
    private fun goToLogin() {
        val i = Intent(this, MainActivity_MCLB::class.java)
        startActivity(i)

    }
}