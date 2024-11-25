package com.id.destinasyik.ui.register

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.id.destinasyik.R
import com.id.destinasyik.databinding.ActivityRegisterBinding
import com.id.destinasyik.model.MainViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.buttonRegister.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.passwordInput.text.toString()
            val email = binding.etEmail.text.toString()
            val passwordConfirmation = binding.passwordConfirmation.text.toString()
            val city = binding.etCity.text.toString()
            val age = binding.etBorn.text.toString()
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val name = firstName+" "+lastName
            val preferedCategory: String? = null
            if(password == passwordConfirmation){
                preferedCategory?.let { prefered ->
                    viewModel.registerUser(username, name, password, age, email, city,
                        prefered
                    )
                }
            }else{
                Toast.makeText(this@RegisterActivity, "Password Not The Same", Toast.LENGTH_SHORT).show()
            }
            viewModel.registrationStatus.observe(this, Observer { isRegistered->
                if(isRegistered){
                    Toast.makeText(this@RegisterActivity, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}