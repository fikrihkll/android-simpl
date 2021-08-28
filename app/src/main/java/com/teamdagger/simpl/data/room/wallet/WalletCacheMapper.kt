package com.teamdagger.simpl.data.room.wallet

import com.teamdagger.simpl.data.model.WalletModel
import com.teamdagger.simpl.util.DataMapper

class WalletCacheMapper :DataMapper<WalletCacheEntity,WalletModel>{
    override fun entityToModel(entity: WalletCacheEntity): WalletModel {
        return WalletModel(
            entity.id,
            entity.name,
            entity.userId,
            entity.balance
        )
    }

    override fun modelToEntity(domainModel: WalletModel): WalletCacheEntity {
        return WalletCacheEntity(
            domainModel.id,
            domainModel.name,
            domainModel.userId,
            domainModel.balance
        )
    }

    override fun entityListToModelList(list: List<WalletCacheEntity>): List<WalletModel> {
        return list.map { entityToModel(it) }
    }
}