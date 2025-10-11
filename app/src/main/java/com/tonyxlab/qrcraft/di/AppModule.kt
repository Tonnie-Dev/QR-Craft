package com.tonyxlab.qrcraft.di

import com.tonyxlab.qrcraft.data.local.datastore.DataStore
import com.tonyxlab.qrcraft.data.repository.QrRepositoryImpl
import com.tonyxlab.qrcraft.domain.repository.QrRepository
import com.tonyxlab.qrcraft.presentation.screens.create.CreateViewModel
import com.tonyxlab.qrcraft.presentation.screens.result.ResultViewModel
import com.tonyxlab.qrcraft.presentation.screens.scan.ScanViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::ScanViewModel)
    viewModelOf(::ResultViewModel)
    viewModelOf(::CreateViewModel)
}

val dataStoreModule = module {
    single { DataStore(context = androidContext()) }
}

val repositoryModule = module {
    single<QrRepository> { QrRepositoryImpl(datastore = get()) }
}