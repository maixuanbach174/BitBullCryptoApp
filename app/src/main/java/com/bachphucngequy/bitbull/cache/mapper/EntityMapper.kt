package com.bachphucngequy.bitbull.cache.mapper

interface EntityMapper<CACHED, ENTITY> {
    fun mapFromCached(model: CACHED): ENTITY
    fun mapToCached(model: ENTITY): CACHED
}