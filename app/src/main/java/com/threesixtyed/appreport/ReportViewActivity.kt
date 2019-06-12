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
                filterDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun filterDialog() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this, R.style.customizedAlert)

        var alertDialog=alertDialogBuilder.create()

        val dialogView = LayoutInflater.from(this).inflate(R.layout.report_filter_dialog, null)

        var btnFilter=dialogView.findViewById<Button>(R.id.btnFilter)
        var btnFilterCancel=dialogView.findViewById<Button>(R.id.btnCancelFilter)
        var sp_app_version=dialogView.findViewById<Spinner>(R.id.sp_app_version)
        var sp_report_type=dialogView.findViewById<Spinner>(R.id.sp_report_type)
        var ch_app_version=dialogView.findViewById<CheckBox>(R.id.ch_app_version)
        var ch_report_type=dialogView.findViewById<CheckBox>(R.id.ch_report_type)

        var versions = arrayOf("v1.0","v1.1","v2.0","v2.2.4","v3.0","v4.0","v5.0","v6.0","v6.0","v7.0")
        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, versions)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_app_version!!.setAdapter(aa)

        var reportType = arrayOf("Bug Report","Request Feature")
        val bb = ArrayAdapter(this, android.R.layout.simple_spinner_item, reportType)
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_report_type!!.setAdapter(bb)


        btnFilter.setOnClickListener {
            if(ch_app_version.isChecked)
                Toast.makeText(this,"Selected version "+sp_app_version.selectedItem,Toast.LENGTH_LONG).show()
            else
                Toast.makeText(this,"Show all about version",Toast.LENGTH_LONG).show()
            if(ch_report_type.isChecked)
                Toast.makeText(this,"Selected Report Type "+sp_report_type.selectedItem,Toast.LENGTH_LONG).show()
            else
                Toast.makeText(this,"Show all about report Type",Toast.LENGTH_LONG).show()
            alertDialog.dismiss()
        }
        btnFilterCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.setTitle("Filter Report")
        alertDialog.setView(dialogView)
        alertDialog.show()

    }
}