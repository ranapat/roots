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
    fun `shall construct`() {
        assertThat(Method.POST.value, `is`(equalTo("POST")))
        assertThat(Method.GET.value, `is`(equalTo("GET")))
        assertThat(Method.PUT.value, `is`(equalTo("PUT")))
        assertThat(Method.DELETE.value, `is`(equalTo("DELETE")))
    }

}