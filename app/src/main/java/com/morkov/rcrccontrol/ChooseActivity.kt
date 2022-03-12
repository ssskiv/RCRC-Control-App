package com.morkov.rcrccontrol

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class ChooseActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var landscapeButton: Button
    private lateinit var portraitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose)

        landscapeButton=findViewById(R.id.landscape_button)
        portraitButton=findViewById(R.id.portrait_button)

        landscapeButton.setOnClickListener(this)
        portraitButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            landscapeButton->{
                val lma=LandscapeMainActivity()
                val intent=Intent(this,lma.javaClass)
                startActivity(intent)
            }
            portraitButton->{
                val ma=MainActivity()
                val intent=Intent(this,ma.javaClass)
                startActivity(intent)
            }
        }
    }
}