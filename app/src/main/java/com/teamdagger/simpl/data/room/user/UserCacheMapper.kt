package com.teamdagger.simpl.data.room.user

import com.teamdagger.simpl.data.model.UserModel
import com.teamdagger.simpl.util.DataMapper

class UserCacheMapper:DataMapper<UserCacherEntity,UserModel> {
    override fun entityToModel(entity: UserCacherEntity): UserModel {
        return UserModel(
            entity.id,
            entity.name,
            entity.pict,
            entity.email
        )
    }

    override fun modelToEntity(domainModel: UserModel): UserCacherEntity {
        return UserCacherEntity(
            domainModel.id,
            domainModel.name,
            domainModel.pict,
            domainModel.email
        )
    }

    override fun entityListToModelList(list: List<UserCacherEntity>): List<UserModel> {
        return list.map { entityToModel(it) }
    }
}