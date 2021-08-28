package com.teamdagger.simpl.di

import android.content.Context
import androidx.room.Room
import com.teamdagger.simpl.data.room.dao.BalanceDao
import com.teamdagger.simpl.data.room.SimplRoomDatabase
import com.teamdagger.simpl.data.room.activity.ActivityCacheMapper
import com.teamdagger.simpl.data.room.bank.BankCacheMapper
import com.teamdagger.simpl.data.room.dao.UserDao
import com.teamdagger.simpl.data.room.user.UserCacheMapper
import com.teamdagger.simpl.data.room.wallet.WalletCacheMapper
import com.teamdagger.simpl.data.room.wtb.WantToBuyCacheMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Singleton
    @Provides
    fun provideRoomDb(@ApplicationContext context: Context):SimplRoomDatabase{
        return Room.databaseBuilder(
            context,
            SimplRoomDatabase::class.java,
            SimplRoomDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }




    //DAO

    @Singleton
    @Provides
    fun provideBalanceDao(localDb:SimplRoomDatabase): BalanceDao {
        return localDb.balanceDao()
    }

    @Singleton
    @Provides
    fun provideUserDao(localDb:SimplRoomDatabase): UserDao {
        return localDb.userDao()
    }




    //MAPPER

    @Singleton
    @Provides
    fun provideBankMapper():BankCacheMapper{
        return BankCacheMapper()
    }

    @Singleton
    @Provides
    fun provideWalletMapper():WalletCacheMapper{
        return WalletCacheMapper()
    }


    @Singleton
    @Provides
    fun provideSafeMapper():WantToBuyCacheMapper{
        return WantToBuyCacheMapper()
    }

    @Singleton
    @Provides
    fun provideUserMapper():UserCacheMapper{
        return UserCacheMapper()
    }

    @Singleton
    @Provides
    fun provideActivityMapper():ActivityCacheMapper{
        return ActivityCacheMapper()
    }

}