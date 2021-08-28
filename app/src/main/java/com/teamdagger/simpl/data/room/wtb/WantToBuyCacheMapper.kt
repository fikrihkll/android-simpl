package com.teamdagger.simpl.data.room.wtb

import com.teamdagger.simpl.data.model.WantToBuyModel
import com.teamdagger.simpl.util.DataMapper

class WantToBuyCacheMapper:DataMapper<WantToBuyCacheEntity,WantToBuyModel> {
    override fun entityToModel(entity: WantToBuyCacheEntity): WantToBuyModel {
        return WantToBuyModel(
            entity.id,
            entity.name,
            entity.desc,
            entity.progress,
            entity.price,
            entity.pict,
            entity.timestamp,
            entity.deadline,
            entity.userId,
            entity.hasDone
        )
    }

    override fun modelToEntity(domainModel: WantToBuyModel): WantToBuyCacheEntity {
        return WantToBuyCacheEntity(
            domainModel.id,
            domainModel.name,
            domainModel.desc,
            domainModel.progress,
            domainModel.price,
            domainModel.pict,
            domainModel.timestamp,
            domainModel.deadline,
            domainModel.userId,
            domainModel.hasDone
        )
    }

    override fun entityListToModelList(list: List<WantToBuyCacheEntity>): List<WantToBuyModel> {
        return list.map { entityToModel(it) }
    }
}