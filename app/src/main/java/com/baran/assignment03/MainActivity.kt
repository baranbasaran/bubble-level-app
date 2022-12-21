package com.baran.assignment03

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var magnetometer: Sensor
    private var sensorDataCollector = SensorDataCollector(500)
    private lateinit var minValueTextView : TextView
    private lateinit var maxValueTextView : TextView

    private var accelerometerReading = floatArrayOf(0f, 0f, 0f)
    private var magnetometerReading = floatArrayOf(0f, 0f, 0f)
    private lateinit var oneDBubbleLevelView : OneDBubbleView
    private lateinit var twoDBubbleLevelView : TwoDBubbleView

    private var rotationMatrix = FloatArray(9)
    private var orientationAngles = FloatArray(3)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        oneDBubbleLevelView = findViewById(R.id.oneDBubble_level_view)
        twoDBubbleLevelView = findViewById(R.id.twoDBubble_level_view)
        minValueTextView = findViewById(R.id.min_value_text_view)
        maxValueTextView =  findViewById(R.id.max_value_text_view)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {

        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            accelerometerReading = event.values
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            magnetometerReading = event.values
        }
        updateOrientationAngles()
        sensorDataCollector.addSensorData(event)
        val minValue = sensorDataCollector.getMinValue()
        val maxValue = sensorDataCollector.getMaxValue()
        minValueTextView.text = minValue.toString()
        maxValueTextView.text = maxValue.toString()
    }
    private fun updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)

        // "orientationAngles" now has up-to-date information.
        SensorManager.getOrientation(rotationMatrix, orientationAngles)

        oneDBubbleLevelView.angle= Math.toDegrees(orientationAngles[1].toDouble()).toFloat()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}