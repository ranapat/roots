package org.ranapat.roots.cache

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.schedulers.Schedulers
import org.ranapat.roots.Result
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

object Cache : Base() {
    fun to(
        url: String, content: String,
        charset: Charset? = null
    ): Maybe<Result> {
        return Maybe
            .fromCallable {
                val normalisedCharset = charset ?: Charsets.UTF_8
                val file = ensureCacheFileForWriting(url)

                var success = false

                if (file != null) {
                    var fileOutputStream: FileOutputStream? = null

                    try {
                        fileOutputStream = FileOutputStream(file).also {
                            it.write(content.toByteArray(normalisedCharset))
                        }

                        success = true
                    } catch (e: Exception) {
                        //
                    }

                    fileOutputStream?.close()
                }

                return@fromCallable Result(
                    Result.Type.TEXT,
                    Result.Source.CACHE,
                    success,
                    file?.lastModified(),
                    file?.absolutePath,
                    content, normalisedCharset,
                )
            }
            .subscribeOn(Schedulers.io())
    }

    fun from(
        url: String,
        charset: Charset? = null
    ): Maybe<Result> {
        return Maybe
            .fromCallable {
                val normalisedCharset = charset ?: Charsets.UTF_8
                val file = ensureCacheFileForReading(url)

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
                        //
                    }

                    fileInputStream?.close()
                    inputStreamReader?.close()
                    bufferedReader?.close()
                }

                return@fromCallable Result(
                    Result.Type.TEXT,
                    Result.Source.CACHE,
                    success,
                    file?.lastModified(),
                    file?.absolutePath,
                    content, normalisedCharset,
                )
            }
            .subscribeOn(Schedulers.io())
    }
}