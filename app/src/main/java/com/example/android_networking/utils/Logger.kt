package com.example.android_networking.utils

import android.util.Log
import com.example.android_networking.network.volley.VolleyHttp

class Logger {
    companion object{
        fun d(tag: String, msg: String){
            if (!VolleyHttp.IS_TESTER) Log.d(tag, msg)
        }

        // information
        fun i(tag: String, msg: String){
            if (VolleyHttp.IS_TESTER) Log.i(tag, msg)
        }

        // warning
        fun v(tag: String, msg: String){
            if (VolleyHttp.IS_TESTER) Log.v(tag, msg)
        }

        // errors
        fun e(tag: String, msg: String){
            if (VolleyHttp.IS_TESTER) Log.e(tag, msg)
        }

    }
}