package fisei.moposita.kotlin_carritocompras.providers

import fisei.moposita.kotlin_carritocompras.api.ApiRoutes_MCLB
import fisei.moposita.kotlin_carritocompras.models.ResponseHttp_MCLB
import fisei.moposita.kotlin_carritocompras.models.User_MCLB
import fisei.moposita.kotlin_carritocompras.routes.UsersRoutes_MCLB
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import java.io.File

class UsersProvider_MCLB(val token : String? = null) {

    private  var usersRoutesMCLB : UsersRoutes_MCLB? = null
    private  var usersRoutesMCLBToken : UsersRoutes_MCLB? = null

    init {
        val api = ApiRoutes_MCLB()
        usersRoutesMCLB = api.getUsersRoutes()
        if(token != null ){
            usersRoutesMCLBToken = api.getUsersRoutesWithToken(token!!)
        }

    }

    fun register (userMCLB : User_MCLB): Call<ResponseHttp_MCLB>?{
        return usersRoutesMCLB?.register(userMCLB)
    }

    fun login (email : String , password : String ): Call<ResponseHttp_MCLB>?{
        return usersRoutesMCLB?.login(email, password)
    }

    fun update (file : File, userMCLB : User_MCLB): Call<ResponseHttp_MCLB>?{
        val reqFile = RequestBody.create(MediaType.parse("image/*"), file )
        val image = MultipartBody.Part.createFormData("image", file.name, reqFile)
        val requestBody = RequestBody.create(MediaType.parse("text/plain"), userMCLB.toJson())

        return  usersRoutesMCLBToken?.update(image, requestBody, token!!)
    }

    fun updateWithoutImage (userMCLB : User_MCLB ): Call<ResponseHttp_MCLB>?{
        return usersRoutesMCLBToken?.updateWithoutImage(userMCLB, token!!)
    }
}