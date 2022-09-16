/*
 * Created by Nitheesh Ag on 16/09/22, 6:19 AM
 */

package com.example.fubocalender.di

import android.app.Application
import androidx.room.Room
import com.example.fubocalender.data.CalenderDatabase
import com.example.fubocalender.data.repo.CalenderRepo
import com.example.fubocalender.data.repo.CalenderRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCalenderDatabase(app: Application): CalenderDatabase {
        return Room.databaseBuilder(
            app,
            CalenderDatabase::class.java,
            CalenderDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideCalenderRepo(db: CalenderDatabase): CalenderRepo {
        return CalenderRepoImpl(db.calenderDao)
    }
}