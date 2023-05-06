package com.example.finalproject.di

import com.example.finalproject.repositories.AuthRepository
import com.example.finalproject.repositories.AuthRepositoryImp
import com.example.finalproject.utils.FIREBASE_URL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL)

    @Provides
    fun providesAuthRepository(impl: AuthRepositoryImp): AuthRepository = impl

}