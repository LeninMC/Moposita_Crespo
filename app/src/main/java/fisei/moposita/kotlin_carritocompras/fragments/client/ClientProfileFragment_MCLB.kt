package fisei.moposita.kotlin_carritocompras.fragments.client

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import fisei.moposita.kotlin_carritocompras.R
import fisei.moposita.kotlin_carritocompras.activities.Client.update.ClientUpdateActivity_MCLB
import fisei.moposita.kotlin_carritocompras.activities.MainActivity_MCLB
import fisei.moposita.kotlin_carritocompras.models.User_MCLB
import fisei.moposita.kotlin_carritocompras.utils.SharedPref_MCLB

class ClientProfileFragment_MCLB : Fragment() {

    /*
    * TODO Variables y Constantes
     */
    var myView : View? = null
    var circleImageUser : CircleImageView? = null
    var buttonUpdateProfile : Button ? = null
    var textViewNombre : TextView? = null
    var textViewEmail : TextView? = null
    var textViewTelefono : TextView? = null
    var imageviewLogout : ImageView ? = null

    var sharedPrefMCLB : SharedPref_MCLB? = null
    var userMCLB : User_MCLB ? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       myView =  inflater.inflate(R.layout.fragment_client_profile, container, false)

        sharedPrefMCLB = SharedPref_MCLB(requireActivity())
        //TODO Instanciar
        buttonUpdateProfile = myView?.findViewById(R.id.btn_update_profile)
        circleImageUser = myView?.findViewById(R.id.circleimage_user)
        textViewNombre = myView?.findViewById(R.id.textview_nombre)
        textViewEmail = myView?.findViewById(R.id.textview_email)
        textViewTelefono = myView?.findViewById(R.id.textview_telefono)
        imageviewLogout = myView?.findViewById(R.id.imageview_logout)


        imageviewLogout?.setOnClickListener{logout()}
        buttonUpdateProfile?.setOnClickListener{goToUpdate()}

        getUserFromSession()

        //TODO: Hacemos referencia a la Data que se guarda en la SharedPref
        textViewNombre?.text = "${userMCLB?.nombre} ${userMCLB?.apellido}"
        textViewEmail?.text = userMCLB?.email
        textViewTelefono?.text = userMCLB?.telefono
        if(!userMCLB?.image.isNullOrBlank()){
            Glide.with(requireContext()).load(userMCLB?.image).into(circleImageUser!!)
        }




        return  myView
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

    /*
    *   TODO Redirigir a la activity de Update
     */
    private fun goToUpdate(){
        val i = Intent(requireContext(), ClientUpdateActivity_MCLB ::class.java)
        startActivity(i)
    }


    /*
   *   Para cerrar la session de usuario almacenada en ShardPref
    */
    private fun logout() {
        sharedPrefMCLB?.remove("user")
        val i = Intent(requireContext(), MainActivity_MCLB::class.java)
        startActivity(i)
    }


}