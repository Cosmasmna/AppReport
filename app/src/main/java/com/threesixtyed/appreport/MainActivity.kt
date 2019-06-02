package com.threesixtyed.appreport

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.database.FirebaseDatabase
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var btnReport: Button


    private var databaseReference: DatabaseReference? = null
    private var ref: DatabaseReference? = null

    lateinit var sharePreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        sharePreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE)



        btnReport = findViewById(R.id.btnReport)
        btnReport.setOnClickListener() {
            startActivity(Intent(this, ReportDeatilActivity::class.java))
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
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
}
