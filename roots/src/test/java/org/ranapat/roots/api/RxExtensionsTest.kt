package org.ranapat.roots.api

import com.fasterxml.jackson.annotation.JsonProperty
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.observers.TestObserver
import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.ranapat.roots.converter.Converter

@RunWith(MockitoJUnitRunner::class)
class RxExtensionsTest {
    private class ApiResponse(
        @JsonProperty("status") val status: String,
        @JsonProperty("response") val response: String
    )

    @Test
    fun `shall get string - case 1`() {
        val responseBody: ResponseBody = mock {
            on { string() } doAnswer { _ ->
                "{\"status\": \"ok\",\"response\": \"good\"}"
            }
        }
        val response: Response = mock {
            on { body } doReturn responseBody
        }

        val testObserver: TestObserver<String> = Maybe.just(response).string().test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result, `is`(equalTo("{\"status\": \"ok\",\"response\": \"good\"}")))
    }

    @Test
    fun `shall get string - case 0`() {
        val requestResponseUrl: HttpUrl = mock {
            on { toString() } doReturn "url"
        }
        val responseRequest: Request = mock {
            on { url } doReturn requestResponseUrl
            on { method } doReturn "POST"
        }
        val responseBody: ResponseBody = mock {
            on { string() } doAnswer { _ ->
                null
            }
        }
        val response: Response = mock {
            on { body } doReturn responseBody
            on { request } doReturn responseRequest
        }

        val testObserver: TestObserver<String> = Maybe.just(response).string().test()
        testObserver.await()

        testObserver.assertValueCount(0)

        testObserver.assertError(RequestMissingBodyException::class.java)
    }


    @Test
    fun `shall not get string - case 1`() {
        val requestResponseUrl: HttpUrl = mock {
            on { toString() } doReturn "url"
        }
        val responseRequest: Request = mock {
            on { url } doReturn requestResponseUrl
            on { method } doReturn "POST"
        }
        val response: Response = mock {
            on { body } doReturn null
            on { request } doReturn responseRequest
        }

        val testObserver: TestObserver<String> = Maybe.just(response).string().test()
        testObserver.await()

        testObserver.assertValueCount(0)

        testObserver.assertError(RequestMissingBodyException::class.java)
    }

    @Test
    fun `shall not get string - case 2`() {
        val requestResponseUrl: HttpUrl = mock {
            on { toString() } doReturn "url"
        }
        val responseRequest: Request = mock {
            on { url } doReturn requestResponseUrl
            on { method } doReturn "undefined"
        }
        val response: Response = mock {
            on { body } doReturn null
            on { request } doReturn responseRequest
        }

        val testObserver: TestObserver<String> = Maybe.just(response).string().test()
        testObserver.await()

        testObserver.assertValueCount(0)

        testObserver.assertError(RequestMissingBodyException::class.java)
    }

    @Test
    fun `shall get json object - case 1`() {
        val responseBody: ResponseBody = mock {
            on { string() } doAnswer { _ ->
                "{\"status\": \"ok\",\"response\": \"good\"}"
            }
        }
        val response: Response = mock {
            on { body } doReturn responseBody
        }

        val testObserver: TestObserver<JSONObject> = Maybe.just(response).jsonObject().test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.getString("status"), `is`(equalTo("ok")))
        assertThat(result.getString("response"), `is`(equalTo("good")))
    }

    @Test
    fun `shall get json array - case 1`() {
        val responseBody: ResponseBody = mock {
            on { string() } doAnswer { _ ->
                "[{\"status\": \"ok\",\"response\": \"good\"}]"
            }
        }
        val response: Response = mock {
            on { body } doReturn responseBody
        }

        val testObserver: TestObserver<JSONArray> = Maybe.just(response).jsonArray().test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.getJSONObject(0).getString("status"), `is`(equalTo("ok")))
        assertThat(result.getJSONObject(0).getString("response"), `is`(equalTo("good")))
    }

    @Test
    fun `shall get instance - case 1`() {
        val responseBody: ResponseBody = mock {
            on { string() } doAnswer { _ ->
                "{\"status\": \"ok\",\"response\": \"good\"}"
            }
        }
        val response: Response = mock {
            on { body } doReturn responseBody
        }

        val testObserver: TestObserver<ApiResponse> = Maybe.just(response).instance(ApiResponse::class.java).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.status, `is`(equalTo("ok")))
        assertThat(result.response, `is`(equalTo("good")))
    }

    @Test
    fun `shall get instance - case 2`() {
        val responseBody: ResponseBody = mock {
            on { string() } doAnswer { _ ->
                "{\"status\": \"ok\",\"response\": \"good\"}"
            }
        }
        val response: Response = mock {
            on { body } doReturn responseBody
        }

        val testObserver: TestObserver<ApiResponse> = Maybe.just(response).instance(
            object : NormaliseResponse<ApiResponse> {
                override fun invoke(from: String): ApiResponse {
                    return Converter.fromJson(from)
                }
            }
        ).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.status, `is`(equalTo("ok")))
        assertThat(result.response, `is`(equalTo("good")))
    }

    @Test
    fun `shall get instance - case 3`() {
        val responseBody: ResponseBody = mock {
            on { string() } doAnswer { _ ->
                "{\"status\": \"ok\",\"response\": \"good\"}"
            }
        }
        val response: Response = mock {
            on { body } doReturn responseBody
        }

        val testObserver: TestObserver<ApiResponse> = Maybe.just(response).instance(
            object : NormaliseResponse<ApiResponse> {
                override fun invoke(from: String): ApiResponse {
                    return Converter.fromJson(from, ApiResponse::class.java)
                }
            }
        ).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.status, `is`(equalTo("ok")))
        assertThat(result.response, `is`(equalTo("good")))
    }

}