package com.threesixtyed.appreport.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.Toolbar
import android.view.*
import com.threesixtyed.appreport.R


class EditUserReportDialog : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.edit_user_report, container, false)


        val toolbar = view.findViewById<Toolbar>(R.id.activity_report_toolbar)
        toolbar.setNavigationIcon(R.drawable.cross)
        toolbar.setNavigationOnClickListener { dismiss() }
        toolbar.title = "DTP Chem"
        return view
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT

            //---animate the full screen dialog---//
            dialog.window!!.attributes.windowAnimations = R.style.DialogThemeAnim

            dialog.window!!.setLayout(width, height)
        }
    }



    companion object {
        var TAG = "FullScreenDialog"
    }


}
