package com.example.sharedcode

import android.graphics.Bitmap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual typealias Image = Bitmap

internal actual val ApplicationDispatcher: CoroutineDispatcher = Dispatchers.Default
