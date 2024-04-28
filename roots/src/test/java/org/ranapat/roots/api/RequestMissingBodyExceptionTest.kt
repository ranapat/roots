package org.ranapat.roots.api

import okhttp3.Response
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class RequestMissingBodyExceptionTest {

    @Test
    fun `shall construct`() {
        val response = mock<Response> {
            on { code } doReturn 200
        }
        val exception = RequestMissingBodyException("url", Method.GET, response)

        assertThat(exception.url, `is`(equalTo("url")))
        assertThat(exception.method, `is`(equalTo(Method.GET)))
        assertThat(exception.response, `is`(equalTo(response)))
        assertThat(exception.message, `is`(equalTo("GET request to [ url ] passed with status code 200, but has empty body")))
    }

}