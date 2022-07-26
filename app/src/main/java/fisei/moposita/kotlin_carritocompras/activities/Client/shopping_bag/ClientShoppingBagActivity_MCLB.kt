package fisei.moposita.kotlin_carritocompras.activities.Client.shopping_bag

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fisei.moposita.kotlin_carritocompras.R
import fisei.moposita.kotlin_carritocompras.adapters.ShoppingBagAdapter_MCLB
import fisei.moposita.kotlin_carritocompras.models.Product_MCLB
import fisei.moposita.kotlin_carritocompras.utils.SharedPref_MCLB

class ClientShoppingBagActivity_MCLB : AppCompatActivity() {

    var recyclerViewShoppingBag : RecyclerView? = null
    var textViewTotal : TextView? = null
    var buttonNext : Button? = null
    var toolbar : Toolbar? = null

    var adapterMCLB : ShoppingBagAdapter_MCLB? = null
    var sharedPrefMCLB : SharedPref_MCLB? = null
    var gson = Gson()
    var selectedProducts = ArrayList<Product_MCLB>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_shooping_bag)

        sharedPrefMCLB = SharedPref_MCLB(this)
        recyclerViewShoppingBag = findViewById(R.id.recyclerview_shopping_bag)
        textViewTotal = findViewById(R.id.textview_total)
        buttonNext = findViewById(R.id.btn_aceptar)
        toolbar = findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        toolbar?.title = "Tu Order"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //Habilitar la Fecha de Regreso a atras



        recyclerViewShoppingBag?.layoutManager = LinearLayoutManager(this)


        getProductsFromSharedPref()
    }

    /*
    *
     */
    fun setTotal (total : Double){
        textViewTotal?.text = "$ ${total}"
    }



    /*
    *   TODO Obtener el sharedPref  los datos
     */
    private  fun getProductsFromSharedPref(){
        if(!sharedPrefMCLB?.getData("order").isNullOrBlank()){ //Si existe
            Log.d("Comprobacion", "${sharedPrefMCLB?.getData("order")}")
            val type  = object: TypeToken<ArrayList<Product_MCLB>>() {}.type //transfoma la lista JSON en una Array de Products
            selectedProducts  = gson.fromJson(sharedPrefMCLB?.getData("order"), type)

            adapterMCLB = ShoppingBagAdapter_MCLB(this, selectedProducts)
            recyclerViewShoppingBag?.adapter = adapterMCLB

        }
    }
}