package org.ranapat.roots.cache.system

import android.content.Context
import io.reactivex.rxjava3.observers.TestObserver
import okhttp3.MediaType.Companion.toMediaType
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

        val testObserver: TestObserver<Result> = Cache.to("url", "content", "application/json; charset=utf-8".toMediaType()).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.source, `is`(equalTo(Result.Source.CACHE)))
        assertThat(result.success, `is`(equalTo(true)))
        assertThat(result.lastModified, `is`(not(equalTo(null))))
        assertThat(result.location, `is`(equalTo("/tmp/_base_test_/url")))
        assertThat(result.mediaType, `is`(equalTo("application/json; charset=utf-8".toMediaType())))
        assertThat(result.content, `is`(equalTo("content")))

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

        val testObserver: TestObserver<Result> = Cache.to("url", "content", "application/json; charset=undefined".toMediaType()).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.source, `is`(equalTo(Result.Source.CACHE)))
        assertThat(result.success, `is`(equalTo(true)))
        assertThat(result.lastModified, `is`(not(equalTo(null))))
        assertThat(result.location, `is`(equalTo("/tmp/_base_test_/url")))
        assertThat(result.mediaType, `is`(equalTo("text/plain; charset=UTF-8".toMediaType())))
        assertThat(result.content, `is`(equalTo("content")))

        val cached = File("/tmp/_base_test_/url")
        assertThat(cached.exists(), `is`(equalTo(true)))
        assertThat(cached.readText(Charsets.UTF_8), `is`(equalTo("content")))

        CacheDetails.config = null
        File("/tmp/_base_test_").deleteRecursively()
    }

    @Test
    fun `shall get to - case 3`() {
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
        assertThat(result.source, `is`(equalTo(Result.Source.CACHE)))
        assertThat(result.success, `is`(equalTo(true)))
        assertThat(result.lastModified, `is`(not(equalTo(null))))
        assertThat(result.location, `is`(equalTo("/tmp/_base_test_/url")))
        assertThat(result.mediaType, `is`(equalTo(null)))
        assertThat(result.content, `is`(equalTo("content")))

        val cached = File("/tmp/_base_test_/url")
        assertThat(cached.exists(), `is`(equalTo(true)))
        assertThat(cached.readText(Charsets.UTF_8), `is`(equalTo("content")))

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
        assertThat(result.source, `is`(equalTo(Result.Source.CACHE)))
        assertThat(result.success, `is`(equalTo(false)))
        assertThat(result.lastModified, `is`(equalTo(null)))
        assertThat(result.location, `is`(equalTo(null)))
        assertThat(result.mediaType, `is`(equalTo(null)))
        assertThat(result.content, `is`(equalTo("content")))

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

        val testObserver: TestObserver<Result> = Cache.to("url", "content", "application/json; charset=utf-8".toMediaType()).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.source, `is`(equalTo(Result.Source.CACHE)))
        assertThat(result.success, `is`(equalTo(false)))
        assertThat(result.lastModified, `is`(equalTo(0)))
        assertThat(result.location, `is`(equalTo("/url")))
        assertThat(result.mediaType, `is`(equalTo("application/json; charset=utf-8".toMediaType())))
        assertThat(result.content, `is`(equalTo("content")))

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

        val testObserver: TestObserver<Result> = Cache.to("", "content", "application/json; charset=utf-8".toMediaType()).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.source, `is`(equalTo(Result.Source.CACHE)))
        assertThat(result.success, `is`(equalTo(false)))
        assertThat(result.lastModified, `is`(not(equalTo(null))))
        assertThat(result.location, `is`(equalTo("/")))
        assertThat(result.mediaType, `is`(equalTo("application/json; charset=utf-8".toMediaType())))
        assertThat(result.content, `is`(equalTo("content")))

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
        assertThat(result.source, `is`(equalTo(Result.Source.CACHE)))
        assertThat(result.success, `is`(equalTo(true)))
        assertThat(result.lastModified, `is`(not(equalTo(null))))
        assertThat(result.location, `is`(equalTo("/tmp/_base_test_/url")))
        assertThat(result.mediaType, `is`(equalTo(null)))
        assertThat(result.content, `is`(equalTo("content")))

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

        val testObserverTo: TestObserver<Result> = Cache.to("url", "content", "application/json; charset=undefined".toMediaType()).test()
        testObserverTo.await()

        val testObserver: TestObserver<Result> = Cache.from("url", "application/json; charset=undefined".toMediaType()).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.source, `is`(equalTo(Result.Source.CACHE)))
        assertThat(result.success, `is`(equalTo(true)))
        assertThat(result.lastModified, `is`(not(equalTo(null))))
        assertThat(result.location, `is`(equalTo("/tmp/_base_test_/url")))
        assertThat(result.mediaType, `is`(equalTo("text/plain; charset=UTF-8".toMediaType())))
        assertThat(result.content, `is`(equalTo("content")))

        CacheDetails.config = null
        File("/tmp/_base_test_").deleteRecursively()
    }

    @Test
    fun `shall get from - case 3`() {
        val file: File = mock {
            on { absolutePath } doReturn "/tmp"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context, "/_base_test_", CacheDetails.PathStructure.NESTED
        )

        val testObserverTo: TestObserver<Result> = Cache.to("url", "content", "application/json; charset=utf-8".toMediaType()).test()
        testObserverTo.await()

        val testObserver: TestObserver<Result> = Cache.from("url", "application/json; charset=utf-8".toMediaType()).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.source, `is`(equalTo(Result.Source.CACHE)))
        assertThat(result.success, `is`(equalTo(true)))
        assertThat(result.lastModified, `is`(not(equalTo(null))))
        assertThat(result.location, `is`(equalTo("/tmp/_base_test_/url")))
        assertThat(result.mediaType, `is`(equalTo("application/json; charset=utf-8".toMediaType())))
        assertThat(result.content, `is`(equalTo("content")))

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
        assertThat(result.source, `is`(equalTo(Result.Source.CACHE)))
        assertThat(result.success, `is`(equalTo(false)))
        assertThat(result.lastModified, `is`(equalTo(null)))
        assertThat(result.location, `is`(equalTo(null)))
        assertThat(result.mediaType, `is`(equalTo(null)))
        assertThat(result.content, `is`(equalTo(null)))

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
        assertThat(result.source, `is`(equalTo(Result.Source.CACHE)))
        assertThat(result.success, `is`(equalTo(false)))
        assertThat(result.lastModified, `is`(not(equalTo(null))))
        assertThat(result.location, `is`(equalTo("/tmp")))
        assertThat(result.mediaType, `is`(equalTo(null)))
        assertThat(result.content, `is`(equalTo(null)))

        CacheDetails.config = null
    }

    @Test
    fun `shall not get from - case 3`() {
        val file: File = mock {
            on { absolutePath } doReturn "/tmp"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context, "", CacheDetails.PathStructure.NESTED
        )

        val testObserver: TestObserver<Result> = Cache.from("", "application/json; charset=utf-8".toMediaType()).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.source, `is`(equalTo(Result.Source.CACHE)))
        assertThat(result.success, `is`(equalTo(false)))
        assertThat(result.lastModified, `is`(not(equalTo(null))))
        assertThat(result.location, `is`(equalTo("/tmp")))
        assertThat(result.mediaType, `is`(equalTo("application/json; charset=utf-8".toMediaType())))
        assertThat(result.content, `is`(equalTo(null)))

        CacheDetails.config = null
    }

}