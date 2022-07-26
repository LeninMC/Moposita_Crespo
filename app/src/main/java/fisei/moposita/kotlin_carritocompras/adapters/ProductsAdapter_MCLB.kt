package fisei.moposita.kotlin_carritocompras.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fisei.moposita.kotlin_carritocompras.R
import fisei.moposita.kotlin_carritocompras.activities.Client.products.detail.ClientProductsDetailActivity_MCLB
import fisei.moposita.kotlin_carritocompras.models.Product_MCLB
import fisei.moposita.kotlin_carritocompras.utils.SharedPref_MCLB

class ProductsAdapter_MCLB(val context: Activity, val productMCLBS: ArrayList<Product_MCLB>): RecyclerView.Adapter<ProductsAdapter_MCLB.ProductsViewHolder>() {

    val sharedPref = SharedPref_MCLB(context)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_product, parent, false)
        return ProductsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productMCLBS.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {

        val product = productMCLBS[position] // CADA UNA DE LAS CATEGORIAS

        holder.textViewName.text = product.nombre
        holder.textViewPrice.text ="${product.precioUnitario}"
        Glide.with(context).load(product.image1).into(holder.imageViewProduct) //Establece la imagen


        holder.itemView.setOnClickListener{goToDetail(product)}
    }

    /*
    * Funcion para enviar al activity de detalle del producto
     */
    private fun goToDetail(productMCLB : Product_MCLB){

        val i = Intent(context, ClientProductsDetailActivity_MCLB::class.java)
        i.putExtra("product", productMCLB.toJson())
        context.startActivity(i)

    }



    class ProductsViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textViewName: TextView
        val textViewPrice: TextView
        val imageViewProduct: ImageView

        init {
            textViewName  = view.findViewById(R.id.textview_productnombre)
            textViewPrice  = view.findViewById(R.id.textview_productprecio)
            imageViewProduct = view.findViewById(R.id.imageview_product)
        }

    }

}