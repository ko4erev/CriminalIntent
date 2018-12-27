package criminalintent.android.mobdev.com.criminalintent

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point

class PictureUtils {

    companion object {
        fun getScaledBitmap(path: String, destWidth: Int, destHeight: Int): Bitmap {
            // Чтение размеров изображения на диске
            var options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, options)

            var srcWidth = options.outWidth
            var srcHeight = options.outHeight

            //Вычисление степени масштабирования
            var inSampleSize = 1
            if (srcHeight > destHeight || srcWidth > destWidth) {
                val heightScale = srcHeight / destHeight
                val widthScale = srcWidth / destWidth
                inSampleSize =
                        if (heightScale > widthScale) {
                            heightScale
                        } else
                            widthScale

            }
            options = BitmapFactory.Options()
            options.inSampleSize = inSampleSize
            // Чтение данных и создание итогового изображения
            return BitmapFactory.decodeFile(path, options)
        }

        fun getScaledBitmap(path: String, activity: Activity): Bitmap {
            val size = Point()
            activity.windowManager.defaultDisplay.getSize(size)
            return getScaledBitmap(path, size.x, size.y)
        }
    }
}