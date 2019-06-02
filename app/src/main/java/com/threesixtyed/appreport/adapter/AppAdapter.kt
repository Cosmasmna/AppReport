package com.threesixtyed.appreport.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.threesixtyed.appreport.R
import com.threesixtyed.appreport.model.User
import kotlinx.android.synthetic.main.app_available_item.view.*
import java.text.FieldPosition

class AppAdapter(val items : ArrayList<User>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.app_available_item, p0, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.appname?.text= items.get(position).user_name.toString()
        holder?.appversion?.text= items.get(position).password.toString()
     }

    override fun getItemCount(): Int {
        return items.size
    }

}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val appname = view.appName
    val appversion=view.apVersion
    val btnDownload=view.btnDownload
    val btnReport=view.btnReport

}