package fisei.moposita.kotlin_carritocompras.providers

import fisei.moposita.kotlin_carritocompras.api.ApiRoutes_MCLB
import fisei.moposita.kotlin_carritocompras.models.Product_MCLB
import fisei.moposita.kotlin_carritocompras.routes.ProductsRoutes_MCLB
import retrofit2.Call

class ProductsProvider_MCLB (val token : String) {
    private  var productsRoutesMCLB : ProductsRoutes_MCLB? = null

    init {
        val api  =ApiRoutes_MCLB()
        productsRoutesMCLB = api.getProductsRoutes(token)
    }
    fun findByCategory (idcategoria : String ) : Call<ArrayList<Product_MCLB>>?{
        return productsRoutesMCLB?.findByCategory(idcategoria, token)
    }
}