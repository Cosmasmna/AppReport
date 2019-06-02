package com.threesixtyed.appreport.adapter

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import com.threesixtyed.appreport.R
import com.threesixtyed.appreport.model.AppReport
import kotlinx.android.synthetic.main.report_deati_rowl.view.*

class AppReportAdapter (val context:Context,val reportList:ArrayList<AppReport>):RecyclerView.Adapter<MyHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int):MyHolder {
        return MyHolder(LayoutInflater.from(context).inflate(R.layout.report_deati_rowl, p0, false))

    }


    override fun getItemCount(): Int {
        return reportList.size
    }

    override fun onBindViewHolder(p0: MyHolder, p1: Int) {
        p0?.app_name?.text= reportList.get(p1).android_version
        p0?.app_version?.text= reportList.get(p1).app_version
    }

}

class MyHolder(view: View): RecyclerView.ViewHolder(view) {
    val app_name=view.app_name
    val app_version=view.app_version
}