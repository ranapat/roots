package org.ranapat.roots

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class GenericTest {
    private class TestObject(
        val field: String
    ) {
        fun test(): String {
            return "test-$field"
        }
        fun testA(value: String): String {
            return "test-$value"
        }
    }

    @Test
    fun `shall do something`() {
        assertThat(4, `is`(equalTo(2 + 2)))

        val test1 = TestObject("wow")
        assertThat(test1.test(), `is`(equalTo("test-wow")))

        val test2 = mock<TestObject> {
            on { test() } doReturn "test-wow"
            on { testA("a") } doReturn "test-A"
            on { testA("b") } doReturn "test-B"
        }
        assertThat(test2.test(), `is`(equalTo("test-wow")))
        assertThat(test2.testA("a"), `is`(equalTo("test-A")))
        assertThat(test2.testA("b"), `is`(equalTo("test-B")))

        verify(test2, times(1)).test()
        verify(test2, times(2)).testA(anyString())
        verify(test2, times(1)).testA("a")
        verify(test2, times(1)).testA("b")
    }
}