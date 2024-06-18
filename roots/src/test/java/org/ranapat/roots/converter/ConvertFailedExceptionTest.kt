package org.ranapat.roots.converter

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ConvertFailedExceptionTest {

    @Test
    fun `shall construct`() {
        val exception = ConvertFailedException()

        assertThat(exception.message, `is`(equalTo("Convert failed")))
    }

}