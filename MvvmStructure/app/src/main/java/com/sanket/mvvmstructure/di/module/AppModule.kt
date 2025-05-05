package com.sanket.mvvmstructure.di.module

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.sanket.mvvmstructure.data.repository.Repository
import com.sanket.mvvmstructure.databased.Accountdatabased
import com.sanket.mvvmstructure.databased.dao.AccountDao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabased(@ApplicationContext context: Context):Accountdatabased
    {
        return Room.databaseBuilder(
            context,Accountdatabased::class.java,"mydb"
        ).build()
    }
    @Provides
    @Singleton
    fun provideAccountDao(databased:Accountdatabased):AccountDao
    {
        return databased.user()
    }
    @Provides
    @Singleton
    fun provideRepo (accountDao: AccountDao,  @ApplicationContext context: Context,firebaseAuth: FirebaseAuth):Repository
    {
        return Repository(accountDao=accountDao, context = context,firebaseAuth=firebaseAuth)

    }

   @Provides
   fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

//    @Singleton
//    @Provides
//    fun provideAuthRepository(firebaseAuth: FirebaseAuth): AuthRepository =
//        AuthRepository(firebaseAuth)


}



