package com.teamdagger.simpl.di

import com.teamdagger.simpl.data.repo.BalanceRepository
import com.teamdagger.simpl.data.repo.UserRepository
import com.teamdagger.simpl.data.room.activity.ActivityCacheMapper
import com.teamdagger.simpl.data.room.dao.BalanceDao
import com.teamdagger.simpl.data.room.bank.BankCacheMapper
import com.teamdagger.simpl.data.room.dao.UserDao
import com.teamdagger.simpl.data.room.user.UserCacheMapper
import com.teamdagger.simpl.data.room.wallet.WalletCacheMapper
import com.teamdagger.simpl.data.room.wtb.WantToBuyCacheMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideBalanceRepository(
        walletCacheMapper: WalletCacheMapper,
        bankCacheMapper: BankCacheMapper,
        wantToBuyCacheMapper: WantToBuyCacheMapper,
        activityCacheMapper : ActivityCacheMapper,
        balanceDao: BalanceDao
    ):BalanceRepository{
        return BalanceRepository(
            walletCacheMapper,
            bankCacheMapper,
            wantToBuyCacheMapper,
            activityCacheMapper,
            balanceDao
        )
    }

    @Singleton
    @Provides
    fun provideUserRepo(
        userCacheMapper: UserCacheMapper,
        userDao: UserDao
    ):UserRepository{
        return UserRepository(
            userCacheMapper,
            userDao
        )
    }

}