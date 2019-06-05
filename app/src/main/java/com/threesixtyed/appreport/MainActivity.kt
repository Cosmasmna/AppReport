package com.threesixtyed.appreport

import android.content.*
import android.net.ConnectivityManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Button
import com.google.firebase.database.FirebaseDatabase
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Toast
import com.google.firebase.database.*
import com.threesixtyed.appreport.adapter.AppAdapter
import com.threesixtyed.appreport.model.AppInfo
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {



    private var databaseReference: DatabaseReference? = null

    lateinit var sharePreferences: SharedPreferences

    private var brocast: BroadcastReceiver =object:BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent) {
            val notConnected=intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,false)
            if (notConnected){
               // Toast.makeText(applicationContext,"not Connected",Toast.LENGTH_LONG).show()

                recyclerView.visibility=View.GONE
                no_connection.visibility=View.VISIBLE
            }
            else {
              //  Toast.makeText(applicationContext, "Connected", Toast.LENGTH_LONG).show()
                no_connection.visibility=View.GONE
                recyclerView.visibility=View.VISIBLE
                bindData()
            }
        }

    }
    override fun onStart() {
        super.onStart()
        registerReceiver(brocast, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))

    }
    override fun onStop() {
        super.onStop()
        unregisterReceiver(brocast)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharePreferences=getSharedPreferences("mypref", Context.MODE_PRIVATE)
        databaseReference=FirebaseDatabase.getInstance().getReference("app")

        setSupportActionBar(toolbar)



        bindData()
    }

    private fun bindData() {
        var app_list=ArrayList<AppInfo>()

        databaseReference!!.addValueEventListener(object :ValueEventListener{


            override fun onCancelled(p0: DatabaseError) {
                Log.i("DbError>>>Main1",p0.toString())
            }

            override fun onDataChange(p0: DataSnapshot) {
                app_list.clear()

                if (p0.exists()){


                    for (h in p0.children){
                        Log.i("AppInfo name>>>",h.key)

                        var app_name=h.child("app_name").value.toString()
                        var img_url=h.child("img_url").value.toString()
                        var latest_vname=h.child("latest_version").child("v_name").value.toString()
                        var latest_vlink=h.child("latest_version").child("link").value.toString()

                        var appInfo=AppInfo(app_name,img_url,latest_vname,latest_vlink)

                        app_list.add(appInfo)



                        Log.i("url",app_name+img_url+latest_vname+latest_vlink)
                    }
                    Log.i("List Size",""+app_list.size)

                    bindAdapter(app_list)


                }
            }
        })



    }

    private fun bindAdapter(app_list: ArrayList<AppInfo>) {
        recyclerView.layoutManager=LinearLayoutManager(this)
        var adapter=AppAdapter(app_list,applicationContext)
        recyclerView.adapter=adapter
        adapter.notifyDataSetChanged()
        //recyclerView.scheduleLayoutAnimation()

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
