package com.teamdagger.simpl.data.room.activity

import com.teamdagger.simpl.data.model.ActivityModel
import com.teamdagger.simpl.util.DataMapper

class ActivityCacheMapper:DataMapper<ActivityCacheEntity,ActivityModel> {
    override fun entityToModel(entity: ActivityCacheEntity): ActivityModel {
        return ActivityModel(
            entity.id,
            entity.type,
            entity.desc,
            entity.balance,
            entity.source,
            entity.sourceId,
            entity.dateTime,
            entity.userId
        )
    }

    override fun modelToEntity(domainModel: ActivityModel): ActivityCacheEntity {
        return ActivityCacheEntity(
            domainModel.id,
            domainModel.type,
            domainModel.desc,
            domainModel.balance,
            domainModel.source,
            domainModel.sourceId,
            domainModel.dateTime,
            domainModel.userId
        )
    }

    override fun entityListToModelList(list: List<ActivityCacheEntity>): List<ActivityModel> {
        return list.map { entityToModel(it) }
    }
}