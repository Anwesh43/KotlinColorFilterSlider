package com.anwesome.ui.colorfiltersliderkotlin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 04/08/17.
 */
class ColorFilterSlider(ctx:Context,bitmap:Bitmap,colors:Array<Int>):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    override fun onDraw(canvas:Canvas) {

    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        when(event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
    data class Screen(var w:Int,var x:Float = 0.0f,var prevX:Float = 0.0f) {
        fun update(scale:Float) {
            x = prevX - w*scale
        }
        fun handleStopCondition() {
            prevX = x
        }
    }
    data class ColorFilterSliderBitmap(var bitmap:Bitmap,var colors:Array<Int>,var w:Int,var h:Int) {
        var screen:Screen?=null
        init {
            screen = Screen(w)
        }
        fun handleStopCondition() {
            screen?.handleStopCondition()
        }
        fun draw(canvas:Canvas,paint:Paint) {
            canvas.drawBitmap(bitmap,0.0f,0.0f,paint)
            canvas.save()
            canvas.translate(screen?.x?:0.0f,0.0f)
            var x = 0.0f
            colors.forEach { color ->
                paint.color = color
                canvas.drawRect(RectF(x,0.0f,x+w.toFloat(),h.toFloat()),paint)
                x += w
            }
            canvas.restore()
        }
        fun update(scale:Float) {
            screen?.update(scale)
        }
    }
}