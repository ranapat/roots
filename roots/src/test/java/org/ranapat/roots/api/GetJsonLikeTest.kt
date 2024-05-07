package org.ranapat.roots.api

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.exc.ValueInstantiationException
import io.reactivex.rxjava3.observers.TestObserver
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
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
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class GetJsonLikeTest {
    private class ApiResponse(
        @JsonProperty("status") val status: String,
        @JsonProperty("response") val response: String
    )

    @Test
    fun `shall get from json - case 1`() {
        val server = MockWebServer()
        val baseUrl = server.url("")
        val response = "{\"status\": \"ok\",\"response\": \"good\"}"

        server.enqueue(
            MockResponse().setBody(response)
        )

        val testObserver: TestObserver<ApiResponse> = Get.json(baseUrl.toString(), ApiResponse::class.java).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.status, `is`(equalTo("ok")))
        assertThat(result.response, `is`(equalTo("good")))

        server.shutdown()
    }

    @Test
    fun `shall get from json - case 2`() {
        val responseBody: ResponseBody = mock {
            on { string() } doAnswer { _ ->
                "{\"status\": \"ok\",\"response\": \"good\"}"
            }
        }
        val response: Response = mock {
            on { isSuccessful } doReturn true
            on { body } doReturn responseBody
        }
        val call: Call = mock {
            on { execute() } doAnswer { _ ->
                response
            }
        }
        val okHttpClient: OkHttpClient = mock {
            on { newCall(any<Request>()) } doAnswer { _ ->
                call
            }
        }

        val testObserver: TestObserver<ApiResponse> = Get.json(
            "https://localhost", ApiResponse::class.java,
            okHttpClient = okHttpClient
        ).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.status, `is`(equalTo("ok")))
        assertThat(result.response, `is`(equalTo("good")))
    }

    @Test
    fun `shall get from json - case 3`() {
        val responseBody: ResponseBody = mock {
            on { string() } doAnswer { _ ->
                "{\"status\": \"ok\",\"response\": \"good\"}"
            }
        }
        val response: Response = mock {
            on { isSuccessful } doReturn true
            on { body } doReturn responseBody
        }
        val call: Call = mock {
            on { execute() } doAnswer { _ ->
                response
            }
        }
        val okHttpClient: OkHttpClient = mock {
            on { newCall(any<Request>()) } doAnswer { _ ->
                call
            }
        }

        val testObserver: TestObserver<ApiResponse> = Get.json(
            "https://localhost", ApiResponse::class.java,
            okHttpClient = okHttpClient,
            normaliseResponse = object : NormaliseResponse<ApiResponse> {
                override fun invoke(response: Response): ApiResponse {
                    val json = JSONObject(response.body?.string() ?: "")
                    return ApiResponse(
                        "wow-" + json.getString("status"),
                        "wow-" + json.getString("response")
                    )
                }
            }
        ).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.status, `is`(equalTo("wow-ok")))
        assertThat(result.response, `is`(equalTo("wow-good")))
    }

    @Test
    fun `shall not get from json - case 1`() {
        val server = MockWebServer()
        val baseUrl = server.url("")
        val response = "{\"status\": \"ok\",\"response\": \"good\"}"

        server.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(response)
        )

        val testObserver: TestObserver<ApiResponse> = Get.json(baseUrl.toString(), ApiResponse::class.java).test()
        testObserver.await()

        testObserver.assertValueCount(0)
        testObserver.assertError(RequestNotSuccessfulException::class.java)

        server.shutdown()
    }

    @Test
    fun `shall not get from json - case 2`() {
        val server = MockWebServer()
        val baseUrl = server.url("")
        val response = "{\"_status\": \"ok\",\"response\": \"good\"}"

        server.enqueue(
            MockResponse().setBody(response)
        )

        val testObserver: TestObserver<ApiResponse> = Get.json(baseUrl.toString(), ApiResponse::class.java).test()
        testObserver.await()

        testObserver.assertValueCount(0)

        server.shutdown()
    }

    @Test
    fun `shall not get from json - case 3`() {
        val okHttpClient: OkHttpClient = mock {
            on { newCall(any<Request>()) } doAnswer { _ ->
                throw IOException()
            }
        }
        val server = MockWebServer()
        val baseUrl = server.url("")
        val response = "{\"_status\": \"ok\",\"response\": \"good\"}"

        server.enqueue(
            MockResponse().setBody(response)
        )

        val testObserver: TestObserver<ApiResponse> = Get.json(
            baseUrl.toString(), ApiResponse::class.java,
            okHttpClient = okHttpClient
        ).test()
        testObserver.await()

        testObserver.assertValueCount(0)
        testObserver.assertError(IOException::class.java)

        server.shutdown()
    }

    @Test
    fun `shall not get from json - case 4`() {
        val responseRequestUrl: HttpUrl = mock {
            on { toString() } doReturn "http://localhost"
        }
        val responseRequest: Request = mock {
            on { url } doReturn responseRequestUrl
        }
        val response: Response = mock {
            on { code } doReturn 400
            on { isSuccessful } doReturn false
            on { request } doReturn responseRequest
        }
        val call: Call = mock {
            on { execute() } doAnswer { _ ->
                response
            }
        }
        val okHttpClient: OkHttpClient = mock {
            on { newCall(any<Request>()) } doAnswer { _ ->
                call
            }
        }

        val testObserver: TestObserver<ApiResponse> = Get.json(
            "http://localhost", ApiResponse::class.java,
            okHttpClient = okHttpClient
        ).test()
        testObserver.await()

        testObserver.assertValueCount(0)
        testObserver.assertError(RequestNotSuccessfulException::class.java)
    }

    @Test
    fun `shall not get from json - case 5`() {
        val responseRequestUrl: HttpUrl = mock {
            on { toString() } doReturn "http://localhost"
        }
        val responseRequest: Request = mock {
            on { url } doReturn responseRequestUrl
        }
        val response: Response = mock {
            on { isSuccessful } doReturn true
            on { body } doReturn null
            on { request } doReturn responseRequest
        }
        val call: Call = mock {
            on { execute() } doAnswer { _ ->
                response
            }
        }
        val okHttpClient: OkHttpClient = mock {
            on { newCall(any<Request>()) } doAnswer { _ ->
                call
            }
        }

        val testObserver: TestObserver<ApiResponse> = Get.json(
            "https://localhost", ApiResponse::class.java,
            okHttpClient = okHttpClient,
            normaliseResponse = object : NormaliseResponse<ApiResponse> {
                override fun invoke(response: Response): ApiResponse {
                    val json = JSONObject(response.body?.string() ?: "")
                    return ApiResponse(
                        "wow-" + json.getString("status"),
                        "wow-" + json.getString("response")
                    )
                }
            }
        ).test()
        testObserver.await()

        testObserver.assertValueCount(0)
        testObserver.assertError(RequestMissingBodyException::class.java)
    }

}