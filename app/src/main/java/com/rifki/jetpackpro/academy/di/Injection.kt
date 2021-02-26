package com.rifki.jetpackpro.academy.di

import android.content.Context
import com.rifki.jetpackpro.academy.data.AcademyRepository
import com.rifki.jetpackpro.academy.data.source.local.LocalDataSource
import com.rifki.jetpackpro.academy.data.source.local.room.AcademyDatabase
import com.rifki.jetpackpro.academy.data.source.remote.RemoteDataSource
import com.rifki.jetpackpro.academy.utils.AppExecutors
import com.rifki.jetpackpro.academy.utils.JsonHelper

//menghubungkan AcademyRepository dengan ViewModel
object Injection {

    fun provideRepository(context: Context): AcademyRepository {
        val database = AcademyDatabase.getInstance(context)

        val remoteDataSource = RemoteDataSource.getInstance(JsonHelper(context))
        val localDataSource = LocalDataSource.getInstance(database.academyDao())
        val appExecutors = AppExecutors()

        return AcademyRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
    }
}