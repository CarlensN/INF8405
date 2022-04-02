package com.example.tp2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.tp2.activities.ProfileActivity
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth

class AccountActivity : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        setUpTabs()

        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if(user != null){
            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
        }
    }

    private fun setUpTabs(){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(LoginFragment(), "Login")
        adapter.addFragment(RegisterFragment(), "Register")
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)
    }
}