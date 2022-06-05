package com.grevi.masakapa.di

import android.content.Context
import androidx.room.Room
import com.grevi.masakapa.data.local.RecipesDAO
import com.grevi.masakapa.data.local.RecipesDataSource
import com.grevi.masakapa.data.local.RecipesDataSourceImpl
import com.grevi.masakapa.data.local.RecipesDatabase
import com.grevi.masakapa.data.remote.data.ApiHelper
import com.grevi.masakapa.data.remote.data.ApiHelperImpl
import com.grevi.masakapa.data.remote.data.ApiService
import com.grevi.masakapa.repository.Repository
import com.grevi.masakapa.repository.RepositoryImpl
import com.grevi.masakapa.repository.mapper.MapperEntity
import com.grevi.masakapa.repository.mapper.MapperImpl
import com.grevi.masakapa.util.Constant
import com.grevi.masakapa.common.factory.ViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun provideOkHttpClient() : OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient) : Retrofit {

        return Retrofit.Builder()
            .baseUrl(Constant.baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) : ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiHelper(apiHelperImpl: ApiHelperImpl) : ApiHelper {
        return apiHelperImpl
    }

    @Provides
    @Singleton
    fun provideRecipesDatabase(@ApplicationContext context: Context) : RecipesDatabase {
        return Room.databaseBuilder(context, RecipesDatabase::class.java, "recipesDB")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideRecipesDAO(database : RecipesDatabase) : RecipesDAO {
        return database.recipesDAO()
    }

    @Provides
    @Singleton
    fun provideRecipesDataSource(recipesDataSourceImpl: RecipesDataSourceImpl) : RecipesDataSource {
        return recipesDataSourceImpl
    }

    @Provides
    @Singleton
    fun provideRepository(repositoryImpl: RepositoryImpl) : Repository {
        return repositoryImpl
    }

    @Provides
    @Singleton
    fun provideMapperEntity(mapperImpl: MapperImpl) : MapperEntity {
        return mapperImpl
    }

    @Provides
    @Singleton
    fun provideViewModelFactory(repository: RepositoryImpl): ViewModelFactory {
        return ViewModelFactory(repository)
    }
}