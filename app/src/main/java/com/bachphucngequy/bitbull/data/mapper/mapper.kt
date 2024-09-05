package com.bachphucngequy.bitbull.data.mapper

interface Mapper<DataEntity, DomainModel> {

    fun mapFromEntity(entity: DataEntity): DomainModel

    fun mapToEntity(domainModel: DomainModel): DataEntity
}