package org.ranapat.roots.entanglement

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class EntanglementFailedExceptionTest {

    @Test
    fun `shall construct`() {
        val exception = EntanglementFailedException()

        assertThat(exception.message, `is`(equalTo("Entanglement failed")))
    }

}