package org.ranapat.roots.api

import io.reactivex.rxjava3.observers.TestObserver
import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class GetJsonTest {
    @Test
    fun `shall get from - case 1`() {
        val server = MockWebServer()
        val baseUrl = server.url("")
        val response = "anything"

        server.enqueue(
            MockResponse().setBody(response)
        )

        val testObserver: TestObserver<Response> = Get.json(baseUrl.toString()).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result, `is`(not(equalTo(null))))

        server.shutdown()
    }

    @Test
    fun `shall get from - case 2`() {
        val response: Response = mock {
            on { isSuccessful } doReturn true
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

        val testObserver: TestObserver<Response> = Get.json(
            "https://localhost",
            okHttpClient = okHttpClient
        ).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result, `is`(not(equalTo(null))))
    }

    @Test
    fun `shall not get from - case 1`() {
        val server = MockWebServer()
        val baseUrl = server.url("")
        val response = "anything"

        server.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(response)
        )

        val testObserver: TestObserver<Response> = Get.json(baseUrl.toString()).test()
        testObserver.await()

        testObserver.assertValueCount(0)
        testObserver.assertError(RequestNotSuccessfulException::class.java)

        server.shutdown()
    }

    @Test
    fun `shall not get from - case 2`() {
        val okHttpClient: OkHttpClient = mock {
            on { newCall(any<Request>()) } doAnswer { _ ->
                throw IOException()
            }
        }
        val server = MockWebServer()
        val baseUrl = server.url("")
        val response = "anything"

        server.enqueue(
            MockResponse().setBody(response)
        )

        val testObserver: TestObserver<Response> = Get.json(
            baseUrl.toString(),
            okHttpClient = okHttpClient
        ).test()
        testObserver.await()

        testObserver.assertValueCount(0)
        testObserver.assertError(IOException::class.java)

        server.shutdown()
    }

    @Test
    fun `shall not get from - case 3`() {
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

        val testObserver: TestObserver<Response> = Get.json(
            "http://localhost",
            okHttpClient = okHttpClient
        ).test()
        testObserver.await()

        testObserver.assertValueCount(0)
        testObserver.assertError(RequestNotSuccessfulException::class.java)
    }

}