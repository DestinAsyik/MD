package com.id.destinasyik.ui.updatepw

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.id.destinasyik.R
import com.id.destinasyik.databinding.ActivityUpdatePasswordBinding

class UpdatePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdatePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.button3.setOnClickListener {
            showToast("Still Under Development!")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}