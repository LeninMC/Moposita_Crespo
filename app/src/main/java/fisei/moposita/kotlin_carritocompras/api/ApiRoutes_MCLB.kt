package fisei.moposita.kotlin_carritocompras.api

import fisei.moposita.kotlin_carritocompras.routes.CategoriesRoutes_MCLB
import fisei.moposita.kotlin_carritocompras.routes.ProductsRoutes_MCLB
import fisei.moposita.kotlin_carritocompras.routes.UsersRoutes_MCLB

class ApiRoutes_MCLB {
    val API_URL = "https://apismoviles.herokuapp.com/api/"
    //Inicializar rutas
    val retrofit = RetrofitClient_MCLB()


    fun getUsersRoutes(): UsersRoutes_MCLB {
        return retrofit.getClient(API_URL).create(UsersRoutes_MCLB::class.java)
    }

    fun getUsersRoutesWithToken(token: String): UsersRoutes_MCLB {
        return retrofit.getClientWithToken(API_URL, token).create(UsersRoutes_MCLB::class.java)
    }


    fun getCategoriesRoutes(token : String) : CategoriesRoutes_MCLB{
        return  retrofit.getClientWithToken(API_URL, token).create(CategoriesRoutes_MCLB::class.java)
    }
    fun getProductsRoutes(token : String) : ProductsRoutes_MCLB{
        return  retrofit.getClientWithToken(API_URL, token).create(ProductsRoutes_MCLB::class.java)
    }
}