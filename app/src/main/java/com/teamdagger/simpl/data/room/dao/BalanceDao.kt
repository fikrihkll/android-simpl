package com.teamdagger.simpl.data.room.dao

import androidx.room.*
import com.teamdagger.simpl.data.room.activity.ActivityCacheEntity
import com.teamdagger.simpl.data.room.bank.SumCacheEntity
import com.teamdagger.simpl.data.room.bank.BankCacheEntity
import com.teamdagger.simpl.data.room.desc.DescCacheEntity
import com.teamdagger.simpl.data.room.safe_progress.BankSafeProgress
import com.teamdagger.simpl.data.room.safe_progress.WalletSafeProgress
import com.teamdagger.simpl.data.room.wallet.WalletCacheEntity
import com.teamdagger.simpl.data.room.wtb.WantToBuyCacheEntity

@Dao
interface BalanceDao {

    @Query("SELECT SUM(balance) as balance FROM bank WHERE user_id = :userId")
    suspend fun getTotalBankBalance(userId:Int): SumCacheEntity

    @Query("SELECT SUM(balance) as balance FROM wallet WHERE user_id = :userId AND name != 'EMERGENCY'")
    suspend fun getWalletBalance(userId:Int):SumCacheEntity

    @Query("SELECT * FROM wantobuy WHERE user_id = :userId AND name = 'EMERGENCY'")
    suspend fun getEmergencySafe(userId:Int):WantToBuyCacheEntity

    @Query("SELECT * FROM wantobuy WHERE id = :id")
    suspend fun getSafe(id:Int):WantToBuyCacheEntity

    @Query("SELECT * FROM bank WHERE  user_id =:userId")
    suspend fun getBankList(userId:Int):List<BankCacheEntity>

    @Query("SELECT * FROM wallet WHERE user_id = :userId")
    suspend fun getWalletList(userId :Int):List<WalletCacheEntity>

    @Query("SELECT * FROM wantobuy WHERE user_id = :userId")
    suspend fun getMySafe(userId:Int):List<WantToBuyCacheEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBank(bankCacheEntity: BankCacheEntity):Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addWallet(bankCacheEntity: WalletCacheEntity):Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSafe(wantToBuyCacheEntity: WantToBuyCacheEntity):Long

    @Update
    suspend fun updateBank(bankCacheEntity: BankCacheEntity):Int

    @Update
    suspend fun updateWallet(walletCacheEntity: WalletCacheEntity):Int

    @Update
    suspend fun updateSafe(wantToBuyCacheEntity: WantToBuyCacheEntity):Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addDesc(descCacheEntity: DescCacheEntity):Long

    @Query ("SELECT * FROM `desc`")
    suspend fun getListDesc():List<DescCacheEntity>

    @Insert
    suspend fun addActivity(activityCacheEntity: ActivityCacheEntity):Long

    @Query("SELECT * FROM wantobuy WHERE wantobuy.name != 'EMERGENCY'")
    suspend fun getListSafe():List<WantToBuyCacheEntity>



    @Query("UPDATE wantobuy SET progress = progress+:balance WHERE id = :safeId")
    suspend fun updateSafeProgress(balance:Long,safeId:Int):Int

    @Query("UPDATE bank SET balance = balance+:balance WHERE id = :bankId")
    suspend fun updateBankBalance(balance:Long,bankId:Int):Int
    @Query("UPDATE wallet SET balance = balance+:balance WHERE id = :walletId")
    suspend fun updateWalletBalance(balance:Long,walletId:Int):Int

    @Query("UPDATE bank_safe_progress SET bank_balance = bank_balance+:balance WHERE wantobuy_id = :safeId")
    suspend fun updateBankSafeProgress(balance:Long,safeId:Int):Int
    @Query("UPDATE wallet_safe_progress SET wallet_balance = wallet_balance+:balance WHERE wantobuy_id = :safeId")
    suspend fun updateWalletSafeProgress(balance:Long,safeId:Int):Int

    @Query("SELECT COUNT(*) FROM wallet_safe_progress WHERE wantobuy_id = :safeId")
    suspend fun checkWalletSafeProgress(safeId: Int):Int
    @Insert
    suspend fun addWalletSafeProgress(walletSafeProgress: WalletSafeProgress):Long

    @Query("SELECT COUNT(*) FROM bank_safe_progress WHERE wantobuy_id = :safeId")
    suspend fun checkBankSafeProgress(safeId: Int):Int
    @Insert
    suspend fun addBankSafeProgress(walletSafeProgress: BankSafeProgress):Long

}