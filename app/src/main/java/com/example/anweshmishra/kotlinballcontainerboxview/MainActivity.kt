package com.example.anweshmishra.kotlinballcontainerboxview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.ballcontainerboxview.BallContainerBoxView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BallContainerBoxView.create(this)
    }
}
