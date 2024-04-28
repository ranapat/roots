package org.ranapat.roots.api

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class OkHttpClientProviderTest {

    @Test
    fun `shall get the same instance`() {
        val client = OkHttpClientProvider.client
        assertThat(client, `is`(equalTo(OkHttpClientProvider.client)))
        assertThat(client, `is`(equalTo(OkHttpClientProvider.client)))
    }

    @Test
    fun `shall get the new instance`() {
        val client = OkHttpClientProvider.client()
        assertThat(client, `is`(not(equalTo(OkHttpClientProvider.client))))
        assertThat(client, `is`(not(equalTo(OkHttpClientProvider.client()))))
    }

}