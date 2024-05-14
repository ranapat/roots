package org.ranapat.roots.api

import okhttp3.Headers
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

@RunWith(MockitoJUnitRunner::class)
class ResponseToolsTest {

    @Test
    fun `shall get encoding from response - case 1`() {
        val responseHeaders: Headers = mock {
            on { get("Content-Encoding") } doReturn "utf-8"
        }
        val response: Response = mock {
            on { headers } doReturn responseHeaders
        }

        val encoding = ResponseTools.getEncoding(response)

        assertThat(encoding, `is`(equalTo(Charsets.UTF_8)))
    }

    @Test
    fun `shall get encoding from response - case 2`() {
        val responseHeaders: Headers = mock {
            on { get("Content-Type") } doReturn "application/json; charset=utf-8"
        }
        val response: Response = mock {
            on { headers } doReturn responseHeaders
        }

        val encoding = ResponseTools.getEncoding(response)

        assertThat(encoding, `is`(equalTo(Charsets.UTF_8)))
    }

    @Test
    fun `shall not get encoding from response - case 1`() {
        val responseHeaders: Headers = mock {
            on { get("Content-Encoding") } doReturn "undefined"
        }
        val response: Response = mock {
            on { headers } doReturn responseHeaders
        }

        val encoding = ResponseTools.getEncoding(response)

        assertThat(encoding, `is`(equalTo(null)))
    }

    @Test
    fun `shall not get encoding from response - case 2`() {
        val responseHeaders: Headers = mock {
            on { get("Content-Encoding") } doReturn null
        }
        val response: Response = mock {
            on { headers } doReturn responseHeaders
        }

        val encoding = ResponseTools.getEncoding(response)

        assertThat(encoding, `is`(equalTo(null)))
    }

    @Test
    fun `shall not get encoding from response - case 3`() {
        val responseHeaders: Headers = mock {
            on { get("Content-Type") } doReturn "application/json; charset=undefined"
        }
        val response: Response = mock {
            on { headers } doReturn responseHeaders
        }

        val encoding = ResponseTools.getEncoding(response)

        assertThat(encoding, `is`(equalTo(null)))
    }

    @Test
    fun `shall not get encoding from response - case 4`() {
        val responseHeaders: Headers = mock {
            on { get("Content-Type") } doReturn "application/json;"
        }
        val response: Response = mock {
            on { headers } doReturn responseHeaders
        }

        val encoding = ResponseTools.getEncoding(response)

        assertThat(encoding, `is`(equalTo(null)))
    }

    @Test
    fun `shall not get encoding from response - case 5`() {
        val responseHeaders: Headers = mock {
            on { get("Content-Type") } doReturn "undefined"
        }
        val response: Response = mock {
            on { headers } doReturn responseHeaders
        }

        val encoding = ResponseTools.getEncoding(response)

        assertThat(encoding, `is`(equalTo(null)))
    }

    @Test
    fun `shall not get encoding from response - case 6`() {
        val responseHeaders: Headers = mock {
            on { get("Content-Type") } doReturn null
        }
        val response: Response = mock {
            on { headers } doReturn responseHeaders
        }

        val encoding = ResponseTools.getEncoding(response)

        assertThat(encoding, `is`(equalTo(null)))
    }

    @Test
    fun `shall not get encoding from response - case 7`() {
        val responseHeaders: Headers = mock {
            on { get(any<String>()) } doReturn null
        }
        val response: Response = mock {
            on { headers } doReturn responseHeaders
        }

        val encoding = ResponseTools.getEncoding(response)

        assertThat(encoding, `is`(equalTo(null)))
    }

}