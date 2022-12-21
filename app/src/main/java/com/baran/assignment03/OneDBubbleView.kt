package com.baran.assignment03
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class OneDBubbleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val bubblePaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }
    private val linePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 3f
    }
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 36f
    }

    // angle at which the bubble is drawn
    var angle: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // get the center x and y coordinates of the view
        val centerX = width / 2f
        val centerY = height / 2f

        // draw the line
        canvas.drawLine(centerX - 100, centerY, centerX + 100, centerY, linePaint)

        // calculate the x coordinate of the bubble based on the angle
        val bubbleRadius = 50f
        val bubbleX = centerX + angle / 20 * 100

        // draw the bubble
        canvas.drawCircle(bubbleX, centerY, bubbleRadius, bubblePaint)

        // Draw angle text
        val text = "${angle.toInt()}°"
        val textWidth = textPaint.measureText(text)
        canvas.drawText(text, centerX - textWidth / 2, centerY - bubbleRadius - 10, textPaint)
    }
}
