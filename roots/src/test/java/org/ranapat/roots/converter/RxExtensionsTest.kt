package org.ranapat.roots.converter

import com.fasterxml.jackson.annotation.JsonProperty
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.observers.TestObserver
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.ranapat.roots.Result
import org.ranapat.roots.converter.ResultConverter.instance
import org.ranapat.roots.converter.StringConverter.instance

@RunWith(MockitoJUnitRunner::class)
class RxExtensionsTest {
    private class ApiResponse(
        @JsonProperty("status") val status: String,
        @JsonProperty("response") val response: String
    )

    @Test
    fun `shall get instance from string- case 1`() {
        val testObserver: TestObserver<ApiResponse> = Maybe.just("{\"status\": \"ok\",\"response\": \"good\"}").instance(ApiResponse::class.java).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.status, `is`(equalTo("ok")))
        assertThat(result.response, `is`(equalTo("good")))
    }

    @Test
    fun `shall get instance from result - case 1`() {
        val testObserver: TestObserver<ApiResponse> = Maybe.just(Result(
            Result.Source.UNDEFINED,
            true, null, null, null,
            "{\"status\": \"ok\",\"response\": \"good\"}"
        )).instance(ApiResponse::class.java).test()
        testObserver.await()

        testObserver.assertValueCount(1)

        val result = testObserver.values()[0]
        assertThat(result.status, `is`(equalTo("ok")))
        assertThat(result.response, `is`(equalTo("good")))
    }

    @Test
    fun `shall not get instance from result - case 1`() {
        val testObserver: TestObserver<ApiResponse> = Maybe.just(Result(
            Result.Source.UNDEFINED,
            false, null, null, null,
            "{\"status\": \"ok\",\"response\": \"good\"}"
        )).instance(ApiResponse::class.java).test()
        testObserver.await()

        testObserver.assertValueCount(0)
        testObserver.assertError(ConvertFailedException::class.java)
    }

}