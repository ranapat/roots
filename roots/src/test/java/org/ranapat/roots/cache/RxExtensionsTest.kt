package org.ranapat.roots.cache

import android.content.Context
import com.fasterxml.jackson.annotation.JsonProperty
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.observers.TestObserver
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.ranapat.roots.Result
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class RxExtensionsTest {
    private class ApiResponse(
        @JsonProperty("status") val status: String,
        @JsonProperty("response") val response: String
    )

    @Test
    fun `shall cache - case 1`() {
        val file: File = mock {
            on { absolutePath } doReturn "/tmp"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context, "/_base_test_", CacheDetails.PathStructure.NESTED
        )

        val testObserver: TestObserver<Result> = Maybe.just("{\"status\": \"ok\",\"response\": \"good\"}").cache("url").test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.type, `is`(equalTo(Result.Type.TEXT)))
        assertThat(result.source, `is`(equalTo(Result.Source.CACHE)))
        assertThat(result.success, `is`(equalTo(true)))
        assertThat(result.lastModified, `is`(not(equalTo(null))))
        assertThat(result.location, `is`(equalTo("/tmp/_base_test_/url")))
        assertThat(result.content, `is`(equalTo("{\"status\": \"ok\",\"response\": \"good\"}")))
        assertThat(result.encoding, `is`(equalTo(Charsets.UTF_8)))

        val cached = File("/tmp/_base_test_/url")
        assertThat(cached.exists(), `is`(equalTo(true)))
        assertThat(cached.readText(Charsets.UTF_8), `is`(equalTo("{\"status\": \"ok\",\"response\": \"good\"}")))

        CacheDetails.config = null
        File("/tmp/_base_test_").deleteRecursively()
    }

    @Test
    fun `shall cache - case 2`() {
        val file: File = mock {
            on { absolutePath } doReturn "/tmp"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context, "/_base_test_", CacheDetails.PathStructure.NESTED
        )

        val testObserver: TestObserver<Result> = Maybe.just("{\"status\": \"ok\",\"response\": \"good\"}").cache("url", Charsets.US_ASCII).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.type, `is`(equalTo(Result.Type.TEXT)))
        assertThat(result.source, `is`(equalTo(Result.Source.CACHE)))
        assertThat(result.success, `is`(equalTo(true)))
        assertThat(result.lastModified, `is`(not(equalTo(null))))
        assertThat(result.location, `is`(equalTo("/tmp/_base_test_/url")))
        assertThat(result.content, `is`(equalTo("{\"status\": \"ok\",\"response\": \"good\"}")))
        assertThat(result.encoding, `is`(equalTo(Charsets.US_ASCII)))

        val cached = File("/tmp/_base_test_/url")
        assertThat(cached.exists(), `is`(equalTo(true)))
        assertThat(cached.readText(Charsets.US_ASCII), `is`(equalTo("{\"status\": \"ok\",\"response\": \"good\"}")))

        CacheDetails.config = null
        File("/tmp/_base_test_").deleteRecursively()
    }

    @Test
    fun `shall get instance - case 1`() {
        val file: File = mock {
            on { absolutePath } doReturn "/tmp"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context, "/_base_test_", CacheDetails.PathStructure.NESTED
        )

        val testObserver: TestObserver<ApiResponse> = Maybe.just("{\"status\": \"ok\",\"response\": \"good\"}").cache("url").instance(ApiResponse::class.java).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.status, `is`(equalTo("ok")))
        assertThat(result.response, `is`(equalTo("good")))

        val cached = File("/tmp/_base_test_/url")
        assertThat(cached.exists(), `is`(equalTo(true)))
        assertThat(cached.readText(Charsets.UTF_8), `is`(equalTo("{\"status\": \"ok\",\"response\": \"good\"}")))

        CacheDetails.config = null
        File("/tmp/_base_test_").deleteRecursively()
    }

    @Test
    fun `shall not get instance - case 1`() {
        val file: File = mock {
            on { absolutePath } doReturn "/tmp"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context, "/_base_test_", CacheDetails.PathStructure.NESTED
        )

        val testObserver: TestObserver<ApiResponse> = Maybe.just("{\"_status\": \"ok\",\"_response\": \"good\"}").cache("url").instance(ApiResponse::class.java).test()
        testObserver.await()

        testObserver.assertValueCount(0)
        //testObserver.assertError(MissingKotlinParameterException::class.java)

        val cached = File("/tmp/_base_test_/url")
        assertThat(cached.exists(), `is`(equalTo(true)))
        assertThat(cached.readText(Charsets.UTF_8), `is`(equalTo("{\"_status\": \"ok\",\"_response\": \"good\"}")))

        CacheDetails.config = null
        File("/tmp/_base_test_").deleteRecursively()
    }

    @Test
    fun `shall not get instance - case 2`() {
        val file: File = mock {
            on { absolutePath } doReturn "/"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context, "", CacheDetails.PathStructure.NESTED
        )

        val testObserver: TestObserver<ApiResponse> = Maybe.just("{\"_status\": \"ok\",\"_response\": \"good\"}").cache("url").instance(ApiResponse::class.java).test()
        testObserver.await()

        testObserver.assertValueCount(0)
        testObserver.assertError(CacheFailedException::class.java)

        CacheDetails.config = null
        File("/tmp/_base_test_").deleteRecursively()
    }

}