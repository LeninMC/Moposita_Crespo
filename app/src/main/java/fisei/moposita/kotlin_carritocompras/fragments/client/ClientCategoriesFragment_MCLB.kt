package fisei.moposita.kotlin_carritocompras.fragments.client

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import fisei.moposita.kotlin_carritocompras.R
import fisei.moposita.kotlin_carritocompras.activities.Client.shopping_bag.ClientShoppingBagActivity_MCLB
import fisei.moposita.kotlin_carritocompras.adapters.CategoriesAdapter_MCLB
import fisei.moposita.kotlin_carritocompras.models.Category_MCLB
import fisei.moposita.kotlin_carritocompras.models.User_MCLB
import fisei.moposita.kotlin_carritocompras.providers.CategoriesProvider_MCLB
import fisei.moposita.kotlin_carritocompras.utils.SharedPref_MCLB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ClientCategoriesFragment_MCLB : Fragment() {


    val TAG = "CategoriesFragment"
    var myView: View? = null
    var recyclerViewCategories: RecyclerView? = null
    var categoriesProviderMCLB: CategoriesProvider_MCLB? = null
    var adapter: CategoriesAdapter_MCLB? = null
    var userMCLB: User_MCLB? = null
    var sharedPrefMCLB: SharedPref_MCLB? = null
    var categories = ArrayList<Category_MCLB>()
    var toolbar : Toolbar? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        myView = inflater.inflate(R.layout.fragment_client_categories, container, false)

        setHasOptionsMenu(true) //Habilita las opcciones de menu para poder posicionar el icon bag

        toolbar = myView?.findViewById(R.id.toolbar)
        toolbar?.setTitleTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
        toolbar?.title = "Categorias"
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        recyclerViewCategories = myView?.findViewById(R.id.recyclerview_categories)
        recyclerViewCategories?.layoutManager = LinearLayoutManager(requireContext()) //Que los elemtos se mostraran uno debajo del otro
        sharedPrefMCLB = SharedPref_MCLB(requireActivity())
        getUserFromSession()
        categoriesProviderMCLB = CategoriesProvider_MCLB(userMCLB?.session_token!!);
        getCategories()

        return myView
    }

    /*
    *   Instanciar el menu_shppping bag
     */

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_shopping_bag , menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    /*
    * para darle funcionalidad al boton con el icono de bag
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.item_shopping_bag){
            goToShoppingBagList()
        }
        return super.onOptionsItemSelected(item)
    }
     /*
     * NAVEGAR AL ACTIVITY
      */

    private fun goToShoppingBagList(){
        val i = Intent(requireContext(), ClientShoppingBagActivity_MCLB::class.java)
        startActivity(i)
    }


    /*
    *
     */
    private fun getCategories(){
        categoriesProviderMCLB?.getAll()?.enqueue(object : Callback<ArrayList<Category_MCLB>>{
            override fun onResponse(
                call: Call<ArrayList<Category_MCLB>>,
                response: Response<ArrayList<Category_MCLB>>
            ) {
              if(response.body() != null){
                  categories = response.body()!!
                  adapter = CategoriesAdapter_MCLB(requireActivity(), categories)
                  recyclerViewCategories?.adapter = adapter
              }
            }

            override fun onFailure(call: Call<ArrayList<Category_MCLB>>, t: Throwable) {
                Log.d(TAG, "Error en la Peticion Data Categorias : ${t.message}")
                Toast.makeText(requireContext(), "Error en la Peticion Data Categorias : ${t.message}", Toast.LENGTH_LONG).show()
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