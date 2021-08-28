package com.teamdagger.simpl.data.room.bank

import com.teamdagger.simpl.data.model.BankModel
import com.teamdagger.simpl.util.DataMapper



class BankCacheMapper
constructor():DataMapper<BankCacheEntity,BankModel>{
    override fun entityToModel(entity: BankCacheEntity): BankModel {
        return BankModel(
            entity.id,
            entity.name,
            entity.userId,
            entity.balance,
            entity.rek
        )
    }

    override fun modelToEntity(domainModel: BankModel): BankCacheEntity {
        return BankCacheEntity(
            domainModel.id,
            domainModel.name,
            domainModel.userId,
            domainModel.balance,
            domainModel.rek
        )
    }

    override fun entityListToModelList(list: List<BankCacheEntity>): List<BankModel> {
        return list.map { entityToModel(it) }
    }

}