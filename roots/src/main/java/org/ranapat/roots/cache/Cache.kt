package org.ranapat.roots.cache

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
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
                    file?.lastModified(),
                    file?.absolutePath,
                    content, normalisedCharset,
                    CacheResult.Type.TEXT
                )
            }
            .subscribeOn(Schedulers.io())
    }

    fun from(
        url: String,
        charset: Charset? = null
    ): Maybe<CacheResult> {
        return Maybe
            .fromCallable {
                val normalisedCharset = charset ?: Charsets.UTF_8
                val file = ensureCacheFile(url)

                var success = false
                var content: String? = null

                if (file != null) {
                    var fileInputStream: FileInputStream? = null
                    var inputStreamReader: InputStreamReader? = null
                    var bufferedReader: BufferedReader? = null

                    try {
                        val stringBuilder = StringBuilder()

                        fileInputStream = FileInputStream(file)
                        inputStreamReader = InputStreamReader(fileInputStream, normalisedCharset)
                        bufferedReader = BufferedReader(inputStreamReader)

                        var line: String?
                        while ((bufferedReader.readLine().also { line = it }) != null) {
                            stringBuilder.append(line)
                        }

                        content = stringBuilder.toString()
                        success = true
                    } catch (exception: Exception) {
                        success = false
                    } finally {
                        fileInputStream?.close()
                        inputStreamReader?.close()
                        bufferedReader?.close()
                    }
                }

                return@fromCallable CacheResult(
                    success,
                    file?.lastModified(),
                    file?.absolutePath,
                    content, normalisedCharset,
                    CacheResult.Type.TEXT
                )
            }
            .subscribeOn(Schedulers.io())
    }
}