package com.judelakshitha.avantriotest.network

import org.json.JSONObject

interface VolleyServiceInterface {

    fun sendArrayRequest(
        path: String,
        params: JSONObject,
        completionHandler: (response: JSONObject?) -> Unit
    )

    fun sendObjRequest(
        path: String,
        params: JSONObject,
        completionHandler: (response: JSONObject?) -> Unit
    )

}