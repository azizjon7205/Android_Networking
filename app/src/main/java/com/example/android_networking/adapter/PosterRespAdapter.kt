package com.example.android_networking.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_networking.R
import com.example.android_networking.model.PosterResp
import com.example.android_networking.utils.Alerts

class PosterRespAdapter(var context: Context,
                        var items: ArrayList<PosterResp>,
                        val apiDeletePoster:(PosterResp) -> Unit,
                        val apiUpdatePoster: (PosterResp, Int) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PosterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_poster_list, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PosterViewHolder){
            holder.bind(position)
        }
    }

    override fun getItemCount() = items.size

    inner class PosterViewHolder(view: View): RecyclerView.ViewHolder(view){
        private var tv_title: TextView = view.findViewById(R.id.tv_title)
        private var tv_body: TextView = view.findViewById(R.id.tv_body)
        private var ll_update: LinearLayout = view.findViewById(R.id.ll_update)
        private var ll_delete: LinearLayout = view.findViewById(R.id.ll_delete)

        var view_foreground: LinearLayout =view.findViewById(R.id.ll_poster)

        fun bind(position: Int){
            var poster = items[position]

            tv_body.text = poster.body
            tv_title.text = poster.title!!.toUpperCase()

            ll_update.setOnClickListener {
                Alerts.createrDialog(context, poster){
                    apiUpdatePoster.invoke(it, position)
                }
            }

            ll_delete.setOnClickListener {
                Alerts.deleterDialog(context) {
                    apiDeletePoster.invoke(poster)
                }
            }
        }
    }
}