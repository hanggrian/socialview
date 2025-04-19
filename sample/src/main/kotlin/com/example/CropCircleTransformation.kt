package com.example

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Shader
import androidx.core.graphics.createBitmap
import com.squareup.picasso.Transformation
import kotlin.math.min

class CropCircleTransformation : Transformation {
    override fun transform(source: Bitmap): Bitmap {
        val size = min(source.width.toDouble(), source.height.toDouble()).toInt()
        val width = (source.width - size) / 2
        val height = (source.height - size) / 2
        val target = createBitmap(size, size)
        val r = size / 2f
        val paint = Paint()
        paint.isAntiAlias = true
        paint.setShader(BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP))
        if (width != 0 || height != 0) {
            val matrix = Matrix()
            matrix.setTranslate(-width.toFloat(), -height.toFloat())
            paint.shader.setLocalMatrix(matrix)
        }
        Canvas(target).drawCircle(r, r, r, paint)
        source.recycle()
        return target
    }

    override fun key(): String = "CropCircleTransformation()"
}
