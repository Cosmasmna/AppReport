package com.threesixtyed.appreport.adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        p0.appVersionName.text="Version: "+reportViewList.get(p1).app_version
        p0.appReportDetail.text=reportViewList.get(p1).report_detail
        p0.appReportDetail.setOnClickListener {
            p0.appReportDetail.maxLines=Int.MAX_VALUE
            p0.appReportDetail.text=reportViewList.get(p1).report_detail

        }
        //p0.appReportDetail.text="Application Details Application Details Application Details Application Details Application Details Application Details Application Details Application Details Application Details Application Details Application Details Application Details Application Details Application Details Application Details Application Details Application Details Application Details Application Details Application Details Application Details Application Details Application Details Application Details Application Details Application Details Application Details Application Details Application Details Application Details"
        p0.reportUserName.text=reportViewList.get(p1).user_name
        p0.reportPhoneModelVersion.text=reportViewList.get(p1).phone_model+"("+reportViewList.get(p1).android_version+")"
        p0.reportDetailDate.text="Date: "+reportViewList.get(p1).report_date

        p0.status.setOnClickListener {
            showDialog(p0.status_txt)
        }

    }
    private fun showDialog(status: TextView?) {
        val vL = arrayOf("UNSOLVE","PROGRESS","COMPLETE","BLOCK")
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.customizedAlert)
        alertDialogBuilder.setTitle("Status")
        alertDialogBuilder.setItems(vL) { dialog, which ->
            status!!.text = vL[which]
            if(vL[which].equals("UNSOLVE"))
                status.setTextColor(Color.GRAY)
            if(vL[which].equals("PROGRESS"))
                status.setTextColor(Color.GREEN)
            if(vL[which].equals("COMPLETE"))
                status.setTextColor(Color.BLUE)
            if(vL[which].equals("BLOCK"))
                status.setTextColor(Color.RED)
        }
        alertDialogBuilder.show()
    }
}

class AppReportHolder (view: View): RecyclerView.ViewHolder(view) {
    val status=view.edit_status
   val reportType=view.app_report_type
    val appVersionName=view.app_version_name
    val appReportDetail=view.app_report_detail
    val reportUserName=view.user_name
    val reportPhoneModelVersion=view.ph_model_version
    val reportDetailDate=view.report_detail_date

    val status_txt=view.status_text
}
