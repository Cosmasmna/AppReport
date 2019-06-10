package com.threesixtyed.appreport.adapter

import android.content.Context
import android.opengl.Visibility
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threesixtyed.appreport.R
import com.threesixtyed.appreport.model.AppReport
import kotlinx.android.synthetic.main.user_view_report_row.view.*

class UserViewReportAdapter(val context: Context?, val appreport: ArrayList<AppReport>) :
    RecyclerView.Adapter<MyViewHolder>() {

    override fun onBindViewHolder(p0: MyViewHolder, p1: Int) {
//        p0.app_version.text = appreport.get(p1).app_version
//        p0.android_version.text = appreport.get(p1).android_version
//        p0.android_model.text = appreport.get(p1).phone_model
//        p0.report_date.text = appreport.get(p1).report_date
//        p0.report_detail.text = appreport.get(p1).report_detail

        p0.itemView.setOnClickListener {
            if (p0.layout_detail.visibility == View.GONE) {
                p0.layout_detail.visibility = View.VISIBLE
                p0.report_detail.maxLines = Int.MAX_VALUE
            } else {
                p0.layout_detail.visibility = View.GONE
                p0.report_detail.maxLines = 1

            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.user_view_report_row, p0, false))

    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}

class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    //    val app_version = view.txt_app_version
//    val android_version = view.txt_android_version
//    val android_model = view.txt_android_model
//    val report_date = view.txt_report_date
//    val report_detail = view.txt_report_detail
    val report_detail = view.txt_report_detail
    val layout_detail = view.layout_detail
}
