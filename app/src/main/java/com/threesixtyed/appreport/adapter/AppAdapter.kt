package com.threesixtyed.appreport.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.threesixtyed.appreport.R
import com.threesixtyed.appreport.ReportDeatilActivity
import com.threesixtyed.appreport.model.AppInfo
import kotlinx.android.synthetic.main.app_available_item.view.*

class AppAdapter(val items : ArrayList<AppInfo>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.app_available_item, p0, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.appname?.text= items.get(position).app_name
        holder?.appversion?.text= items.get(position).latest_vname

        holder.btnReport.setOnClickListener {
            val i=Intent(context,ReportDeatilActivity::class.java)
            i.putExtra("app_name",items.get(position).app_name)
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(i)
        }

        Glide.with(context).load(items.get(position).img_url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.img)
        var playstoreLink=Uri.parse(items.get(position).latest_vlink)
        var directLink=Uri.parse(items.get(position).direct_link)
        holder.btnPlaystore.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW,playstoreLink)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

        }
        holder.btnDownload.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW,directLink)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val appname = view.appName
    val appversion=view.appVersion
    val btnDownload=view.btnDownload
    val btnReport=view.btnReport
    val img=view.img
    val  btnPlaystore=view.btnPlaystore
}