package com.example.myapplication

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

import kotlin.io.path.Path
import kotlin.io.path.moveTo
import kotlin.math.cos
import kotlin.math.sin


class MainActivity : ComponentActivity() {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null
    private var accelerometerReading = FloatArray(3)
    private var magnetometerReading = FloatArray(3)
    private var rotationMatrix = FloatArray(9)
    private var orientationAngles = FloatArray(3)
    private var rotationAngle = 0f // Ángulo de rotación del triángulo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                MainScreen()
            }
        }
    }


}

@Composable
fun MainScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Button(onClick = { /* Acción del botón */ }) {
            Text(text = "Fijar")
        }
        DrawTriangle()
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun DrawTriangle() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val size = size.width
        val padding = 16f

        val x1 = padding
        val y1 = size - padding
        val x2 = size / 2f
        val y2 = padding
        val x3 = size - padding
        val y3 = size - padding

        // Coordenadas originales del triángulo
        val triangleCoords = floatArrayOf(
            x1, y1,
            x2, y2,
            x3, y3
        )

        // Ángulo de rotación (en radianes) para mantener la orientación fija
        val rotationAngle = 45f // Aquí puedes ajustar el ángulo de rotación deseado

        // Aplicar la rotación usando matrices
        val rotatedCoords = rotateTriangle(triangleCoords, rotationAngle)

        // Dibujar el triángulo rotado
        drawLine(Color.Black, Offset(rotatedCoords[0], rotatedCoords[1]), Offset(rotatedCoords[2], rotatedCoords[3]))
        drawLine(Color.Black, Offset(rotatedCoords[2], rotatedCoords[3]), Offset(rotatedCoords[4], rotatedCoords[5]))
        drawLine(Color.Black, Offset(rotatedCoords[4], rotatedCoords[5]), Offset(rotatedCoords[0], rotatedCoords[1]))
    }
}

/**
 * Función para rotar las coordenadas de un triángulo alrededor del centro.
 * @param triangleCoords Coordenadas originales del triángulo en el formato [x1, y1, x2, y2, x3, y3].
 * @param angle Ángulo de rotación en grados.
 * @return Coordenadas rotadas del triángulo en el mismo formato.
 */
fun rotateTriangle(triangleCoords: FloatArray, angle: Float): FloatArray {
    val angleRad = Math.toRadians(angle.toDouble()).toFloat()
    val centerX = (triangleCoords[0] + triangleCoords[2] + triangleCoords[4]) / 3
    val centerY = (triangleCoords[1] + triangleCoords[3] + triangleCoords[5]) / 3

    val rotatedCoords = FloatArray(6)
    for (i in 0 until 3) {
        val x = triangleCoords[2 * i]
        val y = triangleCoords[2 * i + 1]
        val dx = x - centerX
        val dy = y - centerY
        rotatedCoords[2 * i] = (centerX + dx * cos(angleRad) - dy * sin(angleRad)).toFloat()
        rotatedCoords[2 * i + 1] = (centerY + dx * sin(angleRad) + dy * cos(angleRad)).toFloat()
    }
    return rotatedCoords
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        MainScreen()
    }
}