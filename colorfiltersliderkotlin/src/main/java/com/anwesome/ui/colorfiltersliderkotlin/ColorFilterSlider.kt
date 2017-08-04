package com.anwesome.ui.colorfiltersliderkotlin

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

/**
 * Created by anweshmishra on 04/08/17.
 */
class ColorFilterSlider(ctx:Context,var bitmap:Bitmap,var colors:Array<Int>):View(ctx) {
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
    class GestureHandler(var renderer:Renderer):GestureDetector.SimpleOnGestureListener() {
        override fun onDown(event:MotionEvent):Boolean {
            return true
        }
        override fun onSingleTapUp(event:MotionEvent):Boolean {
            return true
        }
        override fun onFling(e1:MotionEvent,e2:MotionEvent,velx:Float,vely:Float):Boolean {
            if(Math.abs(velx)>Math.abs(vely)) {
                renderer.handleSwipe(((e2.x-e1.x)/Math.abs(e2.x-e1.x)).toInt())
            }
            return true
        }
    }
    class Renderer {
        var time = 0
        var cfmb:ColorFilterSliderBitmap?=null
        var animationHandler:AnimationHandler?=null
        fun render(canvas:Canvas,paint:Paint,v:ColorFilterSlider) {
            if(time == 0) {
                cfmb = ColorFilterSliderBitmap(v.bitmap,v.colors,canvas.width,canvas.height)
                animationHandler = AnimationHandler(cfmb)
            }
            cfmb?.draw(canvas,paint)
            time++
        }
        fun handleSwipe(dir:Int) {
            animationHandler?.start(dir)
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
    class AnimationHandler(var cfmb:ColorFilterSliderBitmap?):AnimatorListenerAdapter(),ValueAnimator.AnimatorUpdateListener {
        var startAnim = ValueAnimator.ofFloat(0.0f,1.0f)
        var endAnim = ValueAnimator.ofFloat(1.0f,0.0f)
        var animated = false
        override fun onAnimationUpdate(animator:ValueAnimator) {
            cfmb?.update(animator.animatedValue as Float)
        }
        override fun onAnimationEnd(animator:Animator) {
            if(animated) {
                cfmb?.handleStopCondition()
                animated = false
            }
        }
        fun start(dir:Int) {
            if(!animated) {
                when (dir) {
                    1 -> {
                        startAnim.start()
                    }
                    -1 -> {
                        endAnim.start()
                    }
                }
                animated = true
            }
        }
    }
}