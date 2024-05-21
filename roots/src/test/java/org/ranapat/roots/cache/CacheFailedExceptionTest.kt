package org.ranapat.roots.cache

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CacheFailedExceptionTest {

    @Test
    fun `shall construct`() {
        val exception = CacheFailedException()

        assertThat(exception.message, `is`(equalTo("Cache failed")))
    }

}