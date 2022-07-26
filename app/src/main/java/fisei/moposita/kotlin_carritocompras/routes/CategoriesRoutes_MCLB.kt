package fisei.moposita.kotlin_carritocompras.routes

import fisei.moposita.kotlin_carritocompras.models.Category_MCLB
import retrofit2.Call
import retrofit2.http.*

interface CategoriesRoutes_MCLB {

    @GET("categorias/getAll")
    fun getAll (
        @Header("Authorization") token : String
    ): Call<ArrayList<Category_MCLB>>
}