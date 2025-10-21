package com.tonyxlab.qrcraft

import android.app.Application
import com.tonyxlab.qrcraft.di.dataStoreModule
import com.tonyxlab.qrcraft.di.databaseModule
import com.tonyxlab.qrcraft.di.repositoryModule
import com.tonyxlab.qrcraft.di.useCaseModule
import com.tonyxlab.qrcraft.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class QRCraftApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {

            androidContext(this@QRCraftApp)

            modules(
                    listOf(
                            viewModelModule,
                            dataStoreModule,
                            repositoryModule,
                            databaseModule,
                            useCaseModule
                    )
            )
        }

    }
}
