package org.ranapat.roots.converter

import com.fasterxml.jackson.annotation.JsonProperty
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ConverterTest {
    private class ApiResponse(
        @JsonProperty("status") val status: String,
        @JsonProperty("response") val response: String
    )

    @Test
    fun `shall get from json - case 1`() {
        val instance = fromJson<ApiResponse>("{\"status\": \"ok\",\"response\": \"good\"}")
        assertThat(instance.status, `is`(equalTo("ok")))
        assertThat(instance.response, `is`(equalTo("good")))
    }

    @Test
    fun `shall get from json - case 2`() {
        val instance = Converter.fromJson("{\"status\": \"ok\",\"response\": \"good\"}", ApiResponse::class.java)
        assertThat(instance.status, `is`(equalTo("ok")))
        assertThat(instance.response, `is`(equalTo("good")))
    }

}