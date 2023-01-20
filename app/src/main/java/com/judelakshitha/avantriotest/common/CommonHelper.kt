package com.judelakshitha.avantriotest.common

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.NetworkError
import com.android.volley.ServerError
import com.android.volley.VolleyError

class CommonHelper {

    fun clearSharedPref(context: Context, sharedPref: String) {
        val loginSharedPref =
            context.getSharedPreferences("logPref", AppCompatActivity.MODE_PRIVATE)
        loginSharedPref.edit().clear().apply()
    }

    fun VolleyErrorMsg(error: VolleyError): String {
        var errorMessage = ""
        if (error is NetworkError) {
            errorMessage = "Cannot connect to Service.\nPlease check your \nconnection!"
        } else if (error is ServerError) {
            errorMessage = "Server could not \nbe found. Please try again \nafter some time!"
        } else if (error is AuthFailureError) {
            errorMessage = "Authentication Failed! \nPlease login again!"
        } else {
            errorMessage = "Service is not working.\nPlease try again!"
        }
        return errorMessage
    }
}