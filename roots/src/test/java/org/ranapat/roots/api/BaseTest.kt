package org.ranapat.roots.api

import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.Response
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class BaseTest {
    @Test
    fun `shall set headers`() {
        val builder: Request.Builder = mock()

        val instance = object : Base() {
            fun doSetHeaders(builder: Request.Builder, headers: Map<String, String>?) {
                setHeaders(builder, headers)
            }
        }

        instance.doSetHeaders(builder, mapOf(
            "key1" to "value1"
        ))

        verify(builder, times(1)).addHeader(any<String>(), any<String>())
        verify(builder, times(1)).addHeader("key1", "value1")
    }

    @Test
    fun `shall not set headers`() {
        val builder: Request.Builder = mock()

        val instance = object : Base() {
            fun doSetHeaders(builder: Request.Builder, headers: Map<String, String>?) {
                setHeaders(builder, headers)
            }
        }

        instance.doSetHeaders(builder, null)

        verify(builder, times(0)).addHeader(any<String>(), any<String>())
    }

    @Test
    fun `shall ensure successful`() {
        val response: Response = mock {
            on { isSuccessful } doReturn true
        }

        val instance = object : Base() {
            fun doEnsureSuccessful(response: Response): Response {
                return ensureSuccessful(response)
            }
        }

        val result = instance.doEnsureSuccessful(response)

        assertThat(result, `is`(equalTo(response)))
        verify(response, times(0)).close()
    }

    @Test(expected = RequestNotSuccessfulException::class)
    fun `shall not ensure successful`() {
        val responseRequestUrl: HttpUrl = mock {
            on { toString() } doReturn "http://localhost"
        }
        val responseRequest: Request = mock {
            on { url } doReturn responseRequestUrl
        }
        val response: Response = mock {
            on { isSuccessful } doReturn false
            on { request } doReturn responseRequest
        }

        val instance = object : Base() {
            fun doEnsureSuccessful(response: Response): Response {
                return ensureSuccessful(response)
            }
        }

        instance.doEnsureSuccessful(response)

        verify(response, times(1)).close()
    }
}