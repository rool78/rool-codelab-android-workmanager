package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.background.R

class BlurWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {
        val appContext = applicationContext

        makeStatusNotification("Blurring image", appContext)

        return try {
            val picture = BitmapFactory.decodeResource(appContext.resources, R.drawable.test)

            val blurPicture = blurBitmap(picture, appContext)

            val uri = writeBitmapToFile(applicationContext, blurPicture)

            makeStatusNotification("Output uri -> $uri", appContext)

            Result.success()

        } catch (throwable: Throwable) {
            println("Error applying blur")
            Result.failure()

        }
    }
}