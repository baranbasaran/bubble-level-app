package com.baran.assignment03

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
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

    private lateinit var minRollTextView: TextView
    private lateinit var maxRollTextView: TextView
    private lateinit var minPitchTextView: TextView
    private lateinit var maxPitchTextView: TextView
    private var minRoll = Float.MAX_VALUE
    private var maxRoll = Float.MIN_VALUE
    private var minPitch = Float.MAX_VALUE
    private var maxPitch = Float.MIN_VALUE
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
        minRollTextView = findViewById(R.id.min_roll_text_view)
        maxRollTextView = findViewById(R.id.max_roll_text_view)
        minPitchTextView = findViewById(R.id.min_pitch_text_view)
        maxPitchTextView = findViewById(R.id.max_pitch_text_view)
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        // Unregister listeners to save battery
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        // Update readings for either the accelerometer or magnetometer
        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            accelerometerReading = event.values
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            magnetometerReading = event.values
        }
        // Calculate orientation angles based on the latest readings
        updateOrientationAngles()

        // Add the current sensor data to the collector
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
        val xAngle = Math.toDegrees(orientationAngles[2].toDouble()).toFloat()
        val yAngle = Math.toDegrees(orientationAngles[1].toDouble()).toFloat()

        // Update the 2D bubble view with the roll and pitch values.
        twoDBubbleLevelView.xAngle = xAngle
        twoDBubbleLevelView.yAngle = yAngle

        // Update minimum and maximum values
        if (xAngle < minRoll) {
            minRoll = xAngle
            minRollTextView.text = minRoll.toString()
        }
        if (xAngle > maxRoll) {
            maxRoll = xAngle
            maxRollTextView.text = maxRoll.toString()
        }
        if (yAngle < minPitch) {
            minPitch = yAngle
            minPitchTextView.text = minPitch.toString()
        }
        if (yAngle > maxPitch) {
            maxPitch = yAngle
            maxPitchTextView.text = maxPitch.toString()
        }
        // Update the 2D bubble view with the roll and pitch values.



    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}