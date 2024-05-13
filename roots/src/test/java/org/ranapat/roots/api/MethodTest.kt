package org.ranapat.roots.api

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MethodTest {

    @Test
    fun `shall get from string`() {
        assertThat(Method.fromString("post"), `is`(equalTo(Method.POST)))
        assertThat(Method.fromString("POST"), `is`(equalTo(Method.POST)))

        assertThat(Method.fromString("get"), `is`(equalTo(Method.GET)))
        assertThat(Method.fromString("GET"), `is`(equalTo(Method.GET)))

        assertThat(Method.fromString("put"), `is`(equalTo(Method.PUT)))
        assertThat(Method.fromString("PUT"), `is`(equalTo(Method.PUT)))

        assertThat(Method.fromString("delete"), `is`(equalTo(Method.DELETE)))
        assertThat(Method.fromString("DELETE"), `is`(equalTo(Method.DELETE)))

        assertThat(Method.fromString("anything"), `is`(equalTo(Method.UNDEFINED)))
        assertThat(Method.fromString(""), `is`(equalTo(Method.UNDEFINED)))
    }

}