package com.teamdagger.simpl.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.teamdagger.simpl.data.room.activity.ActivityCacheEntity
import com.teamdagger.simpl.data.room.bank.BankCacheEntity
import com.teamdagger.simpl.data.room.dao.BalanceDao
import com.teamdagger.simpl.data.room.dao.UserDao
import com.teamdagger.simpl.data.room.desc.DescCacheEntity
import com.teamdagger.simpl.data.room.safe_progress.BankSafeProgress
import com.teamdagger.simpl.data.room.safe_progress.WalletSafeProgress
import com.teamdagger.simpl.data.room.user.UserCacherEntity
import com.teamdagger.simpl.data.room.wallet.WalletCacheEntity
import com.teamdagger.simpl.data.room.wtb.WantToBuyCacheEntity

@Database(
    entities = [
        BankCacheEntity::class,
        WalletCacheEntity::class,
        UserCacherEntity::class,
        WantToBuyCacheEntity::class,
        ActivityCacheEntity::class,
        BankSafeProgress::class,
        WalletSafeProgress::class,
        DescCacheEntity::class
    ],
    version = 1
)
abstract class SimplRoomDatabase : RoomDatabase(){

    abstract fun balanceDao(): BalanceDao
    abstract fun userDao(): UserDao

    companion object{
        val DATABASE_NAME = "simpl_db"
    }

}