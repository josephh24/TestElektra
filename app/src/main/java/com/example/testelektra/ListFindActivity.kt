package com.example.testelektra

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import kotlinx.android.synthetic.main.activity_list_find.*
import okhttp3.OkHttpClient

class ListFindActivity : AppCompatActivity() {

    private val urlBase = "https://stage.ektdevelopers.com/_graphql"
    private val tag = "ListFindActivity"
    private val mAdapter: MyAdapterBusqueda = MyAdapterBusqueda()
    private var mutableList: MutableList<itemProduct> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_find)

        val okHttpClient = OkHttpClient.Builder().build()

        val skus: MutableList<String> = mutableListOf()
        skus.addAll(resources.getStringArray(R.array.skus))

        val apolloClient = ApolloClient.builder()
            .serverUrl(urlBase)
            .okHttpClient(okHttpClient)
            .build()

        apolloClient.query(
            GetProductsQuery.Builder().skus(skus)
                .build()
        ).enqueue(object : ApolloCall.Callback<GetProductsQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                Log.e(tag, e.message)
            }

            override fun onResponse(response: Response<GetProductsQuery.Data>) {
                Log.d(tag, response.data()?.viewer?.products().toString())

                val time = response.data()!!.viewer!!.products()!!.size
                var i = 0
                while (i < time) {
                    val item = itemProduct()
                    item.id = response.data()!!.viewer!!.products!![i].id
                    item.name = response.data()!!.viewer!!.products!![i].name
                    item.image = response.data()!!.viewer!!.products!![i].image
                    item.price = response.data()!!.viewer!!.products!![i].price.toString()
                    mutableList.add(item)
                    i++
                }

                this@ListFindActivity.runOnUiThread(Runnable {
                    setUpRecyclerView()
                })
            }
        })
    }

    fun setUpRecyclerView() {
        rv_find.setHasFixedSize(true)
        rv_find.layoutManager = LinearLayoutManager(this)
        mAdapter.MyAdapterBusqueda(
            mutableList,
            this,
            object : MyAdapterBusqueda.AdapaterInterfazProduct {
                override fun tamanioBusqueda(tamanio: Int) {
                    if (tamanio == 0) {
                        rv_find.visibility = View.GONE
                    } else {
                        rv_find.visibility = View.VISIBLE
                    }
                }
            })

        busquedaText()

        rv_find.adapter = mAdapter

    }

    private fun busquedaText() {
        find.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mAdapter.buscarProduct()!!.filter(newText)
                return true
            }
        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ListFindActivity, MainActivity::class.java))
        finish()
    }
}

