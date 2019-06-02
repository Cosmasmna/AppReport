package com.threesixtyed.appreport

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.threesixtyed.appreport.adapter.AndroidVersionAdapter
import com.threesixtyed.appreport.model.Version
import kotlinx.android.synthetic.main.activity_report_deatil.*
import kotlinx.android.synthetic.main.android_version_dialog.*

class ReportDeatilActivity : AppCompatActivity() {

    val app_version_list= ArrayList<Version>()

    val versionList= ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_deatil)

        setSupportActionBar(activity_report_toolbar)
       // var intent=Intent()
        var app_name=intent.getSerializableExtra("app_name")
        activity_report_toolbar.setTitle(app_name.toString())
        app_version_list!!.add(Version("v11","http","122"))
        app_version_list!!.add(Version("v22","http","122"))

        android_version_layout.setOnClickListener(){
            Toast.makeText(this,"click",Toast.LENGTH_LONG).show()
            showapp_version()
        }



    }

    private fun showapp_version() {
        val layoutInflater:LayoutInflater= LayoutInflater.from(this)
        val view: View =layoutInflater.inflate(R.layout.android_version_dialog,null)
        val alertDialogBuilder:AlertDialog.Builder=AlertDialog.Builder(this)
        alertDialogBuilder.setView(view)
        alertDialogBuilder.setTitle("App Version")
        var version_recycler=view.findViewById<RecyclerView>(R.id.android_version_recycle)
        version_recycler.layoutManager=LinearLayoutManager(this)
        version_recycler.adapter=AndroidVersionAdapter(applicationContext,app_version_list)
        val alert:AlertDialog=alertDialogBuilder.create()
        alert.show()
    }
/*
    fun showapp_version(){

        val layoutInflater:LayoutInflater= LayoutInflater.from(this)
        val view: View =layoutInflater.inflate(R.layout.android_version_dialog,null)
        val alertDialogBuilder:AlertDialog.Builder=AlertDialog.Builder(this)
        alertDialogBuilder.setView(view)
        android_version_recycle.layoutManager=LinearLayoutManager(this)



        versionList.add("v1.1")
        versionList.add("v3.3")

        android_version_recycle.adapter=AndroidVersionAdapter(applicationContext,app_version_list)
        val alert:AlertDialog=alertDialogBuilder.create()
        alert.show()
    }*/
}
