package com.example.recipes

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recipes.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var email :String
    private lateinit var password :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        binding.txtsignup.setOnClickListener {val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);}

        binding.btnLogIn.setOnClickListener{
           validateData()
        }

    }
    private fun validateData(){
        email = binding.txtLogInEmail.text.toString().trim()
        password = binding.txtLogInPassword.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.txtLogInEmail.error = "Invalid email format"
        }
        else if(TextUtils.isEmpty(password)){
            binding.txtLogInPassword.error = "Please enter password"
        }
        else{
            firebaseLogIn()
        }

    }

    private fun firebaseLogIn() {
        //data validated begin login

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this,"Logged in as $email", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,RecipesActivity::class.java))
                finish()

            }
            .addOnFailureListener{ e->
                Toast.makeText(this, "Login failed. ${e.message}", Toast.LENGTH_SHORT).show()

            }
    }

    private fun checkUser()  {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            //user is already logged in
            startActivity(Intent(this, RecipesActivity::class.java))
            finish()

        }

    }
}