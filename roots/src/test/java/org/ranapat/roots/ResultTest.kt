package org.ranapat.roots

import okhttp3.MediaType.Companion.toMediaType
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.lang.NullPointerException
import java.util.Date

@RunWith(MockitoJUnitRunner::class)
class ResultTest {

    @Test
    fun `shall construct - case 1`() {
        val time = Date().time
        val result = Result(
            Result.Source.API,
            true,
            time, "location",
            "plain/text; charset=utf-8".toMediaType(),
            "content"
        )

        assertThat(result.source, `is`(equalTo(Result.Source.API)))
        assertThat(result.success, `is`(equalTo(true)))
        assertThat(result.lastModified, `is`(equalTo(time)))
        assertThat(result.location, `is`(equalTo("location")))
        assertThat(result.mediaType, `is`(equalTo("plain/text; charset=utf-8".toMediaType())))

        assertThat(result.content, `is`(equalTo("content")))
        assertThat(result.contentOrNull<String>(), `is`(equalTo("content")))
        assertThat(result.contentValue<String>(), `is`(equalTo("content")))
    }

    @Test
    fun `shall construct - case 2`() {
        val time = Date().time
        val result = Result(
            Result.Source.API,
            true,
            time, "location",
            "plain/text; charset=utf-8".toMediaType(),
            123
        )

        assertThat(result.source, `is`(equalTo(Result.Source.API)))
        assertThat(result.success, `is`(equalTo(true)))
        assertThat(result.lastModified, `is`(equalTo(time)))
        assertThat(result.location, `is`(equalTo("location")))
        assertThat(result.mediaType, `is`(equalTo("plain/text; charset=utf-8".toMediaType())))

        assertThat(result.content, `is`(equalTo(123)))
        assertThat(result.contentOrNull<Int>(), `is`(equalTo(123)))
        assertThat(result.contentValue<Int>(), `is`(equalTo(123)))
    }

    @Test(expected = NullPointerException::class)
    fun `shall throw on null - case 1`() {
        val time = Date().time
        val result = Result(
            Result.Source.API,
            true,
            time, "location",
            "plain/text; charset=utf-8".toMediaType(),
            null
        )

        assertThat(result.source, `is`(equalTo(Result.Source.API)))
        assertThat(result.success, `is`(equalTo(true)))
        assertThat(result.lastModified, `is`(equalTo(time)))
        assertThat(result.location, `is`(equalTo("location")))
        assertThat(result.mediaType, `is`(equalTo("plain/text; charset=utf-8".toMediaType())))

        assertThat(result.content, `is`(equalTo(null)))
        assertThat(result.contentOrNull<Int>(), `is`(equalTo(null)))
        result.contentValue<Int>().let {
            //
        }
    }

    @Test
    fun `shall construct exception - case 1`() {
        val exception = Result.TypeNotImplementedException()

        assertThat(exception.message, `is`(equalTo("Type not implemented")))
    }

}