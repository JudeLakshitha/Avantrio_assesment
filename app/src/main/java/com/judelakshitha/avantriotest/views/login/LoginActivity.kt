package com.judelakshitha.avantriotest.views.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.volley.*
import com.android.volley.toolbox.BasicNetwork
import com.android.volley.toolbox.DiskBasedCache
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.judelakshitha.avantriotest.R
import com.judelakshitha.avantriotest.common.CommonHelper
import com.judelakshitha.avantriotest.views.home.HomeActivity
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    private lateinit var etuserName: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var toolbar: Toolbar
    private var username = ""
    private var password = ""
    var storedToken = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etuserName = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.btn_login)
        toolbar = findViewById(R.id.login_toolbar)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val mCustomView = LayoutInflater.from(this).inflate(R.layout.toolbar, null)
        supportActionBar!!.customView = mCustomView
        supportActionBar!!.setDisplayShowCustomEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val logPref = getSharedPreferences("logPref", MODE_PRIVATE)
        storedToken = logPref.getString("token", "")!!

        if (storedToken.isNotEmpty()){
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogin.setOnClickListener {
            username = etuserName.text.toString()
            password = etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginValidation()
            } else {
                Toast.makeText(this, "username & password cannot empty", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun loginValidation() {

        val url = "http://apps.avantrio.xyz:8010/api/user/login"
        var loginObj = JSONObject()
        val cache = DiskBasedCache(cacheDir, 1024 * 1024)
        val network = BasicNetwork(HurlStack())
        val requestQueue = RequestQueue(cache, network).apply {
            start()
        }

        loginObj.put("username", username)
        loginObj.put("password", password)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, loginObj, { response ->
            try {
                val accessToken = response.getString("token")
                val sharedPref = getSharedPreferences("logPref", MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putString("token", accessToken)
                editor.apply()

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        },
            { error ->
                val errorMessage = CommonHelper().VolleyErrorMsg(error)
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        )
        requestQueue.add(jsonObjectRequest)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                CommonHelper().clearSharedPref(this, "logPref")
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}