package org.ranapat.roots.entanglement

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.observers.TestObserver
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BaseTest {

    @Test
    fun `shall construct flow`() {
        val instance = object : Base<String>() {
            override val flow: Maybe<String>
                get() = Maybe.just("content")
        }

        val testObserver: TestObserver<String> = instance.flow.test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result, `is`(equalTo("content")))
    }

}