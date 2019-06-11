package com.threesixtyed.appreport

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.firebase.database.*
import com.threesixtyed.appreport.adapter.UserViewReportAdapter
import com.threesixtyed.appreport.model.AppReport
import kotlinx.android.synthetic.main.user_view_report_activity.*
class UserViewReport : AppCompatActivity() {

    var database: FirebaseDatabase? = null
    var databaseRef: DatabaseReference? = null
    lateinit var sharedPreferences: SharedPreferences
    var reportList = ArrayList<AppReport>()
    lateinit var name:String
    var appNameList= ArrayList<String>()

    var userviewAdapter:UserViewReportAdapter ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_view_report_activity)
        sharedPreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE)

        database = FirebaseDatabase.getInstance()
        databaseRef = FirebaseDatabase.getInstance().getReference("report")
        name = sharedPreferences!!.getString("name", "")
        bindAdapter()
        bindData()
    }

    private fun bindData() {
        databaseRef!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                reportList.clear()
                for (h in p0.children) {

                    databaseRef!!.child(h.key.toString()).addValueEventListener(object : ValueEventListener {

                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            if (p0.exists()) {
                                Log.i("keyyyyyyyyyy", h.key.toString())


                                for (data in p0.children) {


                                    val appreport = data.getValue(AppReport::class.java)
                                    val user_name: String = appreport!!.user_name

                                    if (name.equals(user_name)) {
                                        appNameList.add(h.key.toString())
                                        reportList!!.add(appreport!!)
                                    }


                                }

                                userviewAdapter!!.notifyDataSetChanged()

                            }
                        }

                    })
                }

            }

        })
    }
    private fun bindAdapter() {
        user_view_report_recycle.layoutManager =
            LinearLayoutManager(applicationContext)
        userviewAdapter = UserViewReportAdapter(applicationContext, reportList!!,appNameList)
        user_view_report_recycle.adapter = userviewAdapter

    }
}
