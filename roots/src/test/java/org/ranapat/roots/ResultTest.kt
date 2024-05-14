package org.ranapat.roots

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.util.Date

@RunWith(MockitoJUnitRunner::class)
class ResultTest {

    @Test
    fun `shall construct - case 1`() {
        val time = Date().time
        val result = Result(
            Result.Type.TEXT, Result.Source.API,
            true,
            time, "location", "content", Charsets.UTF_8
        )

        assertThat(result.type, `is`(equalTo(Result.Type.TEXT)))
        assertThat(result.source, `is`(equalTo(Result.Source.API)))
        assertThat(result.success, `is`(equalTo(true)))
        assertThat(result.lastModified, `is`(equalTo(time)))
        assertThat(result.location, `is`(equalTo("location")))
        assertThat(result.content, `is`(equalTo("content")))
        assertThat(result.encoding, `is`(equalTo(Charsets.UTF_8)))
    }

    @Test
    fun `shall construct - case 2`() {
        val exception = Result.TypeNotImplementedException()

        assertThat(exception.message, `is`(equalTo("Type not implemented")))
    }

}