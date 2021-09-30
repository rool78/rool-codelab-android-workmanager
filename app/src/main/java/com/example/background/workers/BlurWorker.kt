package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.KEY_IMAGE_URI

class BlurWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {
        val appContext = applicationContext

        val resourceUri = inputData.getString(KEY_IMAGE_URI)

        makeStatusNotification("Blurring image", appContext)

        sleep()

        return try {
//            val picture = BitmapFactory.decodeResource(appContext.resources, R.drawable.test)

            if (TextUtils.isEmpty(resourceUri)) {
                println("Invalid input uri")
                throw IllegalArgumentException("Invalid input uri")
            }

            val resolver = appContext.contentResolver

            val picture = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri)))

            val output = blurBitmap(picture, appContext)

            val outputUri = writeBitmapToFile(applicationContext, output)

            makeStatusNotification("Output uri -> $outputUri", appContext)

            val builder = Data.Builder()
            val outputData = builder.putString(KEY_IMAGE_URI, outputUri.toString())
                .build()

            Result.success(outputData)

        } catch (throwable: Throwable) {
            println("Error applying blur")
            Result.failure()

        }
    }
}