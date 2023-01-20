package com.judelakshitha.avantriotest.network

import org.json.JSONObject

class APIController constructor(serviceInjection: VolleyServiceInterface) : VolleyServiceInterface {

    private val service: VolleyServiceInterface = serviceInjection

    override fun sendArrayRequest(
        path: String,
        params: JSONObject,
        completionHandler: (response: JSONObject?) -> Unit
    ) {
        service.sendArrayRequest(path, params, completionHandler)
    }

    override fun sendObjRequest(
        path: String,
        params: JSONObject,
        completionHandler: (response: JSONObject?) -> Unit
    ) {
        service.sendObjRequest(path, params, completionHandler)
    }

}