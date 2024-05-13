package org.ranapat.roots.entanglement

import io.reactivex.rxjava3.core.Maybe

abstract class Base<T : Any> {
    abstract val flow: Maybe<T>
}