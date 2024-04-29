package org.ranapat.roots.api

import com.fasterxml.jackson.annotation.JsonProperty
import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class BaseApiTest {
    private class ApiResponse(
        @JsonProperty("status") val status: String,
        @JsonProperty("response") val response: String
    )

    @Test
    fun `shall set headers`() {
        val builder: Request.Builder = mock()

        BaseApi.setHeaders(builder, mapOf(
            "key1" to "value1"
        ))

        verify(builder, times(1)).addHeader(any<String>(), any<String>())
        verify(builder, times(1)).addHeader("key1", "value1")
    }

    @Test
    fun `shall not set headers`() {
        val builder: Request.Builder = mock()

        BaseApi.setHeaders(builder, null)

        verify(builder, times(0)).addHeader(any<String>(), any<String>())
    }

    @Test
    fun `shall ensure successful`() {
        val response: Response = mock {
            on { isSuccessful } doReturn true
        }

        val result = BaseApi.ensureSuccessful(response)

        assertThat(result, `is`(equalTo(response)))
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

        BaseApi.ensureSuccessful(response)
    }

    @Test
    fun `shall get from json - case 1`() {
        val responseBody: ResponseBody = mock {
            on { string() } doAnswer { _ ->
                "{\"status\": \"ok\",\"response\": \"good\"}"
            }
        }
        val response: Response = mock {
            on { body } doReturn responseBody
        }

        val result = BaseApi.toTyped(response, ApiResponse::class.java, null)

        assertThat(result.status, `is`(equalTo("ok")))
        assertThat(result.response, `is`(equalTo("good")))
    }

    @Test
    fun `shall get from json - case 2`() {
        val responseBody: ResponseBody = mock {
            on { string() } doAnswer { _ ->
                "{\"status\": \"ok\",\"response\": \"good\"}"
            }
        }
        val response: Response = mock {
            on { body } doReturn responseBody
        }

        val result = BaseApi.toTyped(
            response, ApiResponse::class.java,
            object : NormaliseResponse<ApiResponse> {
                override fun invoke(response: Response): ApiResponse {
                    val json = JSONObject(response.body?.string() ?: "")
                    return ApiResponse(
                        "wow-" + json.getString("status"),
                        "wow-" + json.getString("response")
                    )
                }
            }
        )

        assertThat(result.status, `is`(equalTo("wow-ok")))
        assertThat(result.response, `is`(equalTo("wow-good")))
    }

    @Test(expected = RequestMissingBodyException::class)
    fun `shall not get from json - case 1`() {
        val responseRequestUrl: HttpUrl = mock {
            on { toString() } doReturn "http://localhost"
        }
        val responseRequest: Request = mock {
            on { url } doReturn responseRequestUrl
        }
        val response: Response = mock {
            on { body } doReturn null
            on { request } doReturn responseRequest
        }

        BaseApi.toTyped(
            response, ApiResponse::class.java,
            object : NormaliseResponse<ApiResponse> {
                override fun invoke(response: Response): ApiResponse {
                    val json = JSONObject(response.body?.string() ?: "")
                    return ApiResponse(
                        "wow-" + json.getString("status"),
                        "wow-" + json.getString("response")
                    )
                }
            }
        )
    }

}