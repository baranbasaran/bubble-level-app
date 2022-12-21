package com.baran.assignment03
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.abs
class TwoDBubbleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val bubblePaint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }
    private val linePaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 4f
    }
    private val northLinePaint = Paint().apply {
        color = Color.RED
        strokeWidth = 4f
    }
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 40f
    }
    // angle at which the bubble is drawn along the x-axis
    var xAngle: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    // angle at which the bubble is drawn along the y-axis
    var yAngle: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    // working range of the bubble level, in degrees
    private val minAngle = -10f
    private val maxAngle = 10f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // get the center x and y coordinates of the view
        val centerX = width / 2f
        val centerY = height / 2f

        // Draw x-axis line
        canvas.drawLine(centerX - 100, centerY, centerX + 100, centerY, linePaint)

        // Draw y-axis line
        canvas.drawLine(centerX, centerY - 100, centerX, centerY + 100, linePaint)

        // clamp the x and y angles to the working range
        val clampedXAngle = xAngle.coerceIn(minAngle, maxAngle)
        val clampedYAngle = yAngle.coerceIn(minAngle, maxAngle)

        // calculate the x and y coordinates of the bubble based on the x and y angles
        val bubbleRadius = 50f
        val bubbleX = centerX + clampedXAngle / 20 * 100
        val bubbleY = centerY + clampedYAngle / 20 * 100
        // draw the bubble
        canvas.drawCircle(bubbleX, bubbleY, bubbleRadius, bubblePaint)

        // draw the x and y angle text
        val xText = "${clampedXAngle.toInt()}°"
        val yText = "${clampedYAngle.toInt()}°"
        val xTextWidth = textPaint.measureText(xText)
        val yTextWidth = textPaint.measureText(yText)
        canvas.drawText(xText, centerX - xTextWidth / 2, centerY - bubbleRadius - 10, textPaint)
        canvas.drawText(yText, centerX + bubbleRadius + 10, centerY - yTextWidth / 2, textPaint)
        // Draw line to represent North
        val northLineLength = 100f
        val northLineEndX = centerX + northLineLength * Math.sin(Math.toRadians(clampedXAngle.toDouble())).toFloat()
        val northLineEndY = centerY - northLineLength * Math.cos(Math.toRadians(clampedXAngle.toDouble())).toFloat()
        canvas.drawLine(centerX, centerY, northLineEndX, northLineEndY, northLinePaint)
    }
}


