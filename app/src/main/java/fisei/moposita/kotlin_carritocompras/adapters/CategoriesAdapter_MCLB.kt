package fisei.moposita.kotlin_carritocompras.adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fisei.moposita.kotlin_carritocompras.R
import fisei.moposita.kotlin_carritocompras.activities.Client.products.list.ClientProductsListActivity_MCLB
import fisei.moposita.kotlin_carritocompras.models.Category_MCLB
import fisei.moposita.kotlin_carritocompras.utils.SharedPref_MCLB

class CategoriesAdapter_MCLB(val context: Activity, val categoryMCLBS: ArrayList<Category_MCLB>): RecyclerView.Adapter<CategoriesAdapter_MCLB.CategoriesViewHolder>() {

    val sharedPref = SharedPref_MCLB(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_categories, parent, false)
        return CategoriesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categoryMCLBS.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {

        val category = categoryMCLBS[position] // CADA UNA DE LAS CATEGORIAS

        holder.textViewCategory.text = category.nombre
        Glide.with(context).load(category.image).into(holder.imageViewCategory) //Establece la imagen

        holder.itemView.setOnClickListener{goToProducts(category)}
    }

    /*
    *   Una vez escogida la categoria le enviamos el id de la categoria para poder listar los productos segun la categoria
     */
    private  fun  goToProducts (categoryMCLB : Category_MCLB){
        val i = Intent(context, ClientProductsListActivity_MCLB::class.java)
        Log.d("AdapterCategoria", "$categoryMCLB")
        i.putExtra("idcategoria", categoryMCLB.idcategoria)
        context.startActivity(i)

    }


    class CategoriesViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textViewCategory: TextView
        val imageViewCategory: ImageView

        init {
            textViewCategory = view.findViewById(R.id.textview_category)
            imageViewCategory = view.findViewById(R.id.imageview_category)
        }

    }

}