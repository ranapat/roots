package org.ranapat.roots.example

import android.app.Application
import timber.log.Timber

class RootsExampleApplication : Application() {
    init {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}