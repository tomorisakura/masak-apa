package com.grevi.masakapa.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grevi.masakapa.common.factory.ViewModelFactory
import com.grevi.masakapa.di.anotaion.ViewModelKey
import com.grevi.masakapa.ui.viewmodel.DatabaseViewModel
import com.grevi.masakapa.ui.viewmodel.RecipesViewModel
import com.grevi.masakapa.ui.viewmodel.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(SingletonComponent::class)
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(splashViewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecipesViewModel::class)
    abstract fun bindRecipesViewModel(recipesViewModel: RecipesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DatabaseViewModel::class)
    abstract fun bindDatabaseViewModel(databaseViewModel: DatabaseViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}