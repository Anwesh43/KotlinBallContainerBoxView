package com.example.ballcontainerboxview

/**
 * Created by anweshmishra on 18/05/18.
 */

import android.content.*
import android.graphics.*
import android.view.View
import android.view.MotionEvent

class BallContainerBoxView (ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var prevScale : Float = 0f, var dir : Float = 0f, var j : Int = 0) {

        val scales : Array<Float> = arrayOf(0f, 0f, 0f, 0f)

        fun update(stopcb : (Float) -> Unit) {
            scales[j] += 0.1f * dir
            if (Math.abs(scales[j] - prevScale) > 1) {
                scales[j] = prevScale + dir
                j += dir.toInt()
                if (j == scales.size || j == -1) {
                    j -= dir.toInt()
                    dir = 0f
                    prevScale = scales[j]
                    stopcb(prevScale)
                }
            }
        }

        fun startUpdating(startcb  : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }

    data class Animator (var view : View, var animated : Boolean = false) {

        fun animate(animatecb : () -> Unit) {
            if (animated) {
                animatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch (ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class BallContainerBox (var i : Int, val state : State = State()) {

        fun draw(canvas : Canvas, paint : Paint) {
            val w : Float = canvas.width.toFloat()
            val h : Float = canvas.height.toFloat()
            val size : Float = Math.min(w, h)/5
            val r : Float = Math.min(w, h) / 15
            canvas.save()
            canvas.translate(w/2, 4 * h/5)
            paint.color = Color.parseColor("#e74c3c")
            canvas.drawCircle(0f, -0.8f * h * state.scales[2], r * state.scales[1], paint)
            paint.color = Color.parseColor("#2ecc71")
            canvas.drawLine(-size/2, -size/2, -size/2, size/2, paint)
            canvas.drawLine(-size/2, size/2, size/2, size/2, paint)
            canvas.drawLine(size/2, size/2, size/2, -size/2, paint)
            canvas.save()
            canvas.translate(-size/2, -size/2)
            canvas.rotate(90f * (state.scales[0] + 1 - state.scales[3]))
            canvas.drawLine(0f, 0f, size, 0f, paint)
            canvas.restore()
            canvas.restore()
        }

        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }

        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }
    }

    data class Renderer(var view : BallContainerBoxView) {

        private val animator : Animator = Animator(view)

        private val ballContainerBox : BallContainerBox = BallContainerBox(0)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            ballContainerBox.draw(canvas, paint)
            animator.animate {
                ballContainerBox.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            ballContainerBox.startUpdating {
                animator.start()
            }
        }
    }
}