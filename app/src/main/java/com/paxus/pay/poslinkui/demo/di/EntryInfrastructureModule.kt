package com.paxus.pay.poslinkui.demo.di

import com.paxus.pay.poslinkui.demo.data.DataStoreEntryActionStateStore
import com.paxus.pay.poslinkui.demo.data.EntryActionStateStore
import com.paxus.pay.poslinkui.demo.utils.interfacefilter.EntryActionCatalogRepository
import com.paxus.pay.poslinkui.demo.utils.interfacefilter.StaticEntryActionCatalogRepository
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt bindings for entry infrastructure: catalog repository and persisted entry-action filter state.
 */
@dagger.Module
@InstallIn(SingletonComponent::class)
interface EntryInfrastructureModule {
    @Binds
    @Singleton
    fun bindEntryActionCatalogRepository(
        impl: StaticEntryActionCatalogRepository
    ): EntryActionCatalogRepository

    @Binds
    @Singleton
    fun bindEntryActionStateStore(
        impl: DataStoreEntryActionStateStore
    ): EntryActionStateStore
}
