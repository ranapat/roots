package org.ranapat.roots.api

import com.fasterxml.jackson.annotation.JsonProperty
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.observers.TestObserver
import okhttp3.Response
import okhttp3.ResponseBody
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.json.JSONObject
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class RxExtensionTest {
    private class ApiResponse(
        @JsonProperty("status") val status: String,
        @JsonProperty("response") val response: String
    )

    @Test
    fun `shall get like - case 1`() {
        val responseBody: ResponseBody = mock {
            on { string() } doAnswer { _ ->
                "{\"status\": \"ok\",\"response\": \"good\"}"
            }
        }
        val response: Response = mock {
            on { body } doReturn responseBody
        }

        val testObserver: TestObserver<ApiResponse> = Maybe.just(response).like(
            ApiResponse::class.java
        ).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.status, `is`(equalTo("ok")))
        assertThat(result.response, `is`(equalTo("good")))
    }

    @Test
    fun `shall get like json - case 2`() {
        val responseBody: ResponseBody = mock {
            on { string() } doAnswer { _ ->
                "{\"status\": \"ok\",\"response\": \"good\"}"
            }
        }
        val response: Response = mock {
            on { body } doReturn responseBody
        }

        val testObserver: TestObserver<ApiResponse> = Maybe.just(response).like(
            ApiResponse::class.java,
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

}