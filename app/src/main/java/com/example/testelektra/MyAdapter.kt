package com.example.testelektra

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyAdapter : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    private var lista : MutableList<itemProduct>  = ArrayList()
    lateinit var context:Context

    fun MyAdapter(lista : MutableList<itemProduct>, context: Context){
        this.lista = lista
        this.context = context
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.bind(item, context, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.my_adapter, parent, false))
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById(R.id.name) as TextView
        val price = view.findViewById(R.id.price) as TextView
        val img = view.findViewById(R.id.img) as ImageView

        fun bind(data : itemProduct , context: Context, pos : Int){
            name.text = data.name
            price.text = data.price
            img.loadUrl(data.image.toString())
            itemView.setOnClickListener{

                Toast.makeText(context, data.name, Toast.LENGTH_SHORT).show() }
        }
        private fun ImageView.loadUrl(url: String) {
            Glide.with(context).load(url).into(this)
        }
    }
}