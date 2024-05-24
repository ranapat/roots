package org.ranapat.roots.cache

import android.content.Context
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
class CacheTest {
    @Test
    fun `anything`() {
        assertThat(1, `is`(equalTo(1)))
    }

    /*
    @Test
    fun `shall get to - case 1`() {
        val file: File = mock {
            on { absolutePath } doReturn "/tmp"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context, "/_base_test_", CacheDetails.PathStructure.NESTED
        )

        val testObserver: TestObserver<Result> = Cache.to("url", "content").test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.type, `is`(equalTo(Result.Type.TEXT)))
        assertThat(result.source, `is`(equalTo(Result.Source.CACHE)))
        assertThat(result.success, `is`(equalTo(true)))
        assertThat(result.lastModified, `is`(not(equalTo(null))))
        assertThat(result.location, `is`(equalTo("/tmp/_base_test_/url")))
        assertThat(result.content, `is`(equalTo("content")))
        assertThat(result.encoding, `is`(equalTo(Charsets.UTF_8)))

        val cached = File("/tmp/_base_test_/url")
        assertThat(cached.exists(), `is`(equalTo(true)))
        assertThat(cached.readText(Charsets.UTF_8), `is`(equalTo("content")))

        CacheDetails.config = null
        File("/tmp/_base_test_").deleteRecursively()
    }

    @Test
    fun `shall get to - case 2`() {
        val file: File = mock {
            on { absolutePath } doReturn "/tmp"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context, "/_base_test_", CacheDetails.PathStructure.NESTED
        )

        val testObserver: TestObserver<Result> = Cache.to("url", "content", Charsets.US_ASCII).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.type, `is`(equalTo(Result.Type.TEXT)))
        assertThat(result.source, `is`(equalTo(Result.Source.CACHE)))
        assertThat(result.success, `is`(equalTo(true)))
        assertThat(result.lastModified, `is`(not(equalTo(null))))
        assertThat(result.location, `is`(equalTo("/tmp/_base_test_/url")))
        assertThat(result.content, `is`(equalTo("content")))
        assertThat(result.encoding, `is`(equalTo(Charsets.US_ASCII)))

        val cached = File("/tmp/_base_test_/url")
        assertThat(cached.exists(), `is`(equalTo(true)))
        assertThat(cached.readText(Charsets.US_ASCII), `is`(equalTo("content")))

        CacheDetails.config = null
        File("/tmp/_base_test_").deleteRecursively()
    }

    @Test
    fun `shall not get to - case 1`() {
        val file: File = mock {
            on { absolutePath } doReturn "/tmp"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context, "/_base_test_", CacheDetails.PathStructure.NESTED
        )

        val testObserver: TestObserver<Result> = Cache.to("url/../../", "content").test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.type, `is`(equalTo(Result.Type.TEXT)))
        assertThat(result.source, `is`(equalTo(Result.Source.CACHE)))
        assertThat(result.success, `is`(equalTo(false)))
        assertThat(result.lastModified, `is`(equalTo(null)))
        assertThat(result.location, `is`(equalTo(null)))
        assertThat(result.content, `is`(equalTo("content")))
        assertThat(result.encoding, `is`(equalTo(Charsets.UTF_8)))

        CacheDetails.config = null
        File("/tmp/_base_test_").deleteRecursively()
    }

    @Test
    fun `shall not get to - case 2`() {
        val file: File = mock {
            on { absolutePath } doReturn "/"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context, "", CacheDetails.PathStructure.PLAIN
        )

        val testObserver: TestObserver<Result> = Cache.to("url", "content", Charsets.US_ASCII).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.type, `is`(equalTo(Result.Type.TEXT)))
        assertThat(result.source, `is`(equalTo(Result.Source.CACHE)))
        assertThat(result.success, `is`(equalTo(false)))
        assertThat(result.lastModified, `is`(equalTo(0)))
        assertThat(result.location, `is`(equalTo("/url")))
        assertThat(result.content, `is`(equalTo("content")))
        assertThat(result.encoding, `is`(equalTo(Charsets.US_ASCII)))

        CacheDetails.config = null
    }

    @Test
    fun `shall not get to - case 3`() {
        val file: File = mock {
            on { absolutePath } doReturn "/"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context, "", CacheDetails.PathStructure.PLAIN
        )

        val testObserver: TestObserver<Result> = Cache.to("", "content", Charsets.US_ASCII).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.type, `is`(equalTo(Result.Type.TEXT)))
        assertThat(result.source, `is`(equalTo(Result.Source.CACHE)))
        assertThat(result.success, `is`(equalTo(false)))
        assertThat(result.lastModified, `is`(not(equalTo(null))))
        assertThat(result.location, `is`(equalTo("/")))
        assertThat(result.content, `is`(equalTo("content")))
        assertThat(result.encoding, `is`(equalTo(Charsets.US_ASCII)))

        CacheDetails.config = null
    }

    @Test
    fun `shall get from - case 1`() {
        val file: File = mock {
            on { absolutePath } doReturn "/tmp"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context, "/_base_test_", CacheDetails.PathStructure.NESTED
        )

        val testObserverTo: TestObserver<Result> = Cache.to("url", "content").test()
        testObserverTo.await()

        val testObserver: TestObserver<Result> = Cache.from("url").test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.type, `is`(equalTo(Result.Type.TEXT)))
        assertThat(result.source, `is`(equalTo(Result.Source.CACHE)))
        assertThat(result.success, `is`(equalTo(true)))
        assertThat(result.lastModified, `is`(not(equalTo(null))))
        assertThat(result.location, `is`(equalTo("/tmp/_base_test_/url")))
        assertThat(result.content, `is`(equalTo("content")))
        assertThat(result.encoding, `is`(equalTo(Charsets.UTF_8)))

        CacheDetails.config = null
        File("/tmp/_base_test_").deleteRecursively()
    }

    @Test
    fun `shall get from - case 2`() {
        val file: File = mock {
            on { absolutePath } doReturn "/tmp"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context, "/_base_test_", CacheDetails.PathStructure.NESTED
        )

        val testObserverTo: TestObserver<Result> = Cache.to("url", "content", Charsets.US_ASCII).test()
        testObserverTo.await()

        val testObserver: TestObserver<Result> = Cache.from("url", Charsets.US_ASCII).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.type, `is`(equalTo(Result.Type.TEXT)))
        assertThat(result.source, `is`(equalTo(Result.Source.CACHE)))
        assertThat(result.success, `is`(equalTo(true)))
        assertThat(result.lastModified, `is`(not(equalTo(null))))
        assertThat(result.location, `is`(equalTo("/tmp/_base_test_/url")))
        assertThat(result.content, `is`(equalTo("content")))
        assertThat(result.encoding, `is`(equalTo(Charsets.US_ASCII)))

        CacheDetails.config = null
        File("/tmp/_base_test_").deleteRecursively()
    }

    @Test
    fun `shall not get from - case 1`() {
        val file: File = mock {
            on { absolutePath } doReturn "/tmp"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context, "/_base_test_", CacheDetails.PathStructure.NESTED
        )

        val testObserver: TestObserver<Result> = Cache.from("url").test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.type, `is`(equalTo(Result.Type.TEXT)))
        assertThat(result.source, `is`(equalTo(Result.Source.CACHE)))
        assertThat(result.success, `is`(equalTo(false)))
        assertThat(result.lastModified, `is`(equalTo(null)))
        assertThat(result.location, `is`(equalTo(null)))
        assertThat(result.content, `is`(equalTo(null)))
        assertThat(result.encoding, `is`(equalTo(Charsets.UTF_8)))

        CacheDetails.config = null
    }

    @Test
    fun `shall not get from - case 2`() {
        val file: File = mock {
            on { absolutePath } doReturn "/tmp"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context, "", CacheDetails.PathStructure.NESTED
        )

        val testObserver: TestObserver<Result> = Cache.from("").test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.type, `is`(equalTo(Result.Type.TEXT)))
        assertThat(result.source, `is`(equalTo(Result.Source.CACHE)))
        assertThat(result.success, `is`(equalTo(false)))
        assertThat(result.lastModified, `is`(not(equalTo(null))))
        assertThat(result.location, `is`(equalTo("/tmp")))
        assertThat(result.content, `is`(equalTo(null)))
        assertThat(result.encoding, `is`(equalTo(Charsets.UTF_8)))

        CacheDetails.config = null
    }*/

}