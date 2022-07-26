package fisei.moposita.kotlin_carritocompras.routes

import fisei.moposita.kotlin_carritocompras.models.ResponseHttp_MCLB
import fisei.moposita.kotlin_carritocompras.models.User_MCLB
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface UsersRoutes_MCLB {
    @POST("clientes/create")
    fun register(@Body userMCLB : User_MCLB): Call<ResponseHttp_MCLB>


    @FormUrlEncoded
    @POST("clientes/login")
    fun login (@Field("email") email : String , @Field("password") password : String)  : Call<ResponseHttp_MCLB>


    @Multipart //Para Actualizar data y subir Imagenes
    @PUT("clientes/update")
    fun update (
        @Part image: MultipartBody.Part,
        @Part("user") user: RequestBody,
        @Header("Authorization") token : String
    ): Call<ResponseHttp_MCLB>

    @PUT("clientes/updateWithoutImage")  //Para actualizar los datos sin la imagen
    fun updateWithoutImage (
        @Body userMCLB : User_MCLB,
        @Header("Authorization") token : String
    ): Call<ResponseHttp_MCLB>
}