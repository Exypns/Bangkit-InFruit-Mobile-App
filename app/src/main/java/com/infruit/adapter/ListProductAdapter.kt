package com.infruit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.infruit.R
import com.infruit.data.DummyProductData

class ListProductAdapter(private val listProduct: ArrayList<DummyProductData>) : RecyclerView.Adapter<ListProductAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = listProduct.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val (productTitle, shopTitle, productImage) = listProduct[position]

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}