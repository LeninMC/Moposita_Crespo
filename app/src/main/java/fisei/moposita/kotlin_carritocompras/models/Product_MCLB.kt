package fisei.moposita.kotlin_carritocompras.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class Product_MCLB
    (
    @SerializedName("id") val id: String? = null,
    @SerializedName("nombre") val nombre: String? = null,
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("image1") val image1: String? = null,
    @SerializedName("image2") val image2: String? = null,
    @SerializedName("image3") val image3: String? = null,
    @SerializedName("precioUnitario") val precioUnitario: Double? = null,
    @SerializedName("costo") val costo: Double? = null,
    @SerializedName("stock") val stock: Int? = null,
    @SerializedName("idcategoria") val idcategoria: String,
    @SerializedName("quantity") var quantity:  Int? = 1,

    ) {


    /*
    * Transformar esta clase en un objeto JSON
     */

    fun toJson(): String {
        return Gson().toJson(this)
    }


    override fun toString(): String {
        return "Product(id=$id, nombre=$nombre, descripcion=$descripcion, image1=$image1, image2=$image2, image3=$image3, precioUnitario=$precioUnitario, costo=$costo, stock=$stock, idcategoria='$idcategoria', quantity='$quantity')"
    }
}