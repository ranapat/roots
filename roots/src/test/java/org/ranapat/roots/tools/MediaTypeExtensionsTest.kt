package org.ranapat.roots.tools

import okhttp3.MediaType.Companion.toMediaType
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class MediaTypeExtensionsTest {

    @Test
    fun `shall get values - case 1`() {
        val mediaType = "application/json; charset=utf-8".toMediaType()

        assertThat(mediaType.value, `is`(equalTo("application/json")))
        assertThat(mediaType.isApplicationJson, `is`(equalTo(true)))
    }

    @Test
    fun `shall get values - case 2`() {
        val mediaType = "plain/text; charset=utf-8".toMediaType()

        assertThat(mediaType.value, `is`(equalTo("plain/text")))
        assertThat(mediaType.isApplicationJson, `is`(equalTo(false)))
    }

    @Test
    fun `shall get values - case 3`() {
        val mediaType = "octet/stream".toMediaType()

        assertThat(mediaType.value, `is`(equalTo("octet/stream")))
        assertThat(mediaType.isApplicationJson, `is`(equalTo(false)))
    }

    @Test
    fun `shall not get values - case 1`() {
        val mediaType = "undefined/undefined".toMediaType()

        assertThat(mediaType.value, `is`(equalTo("undefined/undefined")))
    }

}