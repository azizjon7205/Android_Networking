package com.example.android_networking.activity

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.*
import com.example.android_networking.R
import com.example.android_networking.adapter.PosterAdapter
import com.example.android_networking.adapter.PosterRespAdapter
import com.example.android_networking.model.Poster
import com.example.android_networking.model.PosterResp
import com.example.android_networking.network.retrofit.RetrofitHttp
import com.example.android_networking.network.volley.VolleyHandler
import com.example.android_networking.network.volley.VolleyHttp
import com.example.android_networking.utils.Alerts
import com.example.android_networking.utils.Logger
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var posterAdapter: PosterAdapter
    private lateinit var posterRespAdapter: PosterRespAdapter
    val posters = ArrayList<Poster>()
    val posterResps = ArrayList<PosterResp>()

    private lateinit var fr_loading: FrameLayout
    private lateinit var pr_loading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

    }

    private fun initViews() {
        fr_loading = findViewById(R.id.fm_loading)
        pr_loading = findViewById(R.id.progress_loading)

        recyclerView = findViewById(R.id.rv_main)
        recyclerView.layoutManager = GridLayoutManager(this, 1)


        val b_floating: FloatingActionButton = findViewById(R.id.b_floating)
        b_floating.setOnClickListener {
            Alerts.createrDialog(this, null) {
                apiPosterCreateByRetrofit(it)
            }
        }

        apiPosterListByRetrofit()

    }

    fun refreshAdapter(posters: ArrayList<Poster>) {
        posterAdapter = PosterAdapter(this, posters)
        recyclerView.adapter = posterAdapter

    }

    fun refreshRespAdapter(posters: ArrayList<PosterResp>) {
        posterRespAdapter = PosterRespAdapter(this, posters, {
            apiPosterDeleteByRetrofit(it)
            posterRespAdapter.notifyDataSetChanged()
        },{posterResps,  position ->
            apiPosterUpdateByRetrofit(posterResps, position)
            posterRespAdapter.notifyDataSetChanged()
        })
        recyclerView.adapter = posterRespAdapter
    }

    // with retrofit
    fun apiPosterListByRetrofit() {
        fr_loading.visibility = View.VISIBLE
        RetrofitHttp.posterService.listPost().enqueue(object : Callback<ArrayList<PosterResp>> {
            override fun onResponse(
                call: Call<ArrayList<PosterResp>>,
                response: Response<ArrayList<PosterResp>>,
            ) {
                fr_loading.visibility = View.INVISIBLE
                response.body()?.let { refreshRespAdapter(it) }
                Logger.d("@@@", response.body().toString())
            }

            override fun onFailure(call: Call<ArrayList<PosterResp>>, t: Throwable) {
                Logger.d("@@@", t.message.toString())
                fr_loading.visibility = View.INVISIBLE
            }

        })
    }

    fun apiPosterCreateByRetrofit(posterResp: PosterResp) {
        fr_loading.visibility = View.VISIBLE
        RetrofitHttp.posterService.createPost(posterResp).enqueue(object : Callback<PosterResp> {
            override fun onResponse(call: Call<PosterResp>, response: Response<PosterResp>) {
                fr_loading.visibility = View.INVISIBLE
                posterRespAdapter.items.add(response.body()!!)
                posterRespAdapter.notifyDataSetChanged()
                Log.d("@@@", "Create -> ${response.body().toString()}")

            }

            override fun onFailure(call: Call<PosterResp>, t: Throwable) {
                fr_loading.visibility = View.INVISIBLE
                Log.d("@@@", t.localizedMessage!!)
            }

        })
    }

    fun apiPosterDeleteByRetrofit(posterResp: PosterResp) {
        fr_loading.visibility = View.VISIBLE
        RetrofitHttp.posterService.deletePost(posterResp.id).enqueue(object : Callback<PosterResp> {
            override fun onResponse(call: Call<PosterResp>, response: Response<PosterResp>) {
                fr_loading.visibility = View.INVISIBLE
                posterRespAdapter.items.remove(posterResp)
                posterRespAdapter.notifyDataSetChanged()
                Log.d("@@@", "Delete -> ${response.body().toString()}")
            }

            override fun onFailure(call: Call<PosterResp>, t: Throwable) {
                fr_loading.visibility = View.INVISIBLE
                Log.d("@@@", t.localizedMessage!!)
            }

        })
    }

    fun apiPosterUpdateByRetrofit(posterResp: PosterResp, position: Int) {
        fr_loading.visibility = View.VISIBLE
        RetrofitHttp.posterService.updatePost(posterResp.id, posterResp).enqueue(object : Callback<PosterResp> {
            override fun onResponse(call: Call<PosterResp>, response: Response<PosterResp>) {
                fr_loading.visibility = View.INVISIBLE
                posterRespAdapter.items[position] = posterResp
                posterRespAdapter.notifyDataSetChanged()
                Log.d("@@@", "Update -> ${response.body().toString()}")
            }

            override fun onFailure(call: Call<PosterResp>, t: Throwable) {
                fr_loading.visibility = View.INVISIBLE
                Log.d("@@@", t.localizedMessage!!)
            }

        })
    }

