package org.ranapat.roots.tools

import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@RunWith(MockitoJUnitRunner::class)
class DispenserTest {

    @Test
    fun `shall subscribe and dispose`() {
        val disposable = Maybe
            .just(true)
            .subscribe()
        val compositeDisposable = mock<CompositeDisposable> {
            on { add(any<Disposable>()) } doReturn false
            on { add(disposable) } doReturn true
        }

        val instance = object : Dispenser {
            override val compositeDisposable: CompositeDisposable
                get() = compositeDisposable
        }

        instance.subscription(disposable)
        verify(compositeDisposable, times(1)).add(any<Disposable>())
        verify(compositeDisposable, times(1)).add(disposable)

        instance.dispose()
        verify(compositeDisposable, times(1)).clear()
    }

}