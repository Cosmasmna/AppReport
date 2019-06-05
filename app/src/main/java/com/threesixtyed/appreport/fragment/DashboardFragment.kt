package com.threesixtyed.appreport.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.*

import com.threesixtyed.appreport.R
import com.threesixtyed.appreport.adapter.AppListReportAdapter
import com.threesixtyed.appreport.model.AppInfo

class DashboardFragment : Fragment() {

    private var databaseReference: DatabaseReference? = null

    var app_list=ArrayList<AppInfo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        databaseReference= FirebaseDatabase.getInstance().getReference("app")

        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_dashboard, container, false)

        databaseReference!!.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){


                    for (h in p0.children){
                        Log.i("AppInfo fragment>>>",h.key)

                        var app_name=h.child("app_name").value.toString()
                        var img_url=h.child("img_url").value.toString()
                        var latest_vname=h.child("latest_version").child("v_name").value.toString()
                        var latest_vlink=h.child("latest_version").child("link").value.toString()

                        var appInfo=AppInfo(app_name,img_url,latest_vname,latest_vlink)

                        app_list.add(appInfo)



                        Log.i("url",app_name+img_url+latest_vname+latest_vlink)
                    }
                    Log.i("List Size",""+app_list.size)

                    appReportBindAdapter(app_list)


                }

            }
        })
        return view
    }

    private fun appReportBindAdapter(app_list: ArrayList<AppInfo>) {

        var appListRecycler=view!!.findViewById<RecyclerView>(R.id.application_list_recycle)
        appListRecycler.layoutManager=LinearLayoutManager(context)
        var adapter=AppListReportAdapter(context,app_list)
        appListRecycler.adapter=adapter

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }
}
