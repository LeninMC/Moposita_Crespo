package fisei.moposita.kotlin_carritocompras.activities.Client.products.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import fisei.moposita.kotlin_carritocompras.R
import fisei.moposita.kotlin_carritocompras.adapters.ProductsAdapter_MCLB
import fisei.moposita.kotlin_carritocompras.models.Product_MCLB
import fisei.moposita.kotlin_carritocompras.models.User_MCLB
import fisei.moposita.kotlin_carritocompras.providers.ProductsProvider_MCLB
import fisei.moposita.kotlin_carritocompras.utils.SharedPref_MCLB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientProductsListActivity_MCLB : AppCompatActivity() {

    val TAG = "ProductsListActivity"
    var recyclerViewProducts : RecyclerView? = null
    var adapterMCLB : ProductsAdapter_MCLB? = null
    var userMCLB : User_MCLB? =null
    var sharedPrefMCLB : SharedPref_MCLB ? = null
    var productsProviderMCLB : ProductsProvider_MCLB? =null

    //TODO Recibir los datos de la activity padre
    var idCategory : String  ? = null


    var productMCLBS : ArrayList<Product_MCLB> = ArrayList()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_list)

        sharedPrefMCLB = SharedPref_MCLB(this)
        //TODO Recibir los datos de la activity padre
        idCategory = intent.getStringExtra("idcategoria")
        getUserFromSession()
        productsProviderMCLB = ProductsProvider_MCLB(userMCLB?.session_token!!)

        recyclerViewProducts = findViewById(R.id.recyclerview_products)
        recyclerViewProducts?.layoutManager = GridLayoutManager(this, 2)//este forma la pantalla en grid de columnas de 2
        getProducts()

    }

    private fun getProducts (){
        Log.d(TAG, "error IDcATEGORIA : $idCategory")
        productsProviderMCLB?.findByCategory(idCategory!!)?.enqueue(object : Callback<ArrayList<Product_MCLB>>{
            override fun onResponse(
                call: Call<ArrayList<Product_MCLB>>,
                response: Response<ArrayList<Product_MCLB>>
            ) {
               if(response.body() != null){
                   productMCLBS = response.body()!!
                   adapterMCLB = ProductsAdapter_MCLB(this@ClientProductsListActivity_MCLB, productMCLBS)
                   recyclerViewProducts?.adapter = adapterMCLB
                   Log.d(TAG, "Peticion realizada con exito : ${response.body()}")
               }
            }

            override fun onFailure(call: Call<ArrayList<Product_MCLB>>, t: Throwable) {
                Toast.makeText(this@ClientProductsListActivity_MCLB, "${t.message}", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "error : ${t.message}")
            }

        })
    }

    /*
*  TODO Obtener la data almacena de Session de SharedPref
*/
    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPrefMCLB?.getData("user").isNullOrBlank()) {
            //Si el usuario Existe en Session
            userMCLB = gson.fromJson(sharedPrefMCLB?.getData("user"), User_MCLB::class.java)

        }
    }
}