package com.judelakshitha.avantriotest.views.log

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.judelakshitha.avantriotest.R
import com.judelakshitha.avantriotest.models.UserLog


class UserLogsActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var tvUserName: TextView
    private lateinit var tabLayout: TabLayout
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar

    companion object {
        var userId = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_logs)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.menu.findItem(R.id.item_home).isEnabled = false
        bottomNavigationView.menu.findItem(R.id.item_settings).isEnabled = false
        bottomNavigationView.selectedItemId = R.id.item_users

        tvUserName = findViewById(R.id.tv_user_name)
        tabLayout = findViewById(R.id.tab_layout)
        toolbar = findViewById(R.id.log_toolbar)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowHomeEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val mCustomView = LayoutInflater.from(this).inflate(R.layout.toolbar, null)
        val title = mCustomView.findViewById(R.id.tv_title) as TextView
        title.text = "Users"

        supportActionBar!!.customView = mCustomView
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        userId = intent.getStringExtra("id").toString()
        val userName = intent.getStringExtra("userName").toString()

        tvUserName.text = userName
        tabLayout.addTab(tabLayout.newTab().setText("All"))
        tabLayout.addTab(tabLayout.newTab().setText("Location"))
        tabLayout.addTab(tabLayout.newTab().setText("Messages"))
        tabLayout.addTab(tabLayout.newTab().setText("Alert"))
        tabLayout.getTabAt(0)!!.select()
        replaceFragment(UserLogFragment())

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tabLayout.selectedTabPosition == 0) {
                    replaceFragment(UserLogFragment())
                } else if (tabLayout.selectedTabPosition == 1) {
                    replaceFragment(UserLocationFragment())
                } else if (tabLayout.selectedTabPosition == 2) {
                    replaceFragment(UserMsgsFragment())
                } else if (tabLayout.selectedTabPosition == 3) {
                    replaceFragment(UserAlertFragment())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        Handler().post {
            val fragmentManager = this.supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.tab_linear, fragment)
            transaction.commit()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

            R.id.item_menu_sort -> {
                val wrapper: Context = ContextThemeWrapper(this, R.style.ThemePopUpMenu)
                val popupMenu = PopupMenu(wrapper, toolbar.findViewById(R.id.item_menu_sort))
                popupMenu.menuInflater.inflate(R.menu.sort_menu, popupMenu.menu)
                popupMenu.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        return
    }
}