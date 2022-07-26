package fisei.moposita.kotlin_carritocompras.routes

import fisei.moposita.kotlin_carritocompras.models.Product_MCLB
import retrofit2.Call
import retrofit2.http.*

interface ProductsRoutes_MCLB {

    @GET("products/getAll")
    fun getAll (
        @Header("Authorization") token : String
    ): Call<ArrayList<Product_MCLB>>


    //TODO solo por categoria la peticion
    @GET("products/findByCategory/{idcategoria}")
    fun findByCategory (
        @Path("idcategoria") idcategoria : String,
        @Header("Authorization") token : String
    ): Call<ArrayList<Product_MCLB>>

}
