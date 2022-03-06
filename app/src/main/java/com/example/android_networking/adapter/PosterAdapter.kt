package com.example.android_networking.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_networking.R
import com.example.android_networking.activity.MainActivity
import com.example.android_networking.model.Poster

class PosterAdapter(var activity: MainActivity, var items: ArrayList<Poster>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PosterViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_poster_list, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PosterViewHolder){
            holder.bind()
        }
    }

    override fun getItemCount() = items.size

    inner class PosterViewHolder(view: View): RecyclerView.ViewHolder(view){
        private var tv_title: TextView = view.findViewById(R.id.tv_title)
        private var tv_body: TextView = view.findViewById(R.id.tv_body)
        private var ll_poster: LinearLayout = view.findViewById(R.id.ll_poster)

        fun bind(){
            var poster = items[adapterPosition]

            tv_body.text = poster.body
            tv_title.text = poster.title.toUpperCase()

            ll_poster.setOnLongClickListener {
//                activity.dialogDeletePoster(poster)
                Log.d("RRR", "post delete")
                false
            }
        }
    }
}