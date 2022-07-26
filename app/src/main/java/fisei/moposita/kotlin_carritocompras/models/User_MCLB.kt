package fisei.moposita.kotlin_carritocompras.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class User_MCLB(
    @SerializedName("id") val id:String? = null,
    @SerializedName("nombre") var nombre:String,
    @SerializedName("apellido") var apellido:String,
    @SerializedName("cedula") var cedula:String? = null,
    @SerializedName("direccion") var direccion:String,
    @SerializedName("telefono") var telefono:String,
    @SerializedName("email") val email:String,
    @SerializedName("password") val password:String,
    @SerializedName("session_token") var session_token:String? = null,
    @SerializedName("image") var image:String? = null,
) {
    override fun toString(): String {
        return "User(id=$id, nombre='$nombre', apellido='$apellido', cedula=$cedula, direccion='$direccion', telefono='$telefono', email='$email', password='$password', session_token=$session_token, image=$image)"
    }

    /*
    * Transformar esta clase en un objeto JSON
     */

    fun toJson (): String{
        return  Gson().toJson(this)
    }
}