package com.anwesome.ui.colorfiltersliderkotlin

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.*
import android.support.v7.app.AppCompatActivity
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager

/**
 * Created by anweshmishra on 04/08/17.
 */
class ColorFilterSlider(var ctx:Context,var bitmap:Bitmap,var colors:Array<Int>):View(ctx) {
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val renderer = Renderer(this)
    val gestureDetector = GestureDetector(ctx,GestureHandler(renderer))
    override fun onDraw(canvas:Canvas) {
        renderer.render(canvas,paint)
    }
    override fun onTouchEvent(event:MotionEvent):Boolean {
        return gestureDetector.onTouchEvent(event)
    }
    data class Screen(var w:Int,var x:Float = 0.0f,var prevX:Float = 0.0f) {
        fun update(scale:Float) {
            x = prevX + w*scale
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
    class Renderer(var v:ColorFilterSlider) {
        var time = 0
        var counter = 0
        var cfmb:ColorFilterSliderBitmap?=null
        var animationHandler:AnimationHandler?=null
        fun render(canvas:Canvas,paint:Paint) {
            if(time == 0) {
                var bitmap = Bitmap.createScaledBitmap(v.bitmap,canvas.width,canvas.height,true)
                cfmb = ColorFilterSliderBitmap(bitmap,v.colors,canvas.width,canvas.height)
                animationHandler = AnimationHandler(cfmb,v)
            }
            cfmb?.draw(canvas,paint)
            time++
        }
        fun handleSwipe(dir:Int) {
            if((dir == -1 && counter < v.colors.size-1) || (dir == 1 && counter > 0)) {
                if(animationHandler?.start(dir)?:false) {
                    counter -= dir
                }
            }
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
                paint.color = Color.argb(120,Color.red(color),Color.green(color),Color.blue(color))
                canvas.drawRect(RectF(x,0.0f,x+w.toFloat(),h.toFloat()),paint)
                x += w
            }
            canvas.restore()
        }
        fun update(scale:Float) {
            screen?.update(scale)
        }
    }
    class AnimationHandler(var cfmb:ColorFilterSliderBitmap?,var v:ColorFilterSlider):AnimatorListenerAdapter(),ValueAnimator.AnimatorUpdateListener {
        var startAnim = ValueAnimator.ofFloat(0.0f,1.0f)
        var endAnim = ValueAnimator.ofFloat(0.0f,-1.0f)
        var animated = false
        init {
            startAnim.addUpdateListener(this)
            endAnim.addUpdateListener(this)
            startAnim.addListener(this)
            endAnim.addListener(this)
            startAnim.duration = 500
            endAnim.duration = 500
        }
        override fun onAnimationUpdate(animator:ValueAnimator) {
            cfmb?.update(animator.animatedValue as Float)
            v.postInvalidate()
        }
        override fun onAnimationEnd(animator:Animator) {
            if(animated) {
                cfmb?.handleStopCondition()
                animated = false
            }
        }
        fun start(dir:Int):Boolean {
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
                return true
            }
            return false
        }
    }
    companion object {
        fun create(activity:Activity,bitmap: Bitmap,colors:Array<Int>) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            if(activity is AppCompatActivity) {
                var appCompatActivity = activity as AppCompatActivity
                var actionBar = appCompatActivity.supportActionBar
                actionBar?.hide()
            }
            else {
                var actionBar = activity.actionBar
                actionBar.hide()
            }
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            var colorFilterSlider = ColorFilterSlider(activity,bitmap,colors)
            activity.setContentView(colorFilterSlider)
        }
    }
}