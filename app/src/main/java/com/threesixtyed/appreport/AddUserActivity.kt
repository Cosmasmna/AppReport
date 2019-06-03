package com.threesixtyed.appreport

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.threesixtyed.appreport.model.User
import kotlinx.android.synthetic.main.activity_add_user.*

class AddUserActivity : AppCompatActivity() {


    private var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)
        databaseReference = FirebaseDatabase.getInstance().getReference("user")



        btnAdd.setOnClickListener {
            var name: String = et_add_user_name.text.toString()
            var password: String = et_add_password.text.toString()
            var user= User(name,password)
            databaseReference!!.push().key
            databaseReference!!.child(databaseReference!!.push().key.toString()).setValue(user)
            Toast.makeText(this,et_add_user_name.text.toString()+""+et_add_password.text.toString(),Toast.LENGTH_LONG).show()


        }
    }
}
