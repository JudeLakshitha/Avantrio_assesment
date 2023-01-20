package com.judelakshitha.avantriotest.network

import android.content.Context
import android.util.Log
import com.android.volley.NetworkResponse
import com.android.volley.ParseError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.judelakshitha.avantriotest.adapters.UserAdapter
import com.judelakshitha.avantriotest.adapters.UserLogsAdapter
import com.judelakshitha.avantriotest.models.User
import com.judelakshitha.avantriotest.models.UserLog
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class VolleyServiceImpl(context: Context) : VolleyServiceInterface {

    val basePath = ""
    var context = context

    override fun sendArrayRequest(
        path: String,
        params: JSONObject,
        completionHandler: (response: JSONObject?) -> Unit
    ) {
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        val request: JsonArrayRequest =
            object :
                JsonArrayRequest(Method.GET, basePath + path, null, Response.Listener { response ->
                    Log.i("onResponse", response.toString())
                    Log.i("onResponse", response.length().toString());

                    try {
                        for (i in 0 until response.length()) {
                            val type = response.getJSONObject(i)
                            completionHandler(type)
                        }

                    } catch (e: Exception) {
                        val jsonObject = JSONObject()
                        jsonObject.put("response", response)
                        completionHandler(jsonObject)
                    }

                }, Response.ErrorListener { error ->
                    Log.e("onErrorResponse", error.toString())

                }) {
                override fun getHeaders(): Map<String, String> {
                    val logPref = context.getSharedPreferences("logPref", Context.MODE_PRIVATE)
                    val token = logPref.getString("token", "")
                    val headers: MutableMap<String, String> = HashMap()
                    headers["Authorization"] = "Bearer $token"
                    return headers
                }

                override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONArray>? {
                    if (response != null) {
                        if (response.statusCode != 200) {
                            return Response.error(ParseError(response))
                        } else {
                            try {
                                if (response.data.isEmpty()) {
                                    val responseData = "{}".toByteArray(charset("UTF8"))
                                    //response = NetworkResponse(response.statusCode, responseData, response.headers, response.notModified)
                                }
                            } catch (e: UnsupportedEncodingException) {
                                e.printStackTrace()
                            }
                        }
                    }
                    return super.parseNetworkResponse(response)
                }
            }
        requestQueue.add(request)
    }

    override fun sendObjRequest(
        path: String,
        params: JSONObject,
        completionHandler: (response: JSONObject?) -> Unit
    ) {

        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        val jsonObject = JSONObject()
        val request: JsonObjectRequest = object :
            JsonObjectRequest(
                Method.GET,
                basePath + path,
                jsonObject,
                Response.Listener { response ->

                    try {
                        val type = response
                        completionHandler(type)

                    } catch (e: Exception) {
                        val jsonObject = JSONObject()
                        jsonObject.put("response", response)
                        completionHandler(jsonObject)
                    }

                },
                Response.ErrorListener { error ->
                    Log.e("onErrorResponse", error.toString())

                }) {
            override fun getHeaders(): Map<String, String> {

                val logPref = context.getSharedPreferences("logPref", Context.MODE_PRIVATE)
                val token = logPref.getString("token", "")
                val headers: MutableMap<String, String> = HashMap()
                headers["Authorization"] = "Bearer $token"
                return headers
            }

            override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject>? {
                if (response != null) {
                    if (response.statusCode != 200) {
                        return Response.error(ParseError(response))
                    } else {
                        try {
                            if (response.data.isEmpty()) {
                                val responseData = "{}".toByteArray(charset("UTF8"))
                                //response = NetworkResponse(response.statusCode, responseData, response.headers, response.notModified)
                            }
                        } catch (e: UnsupportedEncodingException) {
                            e.printStackTrace()
                        }
                    }
                }
                return super.parseNetworkResponse(response)
            }
        }
        requestQueue.add(request)
    }
}