//----------------------------------------------------------------------------------------------------------

    // with volley
    fun apiPosterList() {
        fr_loading.visibility = View.VISIBLE
        VolleyHttp.get(VolleyHttp.API_LIST_POST, VolleyHttp.paramsEmpty(), object : VolleyHandler {
            override fun onSuccess(response: String?) {
                fr_loading.visibility = View.INVISIBLE
                val postArray = Gson().fromJson(response, Array<Poster>::class.java)
                posters.clear()
                posters.addAll(postArray)
                refreshAdapter(posters)
            }

            override fun onError(error: String?) {
                fr_loading.visibility = View.INVISIBLE
                Log.d("RRR", error!!)
            }
        })
    }

    fun apiPosterDelete(poster: Poster) {
        fr_loading.visibility = View.VISIBLE
        VolleyHttp.del(VolleyHttp.API_DELETE_POST + poster.id, object : VolleyHandler {
            override fun onSuccess(response: String?) {
                fr_loading.visibility = View.INVISIBLE
                Log.d("RRR", response!!)
                posterAdapter.items.remove(poster)
                posterAdapter.notifyDataSetChanged()
//                apiPosterList()
            }

            override fun onError(error: String?) {
                fr_loading.visibility = View.INVISIBLE
                Logger.d("RRR", error!!)
            }

        })
    }

    fun apiPosterCreate(poster: Poster) {
        VolleyHttp.post(VolleyHttp.API_CREATE_POST,
            VolleyHttp.paramsCreate(poster),
            object : VolleyHandler {
                override fun onSuccess(response: String?) {
                    //posters.add(poster)
                    posterAdapter.items.add(poster)
                    posterAdapter.notifyDataSetChanged()
                    Logger.d("RRR", "-> ${posters.size}")
                }

                override fun onError(error: String?) {
                    Logger.d("RRR", error!!)
                }

            })
    }

    fun workWithRetrofit() {

        RetrofitHttp.posterService.listPost().enqueue(object : Callback<ArrayList<PosterResp>> {
            override fun onResponse(
                call: Call<ArrayList<PosterResp>>,
                response: Response<ArrayList<PosterResp>>,
            ) {
                Logger.d("@@@", response.body().toString())
            }

            override fun onFailure(call: Call<ArrayList<PosterResp>>, t: Throwable) {
                Logger.d("@@@", t.message.toString())
            }

        })

        val poster = Poster(1, 1, "PDP", "Online")

//        RetrofitHttp.posterService.createPost(poster).enqueue(object : Callback<PosterResp> {
//            override fun onResponse(call: Call<PosterResp>, response: Response<PosterResp>) {
//                Logger.d("@@@", response.body().toString())
//            }
//
//            override fun onFailure(call: Call<PosterResp>, t: Throwable) {
//                Logger.d("@@@", t.message.toString())
//            }
//        })

//        RetrofitHttp.posterService.updatePost(poster.id, poster)
//            .enqueue(object : Callback<PosterResp> {
//                override fun onResponse(call: Call<PosterResp>, response: Response<PosterResp>) {
//                    Logger.d("@@@", response.body().toString())
//                }
//
//                override fun onFailure(call: Call<PosterResp>, t: Throwable) {
//                    Logger.d("@@@", t.message.toString())
//                }
//            })

        RetrofitHttp.posterService.deletePost(poster.id).enqueue(object : Callback<PosterResp> {
            override fun onResponse(call: Call<PosterResp>, response: Response<PosterResp>) {
                Logger.d("@@@", "" + response.body())
            }

            override fun onFailure(call: Call<PosterResp>, t: Throwable) {
                Logger.d("@@@", "" + t.message)
            }
        })
    }

    fun workWithVolley() {

        val poster = Poster(1, 1, "PDP", "Online")

        // getting all list
        VolleyHttp.get(VolleyHttp.API_LIST_POST, VolleyHttp.paramsEmpty(), object : VolleyHandler {
            override fun onSuccess(response: String?) {
                Logger.d("VolleyHttp", response!!)
            }

            override fun onError(error: String?) {
                Logger.e("VolleyHttp", error!!)
            }

        })

        // creating
//        VolleyHttp.post(VolleyHttp.API_CREATE_POST,
//            VolleyHttp.paramsCreate(poster),
//            object : VolleyHandler {
//                override fun onSuccess(response: String?) {
//                    Logger.d("@@@", response!!)
//                }
//
//                override fun onError(error: String?) {
//                    Logger.e("@@@", error!!)
//                }
//
//            })

        // updating
//        VolleyHttp.put(VolleyHttp.API_UPDATE_POST + poster.id,
//            VolleyHttp.paramsUpdate(poster),
//            object : VolleyHandler {
//                override fun onSuccess(response: String?) {
//                    Logger.d("@@@", response!!)
//                }
//
//                override fun onError(error: String?) {
//                    Logger.d("@@@", error!!)
//                }
//            })

        // deleting
        VolleyHttp.del(VolleyHttp.API_DELETE_POST + poster.id, object : VolleyHandler {
            override fun onSuccess(response: String?) {
                Logger.d("@@@", response!!)
            }

            override fun onError(error: String?) {
                Logger.d("@@@", error!!)
            }

        })
    }

}