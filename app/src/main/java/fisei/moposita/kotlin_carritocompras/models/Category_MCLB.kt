package fisei.moposita.kotlin_carritocompras.models

import com.google.gson.Gson

class Category_MCLB(
    val idcategoria: String? = null,
    val nombre: String,
    val descripcion: String,
    val image: String? = null
) {
    override fun toString(): String {
        return "Category(id='$idcategoria', nombre='$nombre', descripcion='$descripcion', image='$image')"
    }
    /*
    * Transformar esta clase en un objeto JSON
     */

    fun toJson (): String{
        return  Gson().toJson(this)
    }
}