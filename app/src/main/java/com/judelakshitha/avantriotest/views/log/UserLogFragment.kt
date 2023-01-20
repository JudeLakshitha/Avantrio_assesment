package com.judelakshitha.avantriotest.views.log

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.judelakshitha.avantriotest.R
import com.judelakshitha.avantriotest.adapters.UserLogsAdapter
import com.judelakshitha.avantriotest.common.CommonHelper
import com.judelakshitha.avantriotest.models.UserLog
import com.judelakshitha.avantriotest.views.log.UserLogsActivity.Companion.userId
import com.judelakshitha.avantriotest.views.login.LoginActivity
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.text.SimpleDateFormat


class UserLogFragment : Fragment() {

    lateinit var userLogAdapter: UserLogsAdapter
    lateinit var userLogListView: ListView

    var responseArrayList = ArrayList<UserLog>()
    var token = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val customView = layoutInflater.inflate(R.layout.fragment_user_log, null)

        userLogListView = customView.findViewById(R.id.lv_log_items)

        val appSharedData =
            (activity as UserLogsActivity).getSharedPreferences("logPref", Context.MODE_PRIVATE)
        token = appSharedData.getString("token", "")!!

        getLogDetails()

        return customView

    }

    private fun getLogDetails() {

        responseArrayList.clear()
        val url = "http://apps.avantrio.xyz:8010/api/user/${userId}/logs"
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val jsonTokenObj = JSONObject()
        jsonTokenObj.put("token", token)

        val request: JsonObjectRequest = object :
            JsonObjectRequest(Method.GET, url, jsonTokenObj, Response.Listener { response ->

                val userId = response.getString("user_id")
                val userName = response.getString("user")

                try {

                    val responseArray = response.getJSONArray("logs")

                    for (i in 0 until responseArray.length()) {
                        val logObj = responseArray.getJSONObject(i)
                        val id = logObj.getString("id")
                        val date = logObj.getString("date")
                        val time = logObj.getString("time")
                        val long = logObj.getString("longitude")
                        val lat = logObj.getString("latitude")

                        val logModelObj = UserLog()
                        logModelObj.id = id.toInt()
                        logModelObj.dateString = date
                        logModelObj.timeString = time
                        logModelObj.latitude = lat.toDouble()
                        logModelObj.longitude = long.toDouble()

                        responseArrayList.add(logModelObj)

                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

                userLogAdapter = UserLogsAdapter(requireContext(), responseArrayList)
                userLogListView.adapter = userLogAdapter
                userLogAdapter.notifyDataSetChanged()

            }, Response.ErrorListener { error ->
                Log.e("onErrorResponse", error.toString())
                var errorMessage = ""
                if (error is NetworkError) {
                    errorMessage = "Cannot connect to Service.\nPlease check your \nconnection!"

                } else if (error is ServerError) {
                    errorMessage =
                        "Server could not \nbe found. Please try again \nafter some time!"

                } else if (error is AuthFailureError) {
                    errorMessage = "Authentication Fail!"
                    CommonHelper().clearSharedPref(requireContext(), "logPref")
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()

                } else {
                    errorMessage = "Service is not working.\nPlease try again!"
                }
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()

            }) {
            override fun getHeaders(): Map<String, String> {

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