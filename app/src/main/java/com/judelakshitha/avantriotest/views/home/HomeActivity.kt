package com.judelakshitha.avantriotest.views.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.judelakshitha.avantriotest.R
import com.judelakshitha.avantriotest.views.log.UsersFragment
import com.judelakshitha.avantriotest.views.login.LoginActivity

class HomeActivity : AppCompatActivity() {


    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var toolBar: androidx.appcompat.widget.Toolbar
    var token = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        toolBar = findViewById(R.id.home_toolbar)

        setSupportActionBar(toolBar)
        supportActionBar!!.setDisplayShowHomeEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val mCustomView = LayoutInflater.from(this).inflate(R.layout.toolbar, null)
        val title = mCustomView.findViewById(R.id.tv_title) as TextView
        title.text = "Users"

        supportActionBar!!.customView = mCustomView
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val appSharedData = getSharedPreferences("logPref", Context.MODE_PRIVATE)
        token = appSharedData.getString("token", "")!!

        val usersFragment = UsersFragment()
        setCurrentFragment(usersFragment)

        bottomNavigationView.menu.findItem(R.id.item_home).isEnabled = false
        bottomNavigationView.menu.findItem(R.id.item_settings).isEnabled = false
        bottomNavigationView.selectedItemId = R.id.item_users

    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}