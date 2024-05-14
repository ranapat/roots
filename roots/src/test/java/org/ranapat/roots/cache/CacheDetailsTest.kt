package org.ranapat.roots.cache

import android.content.Context
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class CacheDetailsTest {

    @Test
    fun `shall construct - case 1`() {
        assertThat(CacheDetails.config, `is`(equalTo(null)))

        val file: File = mock {
            on { absolutePath } doReturn "absolutePath"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context
        )

        assertThat(CacheDetails.config!!.basePath, `is`(equalTo("absolutePath")))
        assertThat(CacheDetails.config!!.prefix, `is`(equalTo(CacheDetails.DEFAULT_PREFIX)))
        assertThat(CacheDetails.config!!.pathStructure, `is`(equalTo(CacheDetails.PathStructure.PLAIN)))

        CacheDetails.config = null
    }

    @Test
    fun `shall construct - case 2`() {
        assertThat(CacheDetails.config, `is`(equalTo(null)))

        val file: File = mock {
            on { absolutePath } doReturn "absolutePath"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context, "prefix"
        )

        assertThat(CacheDetails.config!!.basePath, `is`(equalTo("absolutePath")))
        assertThat(CacheDetails.config!!.prefix, `is`(equalTo("prefix")))
        assertThat(CacheDetails.config!!.pathStructure, `is`(equalTo(CacheDetails.PathStructure.PLAIN)))

        CacheDetails.config = null
    }

    @Test
    fun `shall construct - case 3`() {
        assertThat(CacheDetails.config, `is`(equalTo(null)))

        val file: File = mock {
            on { absolutePath } doReturn "absolutePath"
        }

        val context: Context = mock {
            on { filesDir } doReturn file
        }

        CacheDetails.config = CacheDetails.Config(
            context, "prefix", CacheDetails.PathStructure.NESTED
        )

        assertThat(CacheDetails.config!!.basePath, `is`(equalTo("absolutePath")))
        assertThat(CacheDetails.config!!.prefix, `is`(equalTo("prefix")))
        assertThat(CacheDetails.config!!.pathStructure, `is`(equalTo(CacheDetails.PathStructure.NESTED)))

        CacheDetails.config = null
    }

}