package com.example.testelektra

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyAdapterBusqueda : RecyclerView.Adapter<MyAdapterBusqueda.ViewHolder>() {

    private var listData : MutableList<itemProduct>  = ArrayList()
    private var listFull : MutableList<itemProduct>  = ArrayList()
    lateinit var context:Context
    private lateinit var myAdapterBusqueda : AdapaterInterfazProduct

    fun MyAdapterBusqueda(listData : MutableList<itemProduct>, context: Context, myAdapterBusqueda : AdapaterInterfazProduct){
        this.listData = listData
        this.listFull = java.util.ArrayList<itemProduct>(listData)
        this.context = context
        this.myAdapterBusqueda = myAdapterBusqueda
    }

    fun buscarProduct(): Filter? {
        return retornoBusqueda
    }

    private val retornoBusqueda: Filter = object : Filter() {
        override fun performFiltering(peticion: CharSequence): FilterResults {

            //se instancia un arreglo con la lista de datos
            val listaFiltrada: MutableList<itemProduct> =
                java.util.ArrayList<itemProduct>()
            if (peticion == null || peticion.length == 0) { // se valida si no hay texto en el searchview y llena la lista completa
                listaFiltrada.addAll(listFull)
            } else { //se obtiene el valor que el Usuario escribe en el campo de searchview
                val valor = peticion.toString().toLowerCase().trim { it <= ' ' }
                for (datos in listFull) { // se agregan los registros que concidan con la ciudad escrita a el arreglo
                    if (datos.name!!.toLowerCase().contains(valor)) {
                        listaFiltrada.add(datos)
                    }
                }
            }

            // se envia una respuesta con los resultados del filtro
            val filterResults = FilterResults()
            filterResults.values = listaFiltrada
            filterResults.count = listaFiltrada.size
            return filterResults
        }

        override fun publishResults(
            charSequence: CharSequence,
            filterResults: FilterResults
        ) {
            myAdapterBusqueda.tamanioBusqueda(filterResults.count)
            listData.clear() // se vacia la lista primero
            listData.addAll(filterResults.values as Collection<itemProduct>) //se llena la lista de terminales con el arreglo de datos filtrados
            notifyDataSetChanged() // verifica si hay cambios
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listData[position]
        holder.bind(item, context, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.my_adapter_busqueda, parent, false))
    }

    interface AdapaterInterfazProduct {
        fun tamanioBusqueda(tamanio: Int)
    }

    override fun getItemCount(): Int {
        return listData.size
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