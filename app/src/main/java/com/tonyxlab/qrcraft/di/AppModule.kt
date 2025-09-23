package com.tonyxlab.qrcraft.di

import com.tonyxlab.qrcraft.presentation.screens.result.ResultViewModel
import com.tonyxlab.qrcraft.presentation.screens.scan.ScanViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::ScanViewModel)
    viewModelOf(::ResultViewModel)
}