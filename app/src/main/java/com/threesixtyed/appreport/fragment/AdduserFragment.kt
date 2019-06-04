package com.threesixtyed.appreport.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import com.threesixtyed.appreport.R
import com.threesixtyed.appreport.model.User
import kotlinx.android.synthetic.main.fragment_adduser.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class AdduserFragment : Fragment() {

    var databaseReference: DatabaseReference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_adduser, container, false)

        databaseReference = FirebaseDatabase.getInstance().getReference("user")


//        btnAdd.setOnClickListener {
//            var name: String = et_add_user_name.text.toString()
//            var password: String = et_add_password.text.toString()
//            var user = User(name, password)
//            databaseReference!!.push().key
//            databaseReference!!.child(databaseReference!!.push().key.toString()).setValue(user)
//            Toast.makeText(
//                context,
//                et_add_user_name.text.toString() + "" + et_add_password.text.toString(),
//                Toast.LENGTH_LONG
//            ).show()
//
//
//        }


        return view
    }


}
