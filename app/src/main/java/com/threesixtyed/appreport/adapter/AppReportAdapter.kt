package com.threesixtyed.appreport.adapter

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*
import com.threesixtyed.appreport.R
import com.threesixtyed.appreport.model.AppReport
import kotlinx.android.synthetic.main.report_view_list.view.*

var update_status:String?=null
var myReference:DatabaseReference?=FirebaseDatabase.getInstance().getReference("report")
lateinit var sharedPreferences: SharedPreferences
var app_name:String?=null

class AppReportAdapter (
    val context: Context,
    val reportViewList: ArrayList<AppReport>,
    val appName: String
): RecyclerView.Adapter<AppReportHolder>() {
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

            showDialog(p0.status,p1)
        }

    }
    private fun showDialog(status: TextView?, p1: Int) {
        val vL = arrayOf("Unsolve","Progress","Complete","Block")
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context, R.style.customizedAlert)
        alertDialogBuilder.setTitle("Status")
        alertDialogBuilder.setItems(vL) { dialog, which ->
            status!!.text = vL[which]
            if(vL[which].equals("Unsolve")){
                update_status="unsolve"
                dataUpdate("unsolve",p1)
                status.setTextColor(Color.GRAY)

            }

             else if(vL[which].equals("Progress")){
                update_status="progress"
                dataUpdate("progress", p1)
                status.setTextColor(Color.GREEN)
            }

             else if(vL[which].equals("Complete")){
                update_status="complete"
                dataUpdate("complete", p1)
                status.setTextColor(Color.BLUE)
            }


            else if(vL[which].equals("Block")){
            update_status="block"
            dataUpdate(update_status!!, p1)
            status.setTextColor(Color.RED)

        }
        }
        alertDialogBuilder.show()
    }

    fun dataUpdate(status_update: String, p1: Int){
        myReference!!.child(appName!!).child(reportViewList.get(p1).report_id).child("status").setValue(status_update.toString())


        this.notifyDataSetChanged()


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
}
