package com.threesixtyed.appreport.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.threesixtyed.appreport.AdminMainActivity

import com.threesixtyed.appreport.R
import com.threesixtyed.appreport.ReportViewActivity
import kotlinx.android.synthetic.main.app_list_view_report.*
import kotlinx.android.synthetic.main.app_list_view_report.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_dashboard, container, false)

        view.btnViewReport.setOnClickListener {
            startActivity(Intent(context,ReportViewActivity::class.java))
            Toast.makeText(context,"Report Activity",Toast.LENGTH_LONG).show()
        }
        return view
    }


}
