package com.threesixtyed.appreport

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.google.firebase.database.*
import com.threesixtyed.appreport.adapter.AppReportAdapter
import com.threesixtyed.appreport.model.AppReport
import kotlinx.android.synthetic.main.report_view_activity.*

class ReportViewActivity:AppCompatActivity() {

    private var databaseReference: DatabaseReference? = null

    var reportList=ArrayList<AppReport>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report_view_activity)
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)

        databaseReference = FirebaseDatabase.getInstance().getReference("report")


        val intent=intent
        val appName=intent.getSerializableExtra("appName").toString()
        val appVersion=intent.getSerializableExtra("appVersion").toString()
        toolbar.title=appName
        toolbar.subtitle=appVersion


        databaseReference!!.child(appName).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                Log.i("DataSnap",p0.key)



                if (p0.exists()){

                    for (data in p0.children){


                        val appReport=data.getValue(AppReport::class.java)
                        Log.i("Reporter**", appReport!!.report_date)
                        reportList.add(appReport!!)
                        Log.i("Report List", reportList.size.toString())


                    }
                    detailListRecycler.layoutManager=LinearLayoutManager(this@ReportViewActivity)
                    val adapter=AppReportAdapter(this@ReportViewActivity,reportList,appName)
                    detailListRecycler.adapter=adapter

                }else{
                 Toast.makeText(this@ReportViewActivity,"No detail",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.filter_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.report_filter -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}