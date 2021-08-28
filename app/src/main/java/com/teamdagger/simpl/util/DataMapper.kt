package com.teamdagger.simpl.util

interface DataMapper<Entity,DomainModel> {
    fun entityToModel(entity: Entity):DomainModel

    fun modelToEntity(domainModel: DomainModel):Entity

    fun entityListToModelList(list:List<Entity>):List<DomainModel>
}