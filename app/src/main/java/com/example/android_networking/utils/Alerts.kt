package com.example.android_networking.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import com.example.android_networking.R
import com.example.android_networking.model.Poster
import com.example.android_networking.model.PosterResp

class Alerts {
    companion object {
        var instance: Alerts? = null
            get() {
                if (field == null)
                    field = Alerts()
                return field
            }

        fun createrDialog(context: Context, post: Any?, createPoster: (PosterResp) -> Unit) {
            var posterResp: PosterResp? = null
            var poster: Poster? = null
            if (post is PosterResp)
                posterResp = post

            val alertDialog = AlertDialog.Builder(context)

            val view = LayoutInflater.from(context).inflate(R.layout.alert_create_dialog, null)
            val edt_userId = view.findViewById<EditText>(R.id.edt_create_userId)
            val edt_title = view.findViewById<EditText>(R.id.edt_create_title)
            val edt_body = view.findViewById<EditText>(R.id.edt_create_body)

            alertDialog.setView(view)

            if (posterResp != null){
                edt_userId.text.insert(0, posterResp.userId.toString())
                edt_title.text.insert(0, posterResp.title.toString())
                edt_body.text.insert(0, posterResp.body.toString())

                alertDialog.setTitle("Update Poster")
            } else {
                alertDialog.setTitle("Create Poster")
            }

            alertDialog.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog!!.dismiss()
                    Log.d("RRR", "-> cancel")
                }
            })
            alertDialog.setPositiveButton("Save"
            ) { dialog, which ->
                if (edt_userId.text.isNotEmpty() && edt_title.text.isNotEmpty() && edt_body.text.isNotEmpty()) {

                    val userId = edt_userId.text.toString()
                    val title = edt_title.text.toString()
                    val body = edt_body.text.toString()

//                val poster = Poster(1, userId.toInt(), title, body)
//                apiPosterCreate(poster)

                    val posterResp1 = PosterResp(userId = userId.toInt(), title = title, body = body)
                    createPoster.invoke(posterResp1)

                } else {
                    Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT).show()
                }
            }
            alertDialog.create().show()

        }

        fun deleterDialog(context: Context, deletePoster: () -> Unit){
            androidx.appcompat.app.AlertDialog.Builder(context)
                .setTitle("Delete Poster")
                .setMessage("Are you sure you want to delete this poster?")
                .setPositiveButton(android.R.string.yes) { dialog, which ->
//                    apiPosterDelete(poster!!)
                    deletePoster.invoke()
                }
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create()
                .show()
        }
    }
}