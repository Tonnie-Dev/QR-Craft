package com.tonyxlab.qrcraft.di

import androidx.room.Room
import com.tonyxlab.qrcraft.data.local.database.HistoryDao
import com.tonyxlab.qrcraft.data.local.database.QrCraftDatabase
import com.tonyxlab.qrcraft.data.local.datastore.DataStore
import com.tonyxlab.qrcraft.data.repository.QrRepositoryImpl
import com.tonyxlab.qrcraft.domain.repository.QrRepository
import com.tonyxlab.qrcraft.domain.usecase.ClearHistoryUseCase
import com.tonyxlab.qrcraft.domain.usecase.DeleteHistoryByIdUseCase
import com.tonyxlab.qrcraft.domain.usecase.GetHistoryByIdUseCase
import com.tonyxlab.qrcraft.domain.usecase.GetHistoryUseCase
import com.tonyxlab.qrcraft.domain.usecase.UpsertHistoryUseCase
import com.tonyxlab.qrcraft.presentation.screens.create.CreateViewModel
import com.tonyxlab.qrcraft.presentation.screens.entry.EntryViewModel
import com.tonyxlab.qrcraft.presentation.screens.history.HistoryViewModel
import com.tonyxlab.qrcraft.presentation.screens.preview.PreviewViewModel
import com.tonyxlab.qrcraft.presentation.screens.result.ResultViewModel
import com.tonyxlab.qrcraft.presentation.screens.scan.ScanViewModel
import com.tonyxlab.qrcraft.utils.Constants
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::ScanViewModel)
    viewModelOf(::ResultViewModel)
    viewModelOf(::CreateViewModel)
    viewModelOf(::EntryViewModel)
    viewModelOf(::PreviewViewModel)
    viewModelOf(::HistoryViewModel)
}

val dataStoreModule = module {
    single { DataStore(context = androidContext()) }
}

val repositoryModule = module {
    single<QrRepository> { QrRepositoryImpl(datastore = get(), dao = get()) }
}

val databaseModule = module {

    single<QrCraftDatabase> {
        Room.databaseBuilder(
                context = androidContext(),
                klass = QrCraftDatabase::class.java,
                name = Constants.DATABASE_NAME
        )
                .fallbackToDestructiveMigration(true)
                .build()
    }
    single<HistoryDao> { get<QrCraftDatabase>().dao }
}

val useCaseModule = module {
    single { GetHistoryUseCase(get()) }
    single { GetHistoryByIdUseCase(get()) }
    single { UpsertHistoryUseCase(get()) }
    single { DeleteHistoryByIdUseCase(get()) }
    single { ClearHistoryUseCase(get()) }
}
