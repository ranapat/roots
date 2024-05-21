package org.ranapat.roots.cache

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DefaultConfigNotSetExceptionTest {

    @Test
    fun `shall construct`() {
        val exception = DefaultConfigNotSetException()

        assertThat(exception.message, `is`(equalTo("Make sure you set DefaultConfig.config before using the cache")))
    }

}