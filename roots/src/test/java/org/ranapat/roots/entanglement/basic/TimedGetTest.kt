package org.ranapat.roots.entanglement.basic

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TimedGetTest {
    @JsonIgnoreProperties(ignoreUnknown = true)
    private class ApiResponse(
        @JsonProperty("status") val status: String,
        @JsonProperty("response") val response: String
    )

    @Test
    fun `anything`() {
        assertThat(1, `is`(equalTo(1)))
    }

    /*@Test
    fun `shall flow - case 1`() {
        val file: File = mock {
            on { absolutePath } doReturn "/tmp"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context, "/_base_test_", CacheDetails.PathStructure.NESTED
        )

        val server = MockWebServer()
        val baseUrl = server.url("")
        val response = "{\"status\": \"ok\",\"response\": \"good\"}"

        server.enqueue(
            MockResponse().setBody(response)
        )

        val testObserver: TestObserver<ApiResponse> = TimedGet(baseUrl.toString() + "url", ApiResponse::class.java, 100).flow.test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.status, `is`(equalTo("ok")))
        assertThat(result.response, `is`(equalTo("good")))

        val cached = File("/tmp/_base_test_/url")
        assertThat(cached.exists(), `is`(equalTo(true)))
        assertThat(cached.readText(Charsets.US_ASCII), `is`(equalTo("{\"status\": \"ok\",\"response\": \"good\"}")))

        CacheDetails.config = null
        File("/tmp/_base_test_").deleteRecursively()

        server.shutdown()
    }

    @Test
    fun `shall flow - case 2`() {
        val file: File = mock {
            on { absolutePath } doReturn "/tmp"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context, "/_base_test_", CacheDetails.PathStructure.NESTED
        )

        val server = MockWebServer()
        val baseUrl = server.url("")
        val response = "{\"status\": \"ok\",\"response\": \"good\"}"

        server.enqueue(
            MockResponse().setBody(response)
        )

        val testObserver: TestObserver<ApiResponse> = TimedGet(baseUrl.toString() + "url", ApiResponse::class.java, -100).flow.test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.status, `is`(equalTo("ok")))
        assertThat(result.response, `is`(equalTo("good")))

        val cached = File("/tmp/_base_test_/url")
        assertThat(cached.exists(), `is`(equalTo(true)))
        assertThat(cached.readText(Charsets.US_ASCII), `is`(equalTo("{\"status\": \"ok\",\"response\": \"good\"}")))

        CacheDetails.config = null
        File("/tmp/_base_test_").deleteRecursively()

        server.shutdown()
    }

    @Test
    fun `shall flow - case 3`() {
        val file: File = mock {
            on { absolutePath } doReturn "/tmp"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context, "/_base_test_", CacheDetails.PathStructure.NESTED
        )

        val server = MockWebServer()
        val baseUrl = server.url("")
        val response = "{\"status\": \"ok\",\"response\": \"good\"}"

        server.enqueue(
            MockResponse().setResponseCode(400).setBody(response)
        )

        Cache.to("url", response).test().await()

        val testObserver: TestObserver<ApiResponse> = TimedGet(baseUrl.toString() + "url", ApiResponse::class.java, 100).flow.test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.status, `is`(equalTo("ok")))
        assertThat(result.response, `is`(equalTo("good")))

        val cached = File("/tmp/_base_test_/url")
        assertThat(cached.exists(), `is`(equalTo(true)))
        assertThat(cached.readText(Charsets.US_ASCII), `is`(equalTo("{\"status\": \"ok\",\"response\": \"good\"}")))

        CacheDetails.config = null
        File("/tmp/_base_test_").deleteRecursively()

        server.shutdown()
    }

    @Test
    fun `shall not flow - case 1`() {
        val file: File = mock {
            on { absolutePath } doReturn "/"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context, "", CacheDetails.PathStructure.NESTED
        )

        val server = MockWebServer()
        val baseUrl = server.url("")
        val response = "{\"status\": \"ok\",\"response\": \"good\"}"

        server.enqueue(
            MockResponse().setResponseCode(400).setBody(response)
        )

        val testObserver: TestObserver<ApiResponse> = TimedGet(baseUrl.toString() + "url", ApiResponse::class.java, 100).flow.test()
        testObserver.await()

        testObserver.assertValueCount(0)
        testObserver.assertError(EntanglementFailedException::class.java)

        CacheDetails.config = null

        server.shutdown()
    }

    @Test
    fun `shall not flow - case 2`() {
        val file: File = mock {
            on { absolutePath } doReturn "/"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context, "", CacheDetails.PathStructure.NESTED
        )

        val server = MockWebServer()
        val baseUrl = server.url("")
        val response = "{\"status\": \"ok\",\"response\": \"good\"}"

        server.enqueue(
            MockResponse().setResponseCode(400).setBody(response)
        )

        val testObserver: TestObserver<ApiResponse> = TimedGet(baseUrl.toString() + "url", ApiResponse::class.java, 0).flow.test()
        testObserver.await()

        testObserver.assertValueCount(0)
        testObserver.assertError(EntanglementFailedException::class.java)

        CacheDetails.config = null

        server.shutdown()
    }*/

}