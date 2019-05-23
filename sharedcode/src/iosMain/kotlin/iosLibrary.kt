package com.example.sharedcode

import kotlinx.coroutines.*
import platform.UIKit.UIImage
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_queue_t
import kotlin.coroutines.CoroutineContext

actual typealias Image = UIImage

// The function that gave errors in avatars (some half greyed or blacked out),
// I think it was due to Kotlin's experimental unsigned types ("toULong), and that
// something went wrong in translating the bytearray to cpointer and then to NSData and
// finally to image.
//@ExperimentalUnsignedTypes
//actual fun toPlatformImage(byteArray: ByteArray): Image? {
//    memScoped {
//        val cPointer = byteArray.toCValues().ptr
//        var data = NSData.dataWithBytes(cPointer, byteArray.size.toULong())
//        return UIImage.imageWithData(data)
//    }
//}

internal actual val ApplicationDispatcher: CoroutineDispatcher = NsQueueDispatcher(dispatch_get_main_queue())

internal class NsQueueDispatcher(
    private val dispatchQueue: dispatch_queue_t) : CoroutineDispatcher() {
        override fun dispatch(context: CoroutineContext, block: Runnable) {
        dispatch_async(dispatchQueue) {
            block.run()
        }
    }
}