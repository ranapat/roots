package org.ranapat.roots.tools

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

interface Dispenser {
    val compositeDisposable: CompositeDisposable

    fun subscription(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }
    fun dispose() {
        compositeDisposable.clear()
    }
}