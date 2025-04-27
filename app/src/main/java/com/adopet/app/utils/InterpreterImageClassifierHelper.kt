package com.adopet.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import java.nio.ByteBuffer
import java.nio.ByteOrder
import androidx.core.graphics.scale

class InterpreterImageClassifierHelper(
    context: Context,
    modelName: String = "model_baru.tflite",
    private val listener: ResultListener
) {

    private var interpreter: Interpreter
    private val labels: List<String> = FileUtil.loadLabels(context, "labels.txt")

    private val imageSize = 224
    private val numClasses = 10

    init {
        val model = FileUtil.loadMappedFile(context, modelName)
        interpreter = Interpreter(model)
    }

    fun classify(bitmap: Bitmap) {
        val resized = bitmap.scale(imageSize, imageSize)
        val inputBuffer = convertBitmapToByteBuffer(resized)

        val output = Array(1) { FloatArray(numClasses) }

        val start = SystemClock.uptimeMillis()
        interpreter.run(inputBuffer, output)
        val inferenceTime = SystemClock.uptimeMillis() - start

        val results = output[0]
            .mapIndexed { index, score -> index to score }
            .sortedByDescending { it.second }
            .take(3)
            .map { labels[it.first] to it.second }

        listener.onResults(results, inferenceTime)
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val buffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3)
        buffer.order(ByteOrder.nativeOrder())
        val pixels = IntArray(imageSize * imageSize)
        bitmap.getPixels(pixels, 0, imageSize, 0, 0, imageSize, imageSize)

        for (pixel in pixels) {
            val r = (pixel shr 16 and 0xFF) / 255.0f
            val g = (pixel shr 8 and 0xFF) / 255.0f
            val b = (pixel and 0xFF) / 255.0f
            buffer.putFloat(r)
            buffer.putFloat(g)
            buffer.putFloat(b)
        }

        buffer.rewind()
        return buffer
    }

    interface ResultListener {
        fun onResults(results: List<Pair<String, Float>>, inferenceTime: Long)
        fun onError(error: String)
    }
}