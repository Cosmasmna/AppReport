package com.threesixtyed.appreport

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.threesixtyed.appreport.fragment.AddProductFragment
import com.threesixtyed.appreport.fragment.AdduserFragment
import com.threesixtyed.appreport.fragment.DashboardFragment
import kotlinx.android.synthetic.main.activity_admin_main.*

class AdminMainActivity : AppCompatActivity() {


    lateinit var sharePreferences: SharedPreferences

    var fragment: Fragment? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.admin_main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.logout -> {
                startActivity(Intent(this, LoginActivity::class.java))

                sharePreferences.edit().clear().commit()
                finish()
                Toast.makeText(this, "Logout", Toast.LENGTH_LONG).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)
        setTitle("Dashboard")

        sharePreferences=getSharedPreferences("mypref", Context.MODE_PRIVATE)
      //  databaseReference= FirebaseDatabase.getInstance().getReference("app")

        val transition = supportFragmentManager.beginTransaction()
        transition.replace(R.id.container, DashboardFragment()).commit()
        transition.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.dashboard -> {
                this.setTitle("Dashboard")
                fragment = DashboardFragment()
                //return@OnNavigationItemSelectedListener true
            }
            R.id.prodcut -> {
                this.setTitle("Add Product")
                fragment = AddProductFragment()
                //  return@OnNavigationItemSelectedListener true
            }
            R.id.addPerson -> {
                this.setTitle("Add User")
                fragment = AdduserFragment()
                //return@OnNavigationItemSelectedListener true
            }
        }
        val transition = supportFragmentManager.beginTransaction()
        transition.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transition.replace(R.id.container, fragment!!).commit()
        true
    }
}
