package com.id.destinasyik.ui.preference

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.id.destinasyik.databinding.ActivityPreferedInputBinding

class PreferedInputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreferedInputBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPreferedInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }
}