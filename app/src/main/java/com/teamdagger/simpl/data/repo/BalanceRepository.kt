package com.teamdagger.simpl.data.repo

import android.util.Log
import com.teamdagger.simpl.data.model.*
import com.teamdagger.simpl.data.room.activity.ActivityCacheEntity
import com.teamdagger.simpl.data.room.activity.ActivityCacheMapper
import com.teamdagger.simpl.data.room.bank.BankCacheEntity
import com.teamdagger.simpl.data.room.dao.BalanceDao
import com.teamdagger.simpl.data.room.bank.BankCacheMapper
import com.teamdagger.simpl.data.room.desc.DescCacheEntity
import com.teamdagger.simpl.data.room.safe_progress.BankSafeProgress
import com.teamdagger.simpl.data.room.safe_progress.WalletSafeProgress
import com.teamdagger.simpl.data.room.wallet.WalletCacheMapper
import com.teamdagger.simpl.data.room.wtb.WantToBuyCacheMapper
import com.teamdagger.simpl.util.DataState
import com.teamdagger.simpl.util.UtilFun
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class BalanceRepository
constructor(
    private val walletCacheMapper: WalletCacheMapper,
    private val bankCacheMapper: BankCacheMapper,
    private val wantToBuyCacheMapper: WantToBuyCacheMapper,
    private val activityCacheMapper: ActivityCacheMapper,
    private val balanceDao: BalanceDao
) {

    suspend fun getTotalBankBalance(userId: Int): Flow<DataState<SumModel>> = flow {
        emit(DataState.Loading)

        try {
            val rawCache = balanceDao.getTotalBankBalance(userId)

            val convertedModel = SumModel(rawCache.balance)
            emit(DataState.CacheSuccess(convertedModel))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    suspend fun getWalletBalance(userId: Int): Flow<DataState<SumModel>> = flow {
        emit(DataState.Loading)

        try {
            var rawCache = balanceDao.getWalletBalance(userId)
            Log.w("SYKL", rawCache.toString())
            var convertedModel = SumModel(rawCache.balance)
            emit(DataState.CacheSuccess(convertedModel))
        } catch (e: Exception) {
            emit(DataState.Error(e))
            Log.w("SYKL", e.toString())
        }
    }

    suspend fun getEmergencyBalance(userId: Int): Flow<DataState<WantToBuyModel>> = flow {
        emit(DataState.Loading)

        try {
            var rawCache = balanceDao.getEmergencySafe(userId)
            Log.w("SYKL", "IN-${rawCache}")
            var convertedModel = wantToBuyCacheMapper.entityToModel(rawCache)
            emit(DataState.CacheSuccess(convertedModel))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    suspend fun addBank(bank: BankModel): Flow<DataState<Long>> = flow {
        emit(DataState.Loading)

        try {
            bank.id = 0 // Reset id to 0, to make it auto generated
            var entity = bankCacheMapper.modelToEntity(bank)
            var res = balanceDao.addBank(entity)
            emit(DataState.Success(res))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    suspend fun addListBank(bank: List<BankModel>): Flow<DataState<Long>> = flow {
        emit(DataState.Loading)

        try {
            var res : Long = 0
            for(i in bank){
                var entity = bankCacheMapper.modelToEntity(i)
                res += balanceDao.addBank(entity)
            }
            Log.w("SYKL",res.toString())
            emit(DataState.Success(res))

        } catch (e: Exception) {
            emit(DataState.Error(e))
            Log.w("SYKL",e.message.toString())
        }
    }

    suspend fun addWallet(wallet: WalletModel): Flow<DataState<Long>> = flow {
        emit(DataState.Loading)

        try {
            wallet.id = 0 // Reset id to 0, to make it auto generated
            var entity = walletCacheMapper.modelToEntity(wallet)
            var res = balanceDao.addWallet(entity)
            emit(DataState.Success(res))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    suspend fun addListWallet(wallet: List<WalletModel>): Flow<DataState<Long>> = flow {
        emit(DataState.Loading)

        try {
            var res : Long = 0
            for(i in wallet){
                var entity = walletCacheMapper.modelToEntity(i)
                res += balanceDao.addWallet(entity)
            }

            emit(DataState.Success(res))

        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    suspend fun addSafe(safe: WantToBuyModel): Flow<DataState<Long>> = flow {
        emit(DataState.Loading)

        try {
            safe.id = 0 // Reset id to 0, to make it auto generated
            var entity = wantToBuyCacheMapper.modelToEntity(safe)
            var res = balanceDao.addSafe(entity)
            emit(DataState.Success(res))
        } catch (e: Exception) {
            emit(DataState.Error(e))
            Log.w("SYKL", e.toString())
        }
    }

    suspend fun getBank(userId: Int): Flow<DataState<List<BankModel>>> = flow {
        emit(DataState.Loading)

        try {
            var cache = balanceDao.getBankList(userId)
            Log.w("SYKL", cache.toString())
            var convertedCache = bankCacheMapper.entityListToModelList(cache)
            emit(DataState.Success(convertedCache))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    suspend fun updateBank(
        bank: BankModel,
        desc: String?,
        type: String,
        balanceAdded: Long,
        userId: Int
    ): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)

        try {
            if(desc!=null){
                var descEntity = DescCacheEntity(desc)
                balanceDao.addDesc(descEntity)
            }

            var actEntity = ActivityCacheEntity(
                0,
                type,
                desc,
                balanceAdded,
                UtilFun.SOURCE_BANK,
                bank.id,
                UtilFun.timestamp(),
                userId
            )
            balanceDao.addActivity(actEntity)

            var entity = bankCacheMapper.modelToEntity(bank)
            var res = balanceDao.updateBank(entity)
            Log.w("SYKL", res.toString())
            if (res > 0)
                emit(DataState.Success(true))
            else
                emit(DataState.Error(Exception("Failed")))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    suspend fun getWallet(userId: Int): Flow<DataState<List<WalletModel>>> = flow {
        emit(DataState.Loading)

        try {
            var cache = balanceDao.getWalletList(userId)
            var convertedCache = walletCacheMapper.entityListToModelList(cache)
            emit(DataState.Success(convertedCache))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    suspend fun updateWallet(
        wallet: WalletModel,
        desc: String?,
        type: String,
        balanceAdded: Long,
        userId: Int
    ): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)

        try {
            if(desc!=null){
                var descEntity = DescCacheEntity(desc)
                balanceDao.addDesc(descEntity)
            }


            var actEntity = ActivityCacheEntity(
                0,
                type,
                desc,
                balanceAdded,
                UtilFun.SOURCE_WALLET,
                wallet.id,
                UtilFun.timestamp(),
                userId
            )
            balanceDao.addActivity(actEntity)

            var entity = walletCacheMapper.modelToEntity(wallet)
            var res = balanceDao.updateWallet(entity)
            Log.w("SYKL","$res")
            if (res > 0)
                emit(DataState.Success(true))
            else
                emit(DataState.Error(Exception("Failed")))
        } catch (e: Exception) {
            emit(DataState.Error(e))
            Log.w("SYKL","${e}")
        }
    }

    suspend fun updateSafe(safe: WantToBuyModel): Flow<DataState<Int>> = flow {
        emit(DataState.Loading)

        try {
            Log.w("BRUH",safe.toString())
            var entity = wantToBuyCacheMapper.modelToEntity(safe)
            var res = balanceDao.updateSafe(entity)
            Log.w("BRUH",res.toString())
            emit(DataState.Success(res))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    suspend fun addDesc(desc: DescModel): Flow<DataState<Boolean>> = flow {
        emit(DataState.Loading)

        try {
            var entity = DescCacheEntity(desc.descName)
            var res = balanceDao.addDesc(entity)

            if (res > 0)
                emit(DataState.Success(true))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    suspend fun getDesc(): Flow<DataState<List<DescModel>>> = flow {
        emit(DataState.Loading)

        try {
            var cache = balanceDao.getListDesc()

            var convertedCache = mutableListOf<DescModel>()
            for (i in cache) {
                convertedCache.add(DescModel(i.descName))
            }
            emit(DataState.Success(convertedCache))

        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    suspend fun getListSafe():Flow<DataState<List<WantToBuyModel>>> = flow{

        emit(DataState.Loading)
        try{
            val cache = balanceDao.getListSafe()
            val convertedModel = wantToBuyCacheMapper.entityListToModelList(cache)
            emit(DataState.Success(convertedModel))
        }catch (e:Exception){
            emit(DataState.Error(e))
        }

    }

    suspend fun updateSafeWithBank(balance:Long,safeId:Int,bankId:Int,activityModel: ActivityModel,updateAccountBalance:Boolean,isEmergency:Boolean):Flow<DataState<Boolean>> = flow{
        emit(DataState.Loading)
        try{
            if(isEmergency){
                var emergCache = balanceDao.getEmergencySafe(UtilFun.userId)
                var emergId = emergCache.id

                var activityCache = activityCacheMapper.modelToEntity(activityModel)

                activityCache.desc = "[Emergency]"
                balanceDao.addActivity(activityCache)

                balanceDao.updateSafeProgress(balance,emergId)

                //Check does the wallet safe progress row exist or not
                var bankSafeProgressCount = balanceDao.checkBankSafeProgress(emergId)
                if(bankSafeProgressCount==0){
                    Log.w("BRUH","IN")
                    balanceDao.addBankSafeProgress(BankSafeProgress(0,bankId,0,emergId))
                }

                balanceDao.updateBankSafeProgress(balance,emergId)

                if(updateAccountBalance)
                    balanceDao.updateBankBalance(balance,bankId)
            }else{
                var activityCache = activityCacheMapper.modelToEntity(activityModel)

                activityCache.desc = "[SAFE-${safeId}]"
                balanceDao.addActivity(activityCache)

                balanceDao.updateSafeProgress(balance,safeId)

                //Check does the wallet safe progress row exist or not
                var bankSafeProgressCount = balanceDao.checkBankSafeProgress(safeId)
                if(bankSafeProgressCount==0){
                    Log.w("BRUH","IN")
                    balanceDao.addBankSafeProgress(BankSafeProgress(0,bankId,0,safeId))
                }

                balanceDao.updateBankSafeProgress(balance,safeId)

                if(updateAccountBalance)
                    balanceDao.updateBankBalance(balance,bankId)
            }





            emit(DataState.Success(true))
        }catch (e:Exception){
            emit(DataState.Error(e))
        }
    }

    suspend fun getSafe(safeId: Int):Flow<DataState<WantToBuyModel>> = flow{
        emit(DataState.Loading)
        try {
            Log.w("BRUH",safeId.toString())
            var cache = balanceDao.getSafe(safeId)
            Log.w("BRUH",cache.toString())
            var convertedCache = wantToBuyCacheMapper.entityToModel(cache)
            emit(DataState.Success(convertedCache))
        }catch (e:Exception){
            Log.w("BRUH",e.toString())
            emit(DataState.Error(e))
        }
    }

    suspend fun updateSafeWithWallet(balance:Long,safeId:Int,walletId:Int,activityModel: ActivityModel,updateAccountBalance:Boolean,isEmergency:Boolean):Flow<DataState<Boolean>> = flow{
        emit(DataState.Loading)
        try{

            if(isEmergency){
                var emergCache = balanceDao.getEmergencySafe(UtilFun.userId)
                var emergId  = emergCache.id

                var activityCache = activityCacheMapper.modelToEntity(activityModel)

                activityCache.desc = "[Emergency]"
                balanceDao.addActivity(activityCache)

                balanceDao.updateSafeProgress(balance,emergId)

                //Check does the wallet safe progress row exist or not
                var walletSafeProgressCount = balanceDao.checkWalletSafeProgress(emergId)
                Log.w("BRUH",walletSafeProgressCount.toString())
                if(walletSafeProgressCount==0){
                    Log.w("BRUH","IN")
                    balanceDao.addWalletSafeProgress(WalletSafeProgress(0,walletId,0,emergId))
                }

                balanceDao.updateWalletSafeProgress(balance,emergId)

                if(updateAccountBalance)
                    balanceDao.updateWalletBalance(balance,walletId)
            }else{
                var activityCache = activityCacheMapper.modelToEntity(activityModel)

                activityCache.desc = "[SAFE-${safeId}]"
                balanceDao.addActivity(activityCache)

                balanceDao.updateSafeProgress(balance,safeId)

                //Check does the wallet safe progress row exist or not
                var walletSafeProgressCount = balanceDao.checkWalletSafeProgress(safeId)
                Log.w("BRUH",walletSafeProgressCount.toString())
                if(walletSafeProgressCount==0){
                    Log.w("BRUH","IN")
                    balanceDao.addWalletSafeProgress(WalletSafeProgress(0,walletId,0,safeId))
                }

                balanceDao.updateWalletSafeProgress(balance,safeId)

                if(updateAccountBalance)
                    balanceDao.updateWalletBalance(balance,walletId)
            }




            emit(DataState.Success(true))
        }catch (e:Exception){
            Log.w("BRUH",e.toString())
            emit(DataState.Error(e))
        }
    }



}