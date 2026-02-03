package com.abhishek.internships.identifier.focusflow.permissions

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.abhishek.internships.identifier.focusflow.R
import com.abhishek.internships.identifier.focusflow.databinding.ActivityGetStartedBinding
import com.abhishek.internships.identifier.focusflow.databinding.ActivityPermissionBinding

class GetStartedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGetStartedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetStartedBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnStart.setOnClickListener {
            startActivity(Intent(this, PermissionActivity::class.java))
            finish()
        }
    }
}