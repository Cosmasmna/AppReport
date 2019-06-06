package com.threesixtyed.appreport.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.threesixtyed.appreport.R
import com.threesixtyed.appreport.ReportViewActivity
import com.threesixtyed.appreport.model.AppInfo
import kotlinx.android.synthetic.main.app_list_view_report.view.*

class AppListReportAdapter(val context: Context?, val appList: ArrayList<AppInfo>) : RecyclerView.Adapter<MyHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyHolder {
        return MyHolder(LayoutInflater.from(context).inflate(R.layout.app_list_view_report, p0, false))

    }


    override fun getItemCount(): Int {
        return appList.size
    }

    override fun onBindViewHolder(p0: MyHolder, p1: Int) {
        p0.app_name.text = appList.get(p1).app_name
        p0.app_version.text = appList.get(p1).latest_vname

        p0.itemView.setOnClickListener {
            Toast.makeText(context, appList.get(p1).app_name, Toast.LENGTH_SHORT).show()
            val intent = Intent(context, ReportViewActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("appName", appList.get(p1).app_name)
            intent.putExtra("appVersion", appList.get(p1).latest_vname)

            context!!.startActivity(intent)

        }
    }

}

class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
    val app_name = view.app_name_report_list
    val app_version = view.app_version_report_list
    // val btn = view.btnViewReport
}
