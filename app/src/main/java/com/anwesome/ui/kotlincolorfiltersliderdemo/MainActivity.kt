package com.anwesome.ui.kotlincolorfiltersliderdemo

import android.graphics.BitmapFactory
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesome.ui.colorfiltersliderkotlin.ColorFilterSlider

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var bitmap = BitmapFactory.decodeResource(resources,R.drawable.ozil)
        ColorFilterSlider.create(this,bitmap, arrayOf(Color.GREEN,Color.CYAN,Color.BLACK,Color.BLUE,Color.RED,Color.MAGENTA))
    }
}
