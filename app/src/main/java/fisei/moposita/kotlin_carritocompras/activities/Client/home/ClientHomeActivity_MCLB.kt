package fisei.moposita.kotlin_carritocompras.activities.Client.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import fisei.moposita.kotlin_carritocompras.R
import fisei.moposita.kotlin_carritocompras.activities.MainActivity_MCLB
import fisei.moposita.kotlin_carritocompras.fragments.client.ClientCategoriesFragment_MCLB
import fisei.moposita.kotlin_carritocompras.fragments.client.ClientOrdersFragment
import fisei.moposita.kotlin_carritocompras.fragments.client.ClientProfileFragment_MCLB
import fisei.moposita.kotlin_carritocompras.models.User_MCLB
import fisei.moposita.kotlin_carritocompras.utils.SharedPref_MCLB

class ClientHomeActivity_MCLB : AppCompatActivity() {

    private val TAG = "ClientHomeActivity"
    var buttonLogout: Button? = null
    var sharedPrefMCLB: SharedPref_MCLB? = null
    var bottomNavigation: BottomNavigationView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_client_home)
        //buttonLogout = findViewById(R.id.btn_logout)
        bottomNavigation = findViewById(R.id.bottom_navegation)
        sharedPrefMCLB = SharedPref_MCLB(this)
        openFragment(ClientCategoriesFragment_MCLB()) //Abre un fragmento por defecto
        //cerrar Session
        //buttonLogout?.setOnClickListener { logout() }
        bottomNavigation?.setOnItemSelectedListener {
            when (it.itemId) { //Equivalente al Switch
                R.id.item_home -> {
                    openFragment(ClientCategoriesFragment_MCLB())
                    true
                }
                R.id.item_orders -> {
                    openFragment(ClientOrdersFragment())
                    true
                }
                R.id.item_profile -> {
                    openFragment(ClientProfileFragment_MCLB())
                    true
                }
                else -> false

            }
        }

        getUserFromSession()
    }

    /*
    *   Funcion para mostrar el fragmento segun lo seleccionado
     */
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    /*
    *   Para cerrar la session de usuario almacenada en ShardPref
     */
    private fun logout() {
        sharedPrefMCLB?.remove("user")
        val i = Intent(this, MainActivity_MCLB::class.java)
        startActivity(i)
    }

    /*
    *   Obtener la data almacena de Session de SharedPref
     */
    private fun getUserFromSession() {
        val gson = Gson()
        if (!sharedPrefMCLB?.getData("user").isNullOrBlank()) {
            //Si el usuario Existe en Session
            val user = gson.fromJson(sharedPrefMCLB?.getData("user"), User_MCLB::class.java)
            Log.d(TAG, "Usuario : $user")
        }
    }
}