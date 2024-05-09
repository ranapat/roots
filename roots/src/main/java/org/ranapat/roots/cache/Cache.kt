package org.ranapat.roots.cache

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.FileOutputStream
import java.nio.charset.Charset

object Cache : Base() {
    fun to(
        url: String, content: String,
        charset: Charset? = null
    ): Maybe<CacheResult> {
        return Maybe
            .fromCallable {
                val normalisedCharset = charset ?: Charsets.UTF_8
                val file = ensureCacheFile(url)

                var success = false

                if (file != null) {
                    var fileOutputStream: FileOutputStream? = null
                    try {
                        fileOutputStream = FileOutputStream(file).also {
                            it.write(content.toByteArray(normalisedCharset))
                            it.close()
                        }
                        success = true
                    } catch (e: Exception) {
                        success = false
                    } finally {
                        fileOutputStream?.close()
                    }
                }

                return@fromCallable CacheResult(
                    success,
                    file?.absolutePath,
                    content, normalisedCharset
                )
            }
            .subscribeOn(Schedulers.io())
    }
}