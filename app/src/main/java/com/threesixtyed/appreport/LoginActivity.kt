package com.threesixtyed.appreport

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.*
import com.threesixtyed.appreport.model.User
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {


    private var databaseReference: DatabaseReference? = null
    private var ref: DatabaseReference? = null
    lateinit var sharePreferences:SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sharePreferences=getSharedPreferences("mypref", Context.MODE_PRIVATE)

        databaseReference = FirebaseDatabase.getInstance().getReference("user")



/*
        btnLogin.setOnClickListener {
            var name: String = etName.text.toString()
            var password: String = etPassword.text.toString()
            var user=User(name,password)
            databaseReference!!.push().key
            databaseReference!!.child(databaseReference!!.push().key.toString()).setValue(user)


            if (name.equals(""){

           ) }
            else if (password.equals(""){

           ) }else{

            }

        }

*/


        btnLogin.setOnClickListener {

            var success:Boolean = false

            var name: String = etName.text.toString()
            var password: String = etPassword.text.toString()
            if (name .equals( "")) {
                          etName.error="Enter user name"

            } else if (password .equals( "") ){
                           etPassword.error = "Enter password"
            }

            else if (name.equals("admin") && password.equals("123")){

                Toast.makeText(this,"Login As Admin",Toast.LENGTH_LONG).show()

                val editor=sharePreferences.edit()
                editor.putString("name",name)
                editor.commit()
                startActivity(Intent(this@LoginActivity,AdminMainActivity::class.java))
                finish()
            }

            else {

                    val li = object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.exists()) {

                                for (h in p0.children) {
                                    Log.i("onclick##", h.key)

                                    databaseReference!!.child(h.key.toString())
                                        .addValueEventListener(object : ValueEventListener {
                                            override fun onCancelled(p0: DatabaseError) {

                                            }

                                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    val user = dataSnapshot.getValue(User::class.java)
                                                    if (name .equals( user!!.user_name)) {

                                                        if (password .equals( user!!.password)) {
                                                            success = true

                                                            val editor=sharePreferences.edit()
                                                            editor.putString("name",name)
                                                            editor.commit()
                                                            Toast.makeText(this@LoginActivity,"Successful",Toast.LENGTH_LONG).show()
                                                            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                                                            finish()
                                                        }
                                                    }

                                                    Log.i("length#", user!!.user_name + "^^^^" + success)
                                                }


                                            }
                                        })

                                }

                            }


                        }
                    }
                    databaseReference!!.addListenerForSingleValueEvent(li)
            }
        }

    }
}
