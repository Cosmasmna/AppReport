package com.threesixtyed.appreport.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threesixtyed.appreport.R
import com.threesixtyed.appreport.model.AppReport
import kotlinx.android.synthetic.main.report_view_list.view.*

class AppReportAdapter (val context:Context,val reportViewList:ArrayList<AppReport>): RecyclerView.Adapter<AppReportHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): AppReportHolder {

        return AppReportHolder(LayoutInflater.from(context).inflate(R.layout.report_view_list, p0, false))
    }

    override fun getItemCount(): Int {
return  reportViewList.size
    }

    override fun onBindViewHolder(p0: AppReportHolder, p1: Int) {

        p0.reportType.text=reportViewList.get(p1).report_type
        p0.appVersionName.text=reportViewList.get(p1).app_version
        p0.appReportDetail.text=reportViewList.get(p1).report_detail
        p0.reportUserName.text=reportViewList.get(p1).user_name
        p0.reportPhoneModelVersion.text=reportViewList.get(p1).phone_model+"("+reportViewList.get(p1).android_version+")"
        p0.reportDetailDate.text=reportViewList.get(p1).report_date
    }
}

class AppReportHolder (view: View): RecyclerView.ViewHolder(view) {
   val reportType=view.app_report_type
    val appVersionName=view.app_version_name
    val appReportDetail=view.app_report_detail
    val reportUserName=view.user_name
    val reportPhoneModelVersion=view.ph_model_version
    val reportDetailDate=view.report_detail_date
}
