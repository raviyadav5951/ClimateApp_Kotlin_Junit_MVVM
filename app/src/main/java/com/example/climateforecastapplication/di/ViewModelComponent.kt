package com.example.climateforecastapplication.di

import com.example.climateforecastapplication.viewmodel.MainActivityViewModel
import dagger.Component


@Component(modules = [ApiModule::class,AppModule::class])
interface ViewModelComponent {

    //location where we will inject this
    fun inject(viewModel:MainActivityViewModel)
}