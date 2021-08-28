package com.teamdagger.simpl.data.repo

import android.util.Log
import com.teamdagger.simpl.data.model.UserModel
import com.teamdagger.simpl.data.room.dao.UserDao
import com.teamdagger.simpl.data.room.user.UserCacheMapper
import com.teamdagger.simpl.data.room.user.UserCacherEntity
import com.teamdagger.simpl.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepository
constructor(
    private val userCacheMapper: UserCacheMapper,
    private val userDao : UserDao
){

    suspend fun getUserData(userId:Int): Flow<DataState<UserModel>> = flow {
        emit(DataState.Loading)
        try {
            var rawCache = userDao.getUser(userId)
            Log.w("SYKL","IN-REPO_user"+rawCache.toString())
            var convertedModel = userCacheMapper.entityToModel(rawCache)
            emit(DataState.CacheSuccess(convertedModel))
        }catch (e:Exception){
            emit(DataState.Error(e))
        }
    }

    suspend fun addUser(user:UserModel): Flow<DataState<Long>> = flow {
        emit(DataState.Loading)
        try {
            user.id= 0
            var entity = userCacheMapper.modelToEntity(user)
            var res= userDao.addNewUser(entity)
            emit(DataState.CacheSuccess(res))
        }catch (e:Exception){
            emit(DataState.Error(e))
        }
    }

}