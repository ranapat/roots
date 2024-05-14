package org.ranapat.roots.cache

import android.content.Context
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class BaseTest {

    @Test
    fun `shall ensure config`() {
        CacheDetails.config = mock<CacheDetails.Config>()

        val instance = object : Base() {
            fun doEnsureConfig(): CacheDetails.Config {
                return ensureConfig()
            }
        }

        assertThat(instance.doEnsureConfig(), `is`(equalTo(CacheDetails.config)))

        CacheDetails.config = null
    }

    @Test(expected = DefaultConfigNotSetException::class)
    fun `shall not ensure config`() {
        val instance = object : Base() {
            fun doEnsureConfig(): CacheDetails.Config {
                return ensureConfig()
            }
        }

        instance.doEnsureConfig()
    }

    @Test
    fun `shall ensure path`() {
        val instance = object : Base() {
            fun doEnsurePath(path: String): File? {
                return ensurePath(path)
            }
        }

        assertThat(instance.doEnsurePath("/tmp/"), `is`(not(equalTo(null))))
        assertThat(instance.doEnsurePath("/tmp/_base_test_/file1"), `is`(not(equalTo(null))))
        assertThat(instance.doEnsurePath("/tmp/_base_test_/../../file1"), `is`(equalTo(null)))

        File("/tmp/_base_test_").deleteRecursively()
    }

    @Test
    fun `shall ensure cache file for writing - case 1`() {
        val context: Context = mock {
            on { filesDir } doReturn File("/tmp/")
        }

        CacheDetails.config = CacheDetails.Config(
            context, "/_base_test_", CacheDetails.PathStructure.PLAIN
        )

        val instance = object : Base() {
            fun doEnsureCacheFileForWriting(url: String): File? {
                return ensureCacheFileForWriting(url)
            }
        }

        val path = instance.doEnsureCacheFileForWriting("https://domain.com/path1/path2/file")
        assertThat(path!!.absolutePath, `is`(equalTo("/tmp/_base_test_/_path1_path2_file")))

        File("/tmp/_base_test_").deleteRecursively()
    }

    @Test
    fun `shall ensure cache file for writing - case 2`() {
        val context: Context = mock {
            on { filesDir } doReturn File("/tmp/")
        }

        CacheDetails.config = CacheDetails.Config(
            context, "/_base_test_", CacheDetails.PathStructure.NESTED
        )

        val instance = object : Base() {
            fun doEnsureCacheFileForWriting(url: String): File? {
                return ensureCacheFileForWriting(url)
            }
        }

        val path = instance.doEnsureCacheFileForWriting("https://domain.com/path1/path2/file")
        assertThat(path!!.absolutePath, `is`(equalTo("/tmp/_base_test_/path1/path2/file")))

        File("/tmp/_base_test_").deleteRecursively()
    }

    @Test
    fun `shall not ensure cache file for writing`() {
        val context: Context = mock {
            on { filesDir } doReturn File("/tmp/")
        }

        CacheDetails.config = CacheDetails.Config(
            context, "/_base_test_", CacheDetails.PathStructure.NESTED
        )

        val instance = object : Base() {
            fun doEnsureCacheFileForWriting(url: String): File? {
                return ensureCacheFileForWriting(url)
            }
        }

        val path = instance.doEnsureCacheFileForWriting("https://domain.com/path1/path2/../../../../root/file")
        assertThat(path, `is`(equalTo(null)))

        File("/tmp/_base_test_").deleteRecursively()
    }

    @Test
    fun `shall ensure cache file for reading - case 1`() {
        File("/tmp/_base_test_").apply {
            mkdirs()
        }
        File("/tmp/_base_test_/_path1_path2_file").apply {
            printWriter().use { out ->
                out.println("some content")
            }
        }

        val context: Context = mock {
            on { filesDir } doReturn File("/tmp/")
        }

        CacheDetails.config = CacheDetails.Config(
            context, "/_base_test_", CacheDetails.PathStructure.PLAIN
        )

        val instance = object : Base() {
            fun doEnsureCacheFileForReading(url: String): File? {
                return ensureCacheFileForReading(url)
            }
        }

        val path = instance.doEnsureCacheFileForReading("https://domain.com/path1/path2/file")
        assertThat(path!!.absolutePath, `is`(equalTo("/tmp/_base_test_/_path1_path2_file")))

        File("/tmp/_base_test_").deleteRecursively()
    }

    @Test
    fun `shall ensure cache file for reading - case 2`() {
        File("/tmp/_base_test_/path1/path2").apply {
            mkdirs()
        }
        File("/tmp/_base_test_/path1/path2/file").apply {
            printWriter().use { out ->
                out.println("some content")
            }
        }

        val context: Context = mock {
            on { filesDir } doReturn File("/tmp/")
        }

        CacheDetails.config = CacheDetails.Config(
            context, "/_base_test_", CacheDetails.PathStructure.NESTED
        )

        val instance = object : Base() {
            fun doEnsureCacheFileForReading(url: String): File? {
                return ensureCacheFileForReading(url)
            }
        }

        val path = instance.doEnsureCacheFileForReading("https://domain.com/path1/path2/file")
        assertThat(path!!.absolutePath, `is`(equalTo("/tmp/_base_test_/path1/path2/file")))

        File("/tmp/_base_test_").deleteRecursively()
    }

    @Test
    fun `shall not ensure cache file for reading - case 1`() {
        val context: Context = mock {
            on { filesDir } doReturn File("/tmp/")
        }

        CacheDetails.config = CacheDetails.Config(
            context, "/_base_test_", CacheDetails.PathStructure.PLAIN
        )

        val instance = object : Base() {
            fun doEnsureCacheFileForReading(url: String): File? {
                return ensureCacheFileForReading(url)
            }
        }

        val path = instance.doEnsureCacheFileForReading("https://domain.com/path1/path2/file")
        assertThat(path, `is`(equalTo(null)))

        File("/tmp/_base_test_").deleteRecursively()
    }

    @Test
    fun `shall not ensure cache file for reading - case 2`() {
        val context: Context = mock {
            on { filesDir } doReturn File("/tmp/")
        }

        CacheDetails.config = CacheDetails.Config(
            context, "/_base_test_", CacheDetails.PathStructure.NESTED
        )

        val instance = object : Base() {
            fun doEnsureCacheFileForReading(url: String): File? {
                return ensureCacheFileForReading(url)
            }
        }

        val path = instance.doEnsureCacheFileForReading("https://domain.com/path1/path2/file")
        assertThat(path, `is`(equalTo(null)))

        File("/tmp/_base_test_").deleteRecursively()
    }

    @Test
    fun `shall not ensure cache file for reading - case 3`() {
        val context: Context = mock {
            on { filesDir } doReturn File("/tmp/")
        }

        CacheDetails.config = CacheDetails.Config(
            context, "/_base_test_", CacheDetails.PathStructure.NESTED
        )

        val instance = object : Base() {
            fun doEnsureCacheFileForReading(url: String): File? {
                return ensureCacheFileForReading(url)
            }
        }

        val path = instance.doEnsureCacheFileForReading("https://domain.com/path1/path2/../../../../root/file")
        assertThat(path, `is`(equalTo(null)))

        File("/tmp/_base_test_").deleteRecursively()
    }

}