package fisei.moposita.kotlin_carritocompras.providers

import fisei.moposita.kotlin_carritocompras.api.ApiRoutes_MCLB
import fisei.moposita.kotlin_carritocompras.models.Category_MCLB
import fisei.moposita.kotlin_carritocompras.routes.CategoriesRoutes_MCLB
import retrofit2.Call

class CategoriesProvider_MCLB (val token : String) {
    private  var categoriesRoutesMCLB : CategoriesRoutes_MCLB? = null

    init {
        val api  =ApiRoutes_MCLB()
        categoriesRoutesMCLB = api.getCategoriesRoutes(token)
    }
    fun getAll () : Call<ArrayList<Category_MCLB>>?{
        return categoriesRoutesMCLB?.getAll(token)
    }
}