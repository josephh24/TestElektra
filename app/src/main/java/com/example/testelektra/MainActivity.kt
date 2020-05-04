package com.example.testelektra

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient


class MainActivity : AppCompatActivity(), View.OnClickListener{

    private val urlBase = "https://stage.ektdevelopers.com/_graphql"
    private val tag = "MainActivity"
    private val mAdapter: MyAdapter = MyAdapter()
    private var mutableList: MutableList<itemProduct> = mutableListOf()

    private val TIME_INTERVAL = 2000
    private var mBackPressed: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Glide.with(this).load("https://hnoscarrasco.com/images/hotsale.jpg").centerCrop()
            .into(banner1)
        Glide.with(this).load("https://hnoscarrasco.com/images/cekt.jpg").centerCrop().into(banner2)
        Glide.with(this).load("https://hnoscarrasco.com/images/Imagen%20producto6.jpg").centerCrop()
            .into(banner3)
        Glide.with(this).load("https://hnoscarrasco.com/images/Imagen%20producto5.jpg").centerCrop()
            .into(banner4)
        Glide.with(this).load("https://hnoscarrasco.com/images/Imagen%20producto4.jpg").centerCrop()
            .into(banner5)

        banner1.setOnClickListener(this)
        banner2.setOnClickListener(this)
        banner3.setOnClickListener(this)
        banner4.setOnClickListener(this)
        banner5.setOnClickListener(this)

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

                this@MainActivity.runOnUiThread(Runnable {
                    setUpRecyclerView1()
                    setUpRecyclerView2()
                })
            }
        })
    }

    fun setUpRecyclerView1() {
        rv_test.setHasFixedSize(true)
        rv_test.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mAdapter.MyAdapter(mutableList, this)
        rv_test.adapter = mAdapter
    }

    fun setUpRecyclerView2() {
        rv_test2.setHasFixedSize(true)
        rv_test2.layoutManager = GridLayoutManager(this, 2)
        mAdapter.MyAdapter(mutableList, this)
        rv_test2.adapter = mAdapter
    }

    override fun onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed()
            return
        } else {
            Toast.makeText(baseContext, "Para salir presione de nuevo", Toast.LENGTH_SHORT)
                .show()
        }
        mBackPressed = System.currentTimeMillis()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.banner1 -> irPantalla()

            R.id.banner2 -> irPantalla()

            R.id.banner3 -> irPantalla()

            R.id.banner4 -> irPantalla()

            R.id.banner5 -> irPantalla()
        }
    }

    private fun irPantalla() {
        startActivity(Intent(this@MainActivity, ListFindActivity::class.java))
        finish()
    }
}
