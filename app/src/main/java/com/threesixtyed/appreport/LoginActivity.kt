package com.threesixtyed.appreport

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
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

        //val snackbar = Snackbar.make(findViewById(R.id.lay), "Replace with your own action", Snackbar.LENGTH_LONG)

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
            progressBar.visibility=View.VISIBLE

            var success:Boolean
            var name: String = etName.text.toString()
            var password: String = etPassword.text.toString()
            if (name .equals( "")) {
                          etName.error="Enter user name"
                progressBar.visibility=View.GONE


            } else if (password .equals( "") ){
                           etPassword.error = "Enter password"
                progressBar.visibility=View.GONE

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

                                var i:Int=0
                                for (h in p0.children) {
                                    i++
                                    Log.i("onclick##", h.getValue(User::class.java)!!.user_name)

                                    val userName= h.getValue(User::class.java)!!.user_name

                                    val userpassword=h.getValue(User::class.java)!!.password
                                    if (name .equals(userName)) {

                                        success=true
                                        if (password .equals(userpassword)) {
                                            success = true

                                            val editor=sharePreferences.edit()
                                            editor.putString("name",name)
                                            editor.commit()
                                            Toast.makeText(this@LoginActivity,"Successful",Toast.LENGTH_LONG).show()
                                            var intent=Intent(this@LoginActivity,MainActivity::class.java)
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            startActivity(intent)
                                            finish()
                                            break
                                        }else
                                            success=false


                                    }else{
                                        success=false
                                    }

                                    Log.i("Count>>",i.toString())
                                    Log.i("Countchildren",p0.childrenCount.toString())

                                    if (i.toString().equals(p0.childrenCount.toString())){
                                        if (success!=true){
                                            Toast.makeText(this@LoginActivity,"wrong",Toast.LENGTH_LONG).show()
                                        }

                                        progressBar.visibility=View.GONE
                                    }
                                }
                            }
                        }
                    }
                    databaseReference!!.addListenerForSingleValueEvent(li)
            }
            /*if (success){
            }else{
                Toast.makeText(this,"wrong",Toast.LENGTH_LONG).show()
            }*/
        }

    }
}
