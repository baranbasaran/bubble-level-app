package com.baran.assignment03


import android.hardware.Sensor
import android.hardware.SensorEvent
import kotlin.math.max
import kotlin.math.min

class SensorDataCollector(private val maxValues: Int) {

    private val accelerometerValues = mutableListOf<Float>()
    private val magnetometerValues = mutableListOf<Float>()

    fun addSensorData(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            addValues(accelerometerValues, event.values)
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            addValues(magnetometerValues, event.values)
        }
    }

    private fun addValues(values: MutableList<Float>, newValues: FloatArray) {
        values.addAll(newValues.asIterable())
        while (values.size > maxValues) {
            values.removeAt(0)
        }
    }

    fun getMinValue(): Float {
        var minMagnetometerValue: Float? = null
        val minAccelerometerValue = accelerometerValues.min()
        if(magnetometerValues.size > 0){
            minMagnetometerValue = magnetometerValues.min()
        }

        minMagnetometerValue?.let {
            return min(minAccelerometerValue,it)
        }
     return minAccelerometerValue
    }
    fun getMaxValue(): Float {
        val maxAccelerometerValue = accelerometerValues.max()
        var maxMagnetometerValue: Float? = null
        if(magnetometerValues.size > 0){

            maxMagnetometerValue = magnetometerValues.max()
        }
        maxMagnetometerValue?.let {
            return max(maxAccelerometerValue,it)
        }
        return maxAccelerometerValue
    }

    fun getSize(): Int {
        return accelerometerValues.size
    }

}
