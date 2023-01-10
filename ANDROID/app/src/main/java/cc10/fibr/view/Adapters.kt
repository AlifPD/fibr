package cc10.fibr.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cc10.fibr.R
import cc10.fibr.network.AllMerchantResponseItem
import cc10.fibr.network.AllProductResponseItem
import cc10.fibr.network.ReadCartResponseItem
import com.bumptech.glide.Glide

class CartAdapter(private val listCart: List<ReadCartResponseItem?>?) : RecyclerView.Adapter<CartAdapter.ViewHolder>(){
    private lateinit var onItemClickCallback: OnItemClickCallback
    private val urlThumbnail = "https://storage.googleapis.com/fibr-3ac5b.appspot.com/Merchants/Products/"


    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgvThumbnail: ImageView = itemView.findViewById(R.id.cartitem_imv_thumbnail)
        val tvName: TextView = itemView.findViewById(R.id.cartitem_tv_name)
        val tvPrice: TextView = itemView.findViewById(R.id.cartitem_tv_price)
        val tvQuantity: TextView = itemView.findViewById(R.id.cartitem_tv_quantity)
        val tvTotal: TextView = itemView.findViewById(R.id.cartitem_tv_total)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val totalShow = listCart?.get(position)?.quantity?.let {
            listCart[position]?.price?.times(it)
        }

        Glide.with(holder.itemView.context)
            .load(urlThumbnail + (listCart?.get(position)?.thumbnail))
            .into(holder.imgvThumbnail)
        holder.tvName.text = listCart?.get(position)?.name
        holder.tvPrice.text = listCart?.get(position)?.price.toString() + " / " + listCart?.get(position)?.unit.toString()
        holder.tvQuantity.text = listCart?.get(position)?.quantity.toString()
        holder.tvTotal.text = totalShow.toString()

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listCart?.get(holder.adapterPosition))
        }
    }

    override fun getItemCount(): Int = listCart?.size ?:0

    interface OnItemClickCallback{
        fun onItemClicked(data: ReadCartResponseItem?)
    }
}

class MerchantsAdapter(private val listMerchants: List<AllMerchantResponseItem?>?) : RecyclerView.Adapter<MerchantsAdapter.ViewHolder>(){
    private lateinit var onItemClickCallback: OnItemClickCallback
    private val urlThumbnail = "https://storage.googleapis.com/fibr-3ac5b.appspot.com/Merchants/Thumbnails/"


    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgvThumbnail: ImageView = itemView.findViewById(R.id.merchantitem_imv_thumbnail)
        val tvName: TextView = itemView.findViewById(R.id.merchantitem_tv_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_merchants, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(urlThumbnail + (listMerchants?.get(position)?.thumbnail))
            .into(holder.imgvThumbnail)
        holder.tvName.text = listMerchants?.get(position)?.name

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listMerchants?.get(holder.adapterPosition))
        }
    }

    override fun getItemCount(): Int = listMerchants?.size ?:0

    interface OnItemClickCallback{
        fun onItemClicked(data: AllMerchantResponseItem?)
    }

}

class ProductAdapter(private val listProducts: List<AllProductResponseItem?>?) : RecyclerView.Adapter<ProductAdapter.ViewHolder>(){
    private lateinit var onItemClickCallback: OnItemClickCallback
    private val urlThumbnail = "https://storage.googleapis.com/fibr-3ac5b.appspot.com/Merchants/Products/"


    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgvThumbnail: ImageView = itemView.findViewById(R.id.productitem_imv_thumbnail)
        val tvName: TextView = itemView.findViewById(R.id.productitem_tv_name)
        val tvPrice: TextView = itemView.findViewById(R.id.productitem_tv_price)
        val tvQuantity: TextView = itemView.findViewById(R.id.productitem_tv_quantity)
        val tvDescription: TextView = itemView.findViewById(R.id.productitem_tv_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_products, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(urlThumbnail+(listProducts?.get(position)?.thumbnail))
            .into(holder.imgvThumbnail)
        holder.tvName.text = listProducts?.get(position)?.name
        holder.tvPrice.text = listProducts?.get(position)?.price.toString()
        holder.tvQuantity.text = listProducts?.get(position)?.quantity.toString()
        holder.tvDescription.text = listProducts?.get(position)?.description

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listProducts?.get(holder.adapterPosition))
        }
    }

    override fun getItemCount(): Int = listProducts?.size ?:0

    interface OnItemClickCallback{
        fun onItemClicked(data: AllProductResponseItem?)
    }

}