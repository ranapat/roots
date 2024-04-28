package org.ranapat.roots

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ObjectMapperProviderTest {

    @Test
    fun `shall get the same instance`() {
        val mapper = ObjectMapperProvider.mapper
        assertThat(mapper, `is`(equalTo(ObjectMapperProvider.mapper)))
        assertThat(mapper, `is`(equalTo(ObjectMapperProvider.mapper)))
    }

    @Test
    fun `shall get the new instance`() {
        val mapper = ObjectMapperProvider.mapper()
        assertThat(mapper, `is`(not(equalTo(ObjectMapperProvider.mapper))))
        assertThat(mapper, `is`(not(equalTo(ObjectMapperProvider.mapper()))))
    }

}