package com.example.recipes

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recipes.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var username :String
    private lateinit var email :String
    private lateinit var password :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        firebaseAuth = FirebaseAuth.getInstance()

        binding.txtlogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0);
        }

        binding.btnSignUp.setOnClickListener{
            validateData()
        }

    }

    private fun validateData(){
        username = binding.txtSignUpUsername.text.toString().trim()
        email = binding.txtSignUpEmail.text.toString().trim()
        password = binding.txtSignUpPassword.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.txtSignUpEmail.error = "Invalid email format"
        }
        else if(TextUtils.isEmpty(password)){
            binding.txtSignUpPassword.error = "Please enter password"
        }
        else if(password.length < 6){
            binding.txtSignUpPassword.error = "Password must be at least 6 characters long"
        }
        else{
            firebaseSignUp()
        }

    }
    private fun firebaseSignUp() {
        firebaseAuth.createUserWithEmailAndPassword (email, password)
            .addOnSuccessListener {
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email

                Toast.makeText(this, "Account created with email $email", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, RecipesActivity::class.java))
                finish()
            }
            .addOnFailureListener {e->
                Toast.makeText(this, "Signup failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}