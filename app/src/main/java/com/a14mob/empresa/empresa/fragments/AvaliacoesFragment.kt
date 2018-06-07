package com.a14mob.empresa.empresa.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast


import com.a14mob.empresa.empresa.R
import com.a14mob.empresa.empresa.adapter.AvaliacaoAdapter
import com.a14mob.empresa.empresa.adapter.ScoreAdapter
import com.a14mob.empresa.empresa.entity.Avaliacao
import com.a14mob.empresa.empresa.entity.Score
import com.a14mob.empresa.empresa.retrofit.RetroFitRestAPI
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import com.hendraanggrian.pikasso.picasso
import kotlinx.android.synthetic.main.fragment_avaliacoes.*
import kotlinx.android.synthetic.main.fragment_score.*
import kotlinx.android.synthetic.main.score_item.view.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * A simple [Fragment] subclass.
 */
class AvaliacoesFragment : Fragment() {
    var okHttpClient = OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()


    var gson = GsonBuilder()
            .setLenient()

            .create()

    val retrofit = Retrofit.Builder()
            .baseUrl("http://api.14mob.com")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))

            .build()

    val api = retrofit.create<RetroFitRestAPI>(RetroFitRestAPI::class.java!!)

    var nome: String = ""

    var profissionalId: Int = 0

    var meta: Int = 0
    var foto: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        val prefs = inflater.context.getSharedPreferences("PROFISSIONAL", Context.MODE_PRIVATE)

        profissionalId = prefs.getString("profissionalId", null).toInt()

        meta = prefs.getString("meta", null).toInt()

        nome = prefs.getString("nome", null).toString()

        foto = prefs.getString("foto", null).toString()




        return inflater?.inflate(R.layout.fragment_avaliacoes, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        pontosProfissional.text = nome.toString()

        picasso.load(foto.toString()).into(imgProfissional)

        score(profissionalId.toInt(), meta.toInt())



    }

    fun carregarInformacoes(avaliacoes: List<Avaliacao>){





        val recyclerView = avaliacoes_list_recyclerview

        recyclerView.adapter = AvaliacaoAdapter(avaliacoes, this@AvaliacoesFragment.context!!)

        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager


    }

    fun score(profissionalId: Int, meta:Int){



        api.scoreProfissionalAvaliacao(profissionalId,meta)
                .enqueue(object : Callback<List<Avaliacao>> {
                    override fun onResponse(call: Call<List<Avaliacao>>?, response: Response<List<Avaliacao>>?) {

                        this@AvaliacoesFragment.carregarInformacoes(response?.body() as List<Avaliacao>)



                    }

                    override fun onFailure(call: Call<List<Avaliacao>>?, t: Throwable?) {


                        Log.i("ERROR>>>>>>>",t.toString())


                    }
                })

    }

}// Required empty public constructor
